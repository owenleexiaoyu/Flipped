package life.lixiaoyu.flipped.uikit.navbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import life.lixiaoyu.flipped.uikit.R
import life.lixiaoyu.flipped.uikit.utils.ScreenUtils

class FlippedNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var mMainLayout: LinearLayout? = null
    private var mStartView: TextView? = null
    private var mTitleView: TextView? = null
    private var mEndView: TextView? = null
    private var mLineView: View? = null
    private var mStartIconSize = -1
    private var mEndIconSize = -1
    private val mStartIconPadding = 0
    private val mEndIconPadding = 0
    private var mListener: OnFlippedNavBarListener? = null
    private fun initView(context: Context) {
        val builder = Builder(context)
        mMainLayout = builder.mainLayout
        mStartView = builder.startView
        mTitleView = builder.titleView
        mEndView = builder.endView
        mLineView = builder.lineView
        mMainLayout?.addView(mStartView)
        mMainLayout?.addView(mTitleView)
        mMainLayout?.addView(mEndView)
        addView(mMainLayout, 0)
        addView(mLineView, 1)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initStyle(attrs: AttributeSet?) {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable._FlippedNavBar)
        if (ta.hasValue(R.styleable._FlippedNavBar_start_title)) {
            setTitle(mStartView, ta.getString(R.styleable._FlippedNavBar_start_title) ?: "")
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_title)) {
            setTitle(mTitleView, ta.getString(R.styleable._FlippedNavBar_title) ?: "")
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_end_title)) {
            setTitle(mEndView, ta.getString(R.styleable._FlippedNavBar_end_title) ?: "")
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_start_icon_size)) {
            mStartIconSize = ta.getDimensionPixelSize(R.styleable._FlippedNavBar_start_icon_size, -1)
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_end_icon_size)) {
            mEndIconSize = ta.getDimensionPixelSize(R.styleable._FlippedNavBar_end_icon_size, -1)
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_flipped_start_icon)) {
            setStartIcon(
                ContextCompat.getDrawable(
                    context,
                    ta.getResourceId(R.styleable._FlippedNavBar_flipped_start_icon, 0)
                )!!
            )
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_end_icon)) {
            setEndIcon(
                ContextCompat.getDrawable(
                    context,
                    ta.getResourceId(R.styleable._FlippedNavBar_end_icon, 0)
                )!!
            )
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_start_icon_padding)) {
            setStartIconPadding(
                ta.getDimensionPixelSize(
                    R.styleable._FlippedNavBar_start_icon_padding,
                    0
                )
            )
        }
        if (ta.hasValue(R.styleable._FlippedNavBar_end_icon_padding)) {
            setEndIconPadding(ta.getDimensionPixelSize(R.styleable._FlippedNavBar_end_icon_padding, 0))
        }

        // 文字颜色设置
        mStartView?.setTextColor(ta.getColor(R.styleable._FlippedNavBar_start_title_color, -0x99999a))
        mTitleView?.setTextColor(ta.getColor(R.styleable._FlippedNavBar_title_color, -0xddddde))
        mEndView?.setTextColor(ta.getColor(R.styleable._FlippedNavBar_end_title_color, -0x5b5b5c))

        //文字大小设置
        mStartView?.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            ta.getDimensionPixelSize(
                R.styleable._FlippedNavBar_start_title_size,
                ScreenUtils.sp2px(context, 14F)
            ).toFloat()
        )
        mTitleView?.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            ta.getDimensionPixelSize(
                R.styleable._FlippedNavBar_title_size,
                ScreenUtils.sp2px(context, 16F)
            ).toFloat()
        )
        mEndView?.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            ta.getDimensionPixelSize(
                R.styleable._FlippedNavBar_end_title_size,
                ScreenUtils.sp2px(context, 14F)
            ).toFloat()
        )

        //背景设置
        mStartView?.setBackgroundResource(
            ta.getResourceId(
                R.styleable._FlippedNavBar_start_background,
                0
            )
        )
        mEndView?.setBackgroundResource(ta.getResourceId(R.styleable._FlippedNavBar_end_background, 0))

        //分割线设置
        mLineView!!.visibility = if (ta.getBoolean(
                R.styleable._FlippedNavBar_show_bottom_line,
                true
            )
        ) View.VISIBLE else View.GONE
        mLineView!!.setBackgroundColor(
            ta.getColor(
                R.styleable._FlippedNavBar_bottom_line_color,
                -0x131314
            )
        )

        //回收TypedArray
        ta.recycle()

        //设置默认背景
        if (background == null) {
            setBackgroundColor(-0x1)
        }
    }

    private fun setStartIconPadding(startIconPadding: Int) {
        mStartView?.compoundDrawablePadding = startIconPadding
    }

    private fun setEndIconPadding(endIconPadding: Int) {
        mEndView?.compoundDrawablePadding = endIconPadding
    }

    private fun setTitle(view: TextView?, text: CharSequence) {
        view?.text = text
    }

    private fun setStartIcon(drawable: Drawable) {
        if (mStartIconSize != -1) {
            drawable.setBounds(0, 0, mStartIconSize, mStartIconSize)
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        mStartView?.setCompoundDrawables(drawable, null, null, null)
    }

    private fun setEndIcon(drawable: Drawable) {
        if (mEndIconSize != -1) {
            drawable.setBounds(0, 0, mEndIconSize, mEndIconSize)
        } else {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        mEndView?.setCompoundDrawables(null, null, drawable, null)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 设置 titlebar 的默认高度
        val mode: Int = MeasureSpec.getMode(heightMeasureSpec)
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            val heightSpec: Int = MeasureSpec.makeMeasureSpec(
                ScreenUtils.getActionBarHeight(context),
                MeasureSpec.EXACTLY
            )
            super.onMeasure(widthMeasureSpec, heightSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 设置点击事件
        mStartView?.setOnClickListener(this)
        mTitleView?.setOnClickListener(this)
        mEndView?.setOnClickListener(this)
    }

    override fun onDetachedFromWindow() {
        mStartView?.setOnClickListener(null)
        mTitleView?.setOnClickListener(null)
        mEndView?.setOnClickListener(null)
        super.onDetachedFromWindow()
    }

    fun setOnNavBarListener(listener: OnFlippedNavBarListener?) {
        mListener = listener
    }

    override fun onClick(v: View) {
        if (mListener == null) {
            return
        }
        if (v === mStartView) {
            mListener!!.onStartClick(v)
        } else if (v === mTitleView) {
            mListener!!.onTitleClick(v)
        } else if (v === mEndView) {
            mListener!!.onEndClick(v)
        }
    }

    internal class Builder(context: Context) {
        private val mMainLayout: LinearLayout
        private val mStartView: TextView
        private val mTitleView: TextView
        private val mEndView: TextView
        val lineView: View
        val mainLayout: LinearLayout
            get() = mMainLayout
        val startView: TextView
            get() = mStartView
        val titleView: TextView
            get() = mTitleView
        val endView: TextView
            get() = mEndView

        init {
            mMainLayout = LinearLayout(context)
            mMainLayout.orientation = LinearLayout.HORIZONTAL
            mMainLayout.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            mStartView = TextView(context)
            mStartView.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            )
            mStartView.compoundDrawablePadding = ScreenUtils.dp2px(context, 5F)
            mStartView.gravity = Gravity.CENTER_VERTICAL
            mStartView.isSingleLine = true
            mStartView.ellipsize = TextUtils.TruncateAt.END
            mStartView.isEnabled = false
            mTitleView = TextView(context)
            val titleParams: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT)
            titleParams.weight = 1f
            titleParams.leftMargin = ScreenUtils.dp2px(context, 10F)
            titleParams.rightMargin = ScreenUtils.dp2px(context, 10F)
            mTitleView.layoutParams = titleParams
            mTitleView.gravity = Gravity.CENTER
            mTitleView.isSingleLine = true
            mTitleView.ellipsize = TextUtils.TruncateAt.END
            mTitleView.isEnabled = false
            mEndView = TextView(context)
            mEndView.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT
            )
            mEndView.compoundDrawablePadding = ScreenUtils.dp2px(context, 5F)
            mEndView.gravity = Gravity.CENTER_VERTICAL
            mEndView.isSingleLine = true
            mEndView.ellipsize = TextUtils.TruncateAt.END
            mEndView.isEnabled = false
            lineView = View(context)
            val lineParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
            lineParams.gravity = Gravity.BOTTOM
            lineView.layoutParams = lineParams
        }
    }

    init {
        initView(context)
        initStyle(attrs)
    }
}