package com.example.app_mvvm_kotlin.utils

import android.net.http.SslError
import android.webkit.*
import android.webkit.WebView

/**
 * WebView URL 加载状态监听器
 */
interface WebViewUrlLoadListener {
    fun onLoadStarted()                      // 开始加载
    fun onLoadFinished()                     // 加载成功完成
    fun onLoadError(errorCode: Int?, description: String?) // 加载失败
    fun onLoadProgress(progress: Int)        // 加载进度
}

/**
 * URL 加载配置
 */
data class UrlLoadConfig(
    val headers: Map<String, String>? = null,
    val loadListener: WebViewUrlLoadListener? = null,
    val handleSslErrors: Boolean = false,    // 是否处理SSL错误（生产环境建议false）
    val enableJavaScript: Boolean = false,   // 是否启用JavaScript
    val blockNetworkImage: Boolean = false,  // 是否阻止网络图片加载
)

/**
 * 加载 URL 的扩展方法
 */
fun WebView.loadUrl(
    url: String,
    config: UrlLoadConfig = UrlLoadConfig()
) {
    // 配置 WebView 设置
    setupWebViewConfig(config)
    
    // 开始加载回调
    config.loadListener?.onLoadStarted()
    
    // 加载 URL
    if (config.headers != null) {
        this.loadUrl(url, config.headers)
    } else {
        this.loadUrl(url)
    }
}

/**
 * 配置 WebView 设置
 */
private fun WebView.setupWebViewConfig(config: UrlLoadConfig) {
    // 保存原有的设置
    val settings = this.settings
    
    // 启用基本功能
    settings.javaScriptEnabled = config.enableJavaScript
    settings.domStorageEnabled = true
    settings.loadWithOverviewMode = true
    settings.useWideViewPort = true
    settings.builtInZoomControls = true
    settings.displayZoomControls = false
    
    // 根据配置设置缓存策略
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    
    // 设置 WebViewClient 来监听加载状态
    this.webViewClient = createWebViewClient(config)
    
    // 设置 WebChromeClient 来监听进度
    this.webChromeClient = createWebChromeClient(config)
}

/**
 * 创建 WebViewClient
 */
private fun createWebViewClient(config: UrlLoadConfig): WebViewClient {
    return object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
            super.onPageStarted(view, url, favicon)
            config.loadListener?.onLoadStarted()
        }
        
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            config.loadListener?.onLoadFinished()
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            if (request?.isForMainFrame == true) {
                val errorCode =
                    error?.errorCode
                val description =
                    error?.description?.toString()
                config.loadListener?.onLoadError(errorCode, description)
            }
        }

        
        // SSL 错误处理
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            if (config.handleSslErrors) {
                // 生产环境不建议这样做，这里仅演示
                handler?.proceed()
            } else {
                handler?.cancel()
                config.loadListener?.onLoadError(
                    error?.primaryError ?: -1,
                    "SSL Error: ${error?.toString()}"
                )
            }
        }
        
        // 拦截资源请求（例如阻止图片加载）
        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            if (config.blockNetworkImage) {
                request?.let {
                    if (it.url.toString().matches(Regex(".*\\.(jpg|jpeg|png|gif|webp)$", RegexOption.IGNORE_CASE))) {
                        // 阻止网络图片加载，可以返回空或本地占位图
                        return WebResourceResponse("image/png", "UTF-8", null)
                    }
                }
            }
            return super.shouldInterceptRequest(view, request)
        }

        @Deprecated("Deprecated in Java")
        override fun shouldInterceptRequest(
            view: WebView?,
            url: String?
        ): WebResourceResponse? {
            if (config.blockNetworkImage) {
                url?.let {
                    if (it.matches(Regex(".*\\.(jpg|jpeg|png|gif|webp)$", RegexOption.IGNORE_CASE))) {
                        return WebResourceResponse("image/png", "UTF-8", null)
                    }
                }
            }
            return super.shouldInterceptRequest(view, url)
        }
    }
}

/**
 * 创建 WebChromeClient
 */
private fun createWebChromeClient(config: UrlLoadConfig): WebChromeClient {
    return object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            config.loadListener?.onLoadProgress(newProgress)
        }
        
        // 可以添加更多 ChromeClient 的回调
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            // 如果需要可以在这里处理标题
        }
        
        override fun onReceivedIcon(view: WebView?, icon: android.graphics.Bitmap?) {
            super.onReceivedIcon(view, icon)
            // 处理网站图标
        }
    }
}

/**
 * DSL 风格的加载方法
 */
fun WebView.loadUrl(
    url: String,
    config: UrlLoadConfig.() -> Unit = {}
) {
    val loadConfig = UrlLoadConfig().apply(config)
    this.loadUrl(url, loadConfig)
}

/**
 * 简化的加载方法（最基本的使用）
 */
fun WebView.loadUrlWithListener(
    url: String,
    listener: WebViewUrlLoadListener? = null
) {
    this.loadUrl(url, UrlLoadConfig(loadListener = listener))
}

/**
 * 带 headers 的加载方法
 */
fun WebView.loadUrlWithHeaders(
    url: String,
    headers: Map<String, String>,
    listener: WebViewUrlLoadListener? = null
) {
    this.loadUrl(url, UrlLoadConfig(headers = headers, loadListener = listener))
}

/**
 * 默认的监听器实现，方便使用
 */
open class DefaultWebViewLoadListener : WebViewUrlLoadListener {
    override fun onLoadStarted() {}
    override fun onLoadFinished() {}
    override fun onLoadError(errorCode: Int?, description: String?) {}
    override fun onLoadProgress(progress: Int) {}
}