package com.example.common.ui.dialog

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.annotation.RawRes
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.example.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoadingDialog(context: Context) {

    sealed class Anim {
        data class Raw(@RawRes val resId: Int) : Anim()
        data class Asset(val fileName: String) : Anim()
        object None : Anim()
    }

    private val ctx: Context = context
    private var dialog: AlertDialog? = null
    private var tvMsg: TextView? = null
    private var lottie: LottieAnimationView? = null

    var cancelable: Boolean = false

    var defaultAnim: Anim = Anim.Raw(com.example.common_res.R.raw.dialog_loading)

    @MainThread
    fun show(
        msg: String = "加载中...",
        anim: Anim = defaultAnim
    ) {
        val act = ctx.asActivityOrNull() ?: return
        if (act.isFinishing || act.isDestroyed) return

        // 已经显示：直接更新内容（不会重新创建窗口）
        dialog?.let { d ->
            if (d.isShowing) {
                setMessage(msg)
                setAnim(anim)
                return
            }
        }

        val view = LayoutInflater.from(act).inflate(R.layout.dialog_loading, null, false)
        tvMsg = view.findViewById(R.id.tv_msg)
        lottie = view.findViewById(R.id.lottie)

        tvMsg?.text = msg
        setAnim(anim)

        val d = MaterialAlertDialogBuilder(act)
            .setView(view)
            .setCancelable(cancelable)
            .create()

        d.setCanceledOnTouchOutside(cancelable)
        d.setOnDismissListener {
            // 释放引用，避免泄漏
            tvMsg = null
            lottie = null
            dialog = null
        }

        d.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog = d
        d.show()
    }

    @MainThread
    fun hide() {
        dialog?.let { d ->
            if (d.isShowing) d.dismiss()
        }
    }

    @MainThread
    fun setMessage(msg: String) {
        tvMsg?.text = msg
    }

    @MainThread
    fun setAnim(anim: Anim) {
        val lv = lottie ?: return

        // 停掉旧动画，避免切换时残留
        lv.cancelAnimation()
        lv.clearAnimation()

        when (anim) {
            is Anim.Raw -> {
                lv.setAnimation(anim.resId)
                lv.repeatCount = com.airbnb.lottie.LottieDrawable.INFINITE
                lv.playAnimation()
                lv.visibility = android.view.View.VISIBLE
            }

            is Anim.Asset -> {
                lv.setAnimation(anim.fileName)
                lv.repeatCount = com.airbnb.lottie.LottieDrawable.INFINITE
                lv.playAnimation()
                lv.visibility = android.view.View.VISIBLE
            }

            Anim.None -> {
                lv.visibility = android.view.View.GONE
            }
        }
    }

    private fun Context.asActivityOrNull(): Activity? = this as? Activity
}
