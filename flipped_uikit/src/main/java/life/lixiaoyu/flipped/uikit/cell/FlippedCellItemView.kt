package life.lixiaoyu.flipped.uikit.cell

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import life.lixiaoyu.flipped.uikit.R

class FlippedCellItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var ivStartIcon: ImageView? = null
    private var tvTitle: TextView? = null
    private var tvSubtitle: TextView? = null
    private var divider: View? = null

    init {
        initView(context)
        initStyle(context, attrs)
    }

    private fun initStyle(context: Context, attrs: AttributeSet?) {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable._FlippedCellItemView)
        setStartIcon(ta.getDrawable(R.styleable._FlippedCellItemView_flipped_start_icon))
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_start_icon_tint_color)) {
            setStartIconTintColor(ta.getColor(R.styleable._FlippedCellItemView_flipped_start_icon_tint_color, -1))
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_title)) {
            setTitleText(ta.getString(R.styleable._FlippedCellItemView_flipped_title)!!)
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_title_color)) {
            setTitleTextColor(ta.getColor(R.styleable._FlippedCellItemView_flipped_title_color, -1))
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_subtitle)) {
            setSubtitleText(ta.getString(R.styleable._FlippedCellItemView_flipped_subtitle)!!)
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_subtitle_color)) {
            setSubtitleTextColor(ta.getColor(R.styleable._FlippedCellItemView_flipped_subtitle_color, -1))
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_show_divider)) {
            setShowDivider(ta.getBoolean(R.styleable._FlippedCellItemView_flipped_show_divider, true))
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_divider_color)) {
            setDividerColor(ta.getColor(R.styleable._FlippedCellItemView_flipped_divider_color, -1))
        }
        ta.recycle()
    }

    private fun initView(context: Context) {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.flipped_cell_item_view, this, true)
        ivStartIcon = view.findViewById(R.id.start_icon)
        tvTitle = view.findViewById(R.id.title)
        tvSubtitle = view.findViewById(R.id.subtitle)
        divider = view.findViewById(R.id.divider)
    }

    fun setStartIcon(drawable: Drawable?) {
        if (drawable == null) {
            ivStartIcon?.visibility = View.GONE
        } else {
            ivStartIcon?.visibility = View.VISIBLE
            ivStartIcon?.setImageDrawable(drawable)
        }
    }

    fun setStartIconTintColor(color: Int) {
        ivStartIcon?.setColorFilter(color)
    }

    fun setTitleText(text: CharSequence) {
        tvTitle?.text = text
    }

    fun setTitleTextColor(color: Int) {
        tvTitle?.setTextColor(color)
    }

    fun setSubtitleText(text: CharSequence) {
        tvSubtitle?.let {
            it.visibility = View.VISIBLE
            it.text = text
        }
    }

    fun setSubtitleTextColor(color: Int) {
        tvSubtitle?.setTextColor(color)
    }

    fun setShowDivider(show: Boolean) {
        divider?.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setDividerColor(dividerColor: Int) {
        divider?.setBackgroundColor(dividerColor)
    }
}