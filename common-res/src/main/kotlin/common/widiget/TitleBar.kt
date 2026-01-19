package common.widiget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.common_res.R

/**
 * @author wandervogel
 * @date 2026-01-15  星期四
 * @description
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var tvTitle: TextView
    private var ivBack: ImageView
    private var ivIcon1: ImageView
    private var ivIcon2: ImageView
    private var tvText1: TextView

    private var onBackClickListener: (() -> Unit)? = null
    private var onIcon1ClickListener: (() -> Unit)? = null
    private var onIcon2ClickListener: (() -> Unit)? = null
    private var onText1ClickListener: (() -> Unit)? = null

    // 属性变量
    private var title: String? = null
    private var icon1ResId: Int = 0
    private var icon2ResId: Int = 0
    private var backIconResId: Int = 0
    private var iconSize: Float = 0f
    private var backIconSize: Float = 0f
    private var iconTint: Int = 0
    private var backIconTint: Int = 0
    private var titleColor: Int = 0
    private var text1: String? = null
    private var text1Color: Int = 0

    init {
        // 设置方向为水平
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.layout_title_bar, this, true)

        // 初始化View
        tvTitle = findViewById(R.id.tv_title)
        ivBack = findViewById(R.id.iv_back)
        ivIcon1 = findViewById(R.id.iv_icon1)
        ivIcon2 = findViewById(R.id.iv_icon2)
        tvText1 = findViewById(R.id.tv_text1)

        // 获取自定义属性
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.TitleBar,
            defStyleAttr,
            0
        )

        try {
            // 解析属性
            title = typedArray.getString(R.styleable.TitleBar_title)
            icon1ResId = typedArray.getResourceId(R.styleable.TitleBar_icon1, 0)
            icon2ResId = typedArray.getResourceId(R.styleable.TitleBar_icon2, 0)
            backIconResId = typedArray.getResourceId(R.styleable.TitleBar_backIcon,  R.drawable.ic_back)
            iconSize = typedArray.getDimension(R.styleable.TitleBar_iconSize, 0f)
            backIconSize = typedArray.getDimension(R.styleable.TitleBar_backIconSize, 0f)

            iconTint = typedArray.getColor(R.styleable.TitleBar_iconTint, 0)
            backIconTint = typedArray.getColor(R.styleable.TitleBar_backIconTint, 0)

            titleColor = typedArray.getColor(R.styleable.TitleBar_titleColor, 0)
            text1 = typedArray.getString(R.styleable.TitleBar_text1)
            text1Color = typedArray.getColor(R.styleable.TitleBar_text1Color, 0)

        } finally {
            typedArray.recycle()
        }

        // 应用属性
        applyAttributes()

        // 设置点击监听
        setupClickListeners()
    }

    private fun applyAttributes() {
        // 设置标题
        tvTitle.text = title ?: ""

        // 设置标题颜色
        if (titleColor != 0) {
            tvTitle.setTextColor(titleColor)
        }

        // 设置返回图标
        if (backIconResId != 0) {
            ivBack.setImageResource(backIconResId)
            ivBack.isVisible = true
        } else {
            ivBack.isVisible = false
        }

        // 设置返回图标大小
        if (backIconSize > 0) {
            ivBack.layoutParams = ivBack.layoutParams.apply {
                width = backIconSize.toInt()
                height = backIconSize.toInt()
            }
        } else if (iconSize > 0) {
            ivBack.layoutParams = ivBack.layoutParams.apply {
                width = iconSize.toInt()
                height = iconSize.toInt()
            }
        }

        // 设置返回图标颜色
        if (backIconTint != 0) {
            ivBack.setColorFilter(backIconTint)
        } else if (iconTint != 0) {
            ivBack.setColorFilter(iconTint)
        }

        // 设置图标1
        if (icon1ResId != 0) {
            ivIcon1.setImageResource(icon1ResId)
            ivIcon1.isVisible = true
            // 设置图标大小
            if (iconSize > 0) {
                ivIcon1.layoutParams = ivIcon1.layoutParams.apply {
                    width = iconSize.toInt()
                    height = iconSize.toInt()
                }
            }
            // 设置图标颜色
            if (iconTint != 0) {
                ivIcon1.setColorFilter(iconTint)
            }
        } else {
            ivIcon1.isVisible = false
        }

        // 设置图标2
        if (icon2ResId != 0) {
            ivIcon2.setImageResource(icon2ResId)
            ivIcon2.isVisible = true
            // 设置图标大小
            if (iconSize > 0) {
                ivIcon2.layoutParams = ivIcon2.layoutParams.apply {
                    width = iconSize.toInt()
                    height = iconSize.toInt()
                }
            }
            // 设置图标颜色
            if (iconTint != 0) {
                ivIcon2.setColorFilter(iconTint)
            }
        } else {
            ivIcon2.isVisible = false
        }

        // 设置右侧文字
        tvText1.text = text1 ?: ""
        if (text1.isNullOrEmpty()) {
            tvText1.isVisible = false
        } else {
            tvText1.isVisible = true
            // 设置文字颜色
            if (text1Color != 0) {
                tvText1.setTextColor(text1Color)
            }
        }
    }

    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            onBackClickListener?.invoke()
        }

        ivIcon1.setOnClickListener {
            onIcon1ClickListener?.invoke()
        }

        ivIcon2.setOnClickListener {
            onIcon2ClickListener?.invoke()
        }

        tvText1.setOnClickListener {
            onText1ClickListener?.invoke()
        }
    }

    // 公共方法：设置标题
    fun setTitle(title: String) {
        this.title = title
        tvTitle.text = title
    }

    // 公共方法：获取标题
    fun getTitle(): String = tvTitle.text.toString()

    // 设置返回图标点击监听
    fun setOnBackClickListener(listener: () -> Unit) {
        this.onBackClickListener = listener
    }

    // 设置图标1点击监听
    fun setOnIcon1ClickListener(listener: () -> Unit) {
        this.onIcon1ClickListener = listener
    }

    // 设置图标2点击监听
    fun setOnIcon2ClickListener(listener: () -> Unit) {
        this.onIcon2ClickListener = listener
    }

    // 设置右侧文字点击监听
    fun setOnText1ClickListener(listener: () -> Unit) {
        this.onText1ClickListener = listener
    }

    // 显示/隐藏返回按钮
    fun showBackIcon(show: Boolean) {
        ivBack.isVisible = show
    }

    // 显示/隐藏图标1
    fun showIcon1(show: Boolean) {
        ivIcon1.isVisible = show
    }

    // 显示/隐藏图标2
    fun showIcon2(show: Boolean) {
        ivIcon2.isVisible = show
    }

    // 显示/隐藏右侧文字
    fun showText1(show: Boolean) {
        tvText1.isVisible = show
    }

    // 设置图标1图片资源
    fun setIcon1(resId: Int) {
        icon1ResId = resId
        if (resId != 0) {
            ivIcon1.setImageResource(resId)
            ivIcon1.isVisible = true
        }
    }

    // 设置图标2图片资源
    fun setIcon2(resId: Int) {
        icon2ResId = resId
        if (resId != 0) {
            ivIcon2.setImageResource(resId)
            ivIcon2.isVisible = true
        }
    }

    // 设置返回图标
    fun setBackIcon(resId: Int) {
        backIconResId = resId
        if (resId != 0) {
            ivBack.setImageResource(resId)
            ivBack.isVisible = true
        }
    }

    // 设置右侧文字
    fun setText1(text: String) {
        this.text1 = text
        tvText1.text = text
        tvText1.isVisible = text.isNotEmpty()
    }

    // 设置图标颜色
    fun setIconTint(color: Int) {
        iconTint = color
        if (color != 0) {
            ivIcon1.setColorFilter(color)
            ivIcon2.setColorFilter(color)
            // 如果返回图标没有单独设置颜色，也应用这个颜色
            if (backIconTint == 0) {
                ivBack.setColorFilter(color)
            }
        }
    }

    // 设置返回图标颜色
    fun setBackIconTint(color: Int) {
        backIconTint = color
        if (color != 0) {
            ivBack.setColorFilter(color)
        }
    }

    // 设置标题颜色
    fun setTitleColor(color: Int) {
        titleColor = color
        tvTitle.setTextColor(color)
    }

    // 设置右侧文字颜色
    fun setText1Color(color: Int) {
        text1Color = color
        tvText1.setTextColor(color)
    }
}