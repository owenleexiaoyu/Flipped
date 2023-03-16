package life.lixiaoyu.flipped.uikit.cell

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import life.lixiaoyu.flipped.uikit.R

class FlippedCellItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mStartIconView: ImageView? = null
    private var mStartTextView: TextView? = null
    private fun initStyle(context: Context, attrs: AttributeSet?) {
        val ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable._FlippedCellItemView)
        if (ta.hasValue(R.styleable._FlippedCellItemView_start_text)) {
            setStartText(ta.getString(R.styleable._FlippedCellItemView_start_text))
        }
        if (ta.hasValue(R.styleable._FlippedCellItemView_flipped_start_icon)) {
            setStartIcon(ta.getDrawable(R.styleable._FlippedCellItemView_flipped_start_icon))
        }
        ta.recycle()
    }

    private fun initView(context: Context) {
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.flipped_cell_item_view, this, true)
        mStartIconView = view.findViewById(R.id.start_icon)
        mStartTextView = view.findViewById(R.id.start_text)
    }

    fun setStartIcon(drawable: Drawable?) {
        mStartIconView?.setImageDrawable(drawable)
    }

    fun setStartText(text: CharSequence?) {
        mStartTextView?.text = text
    }

    init {
        initView(context)
        initStyle(context, attrs)
    }
}