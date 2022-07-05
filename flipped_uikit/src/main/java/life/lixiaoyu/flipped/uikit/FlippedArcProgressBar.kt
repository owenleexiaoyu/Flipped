package life.lixiaoyu.flipped.uikit

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.annotation.ColorInt

class FlippedArcProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /**
     * 进度
     */
    private var percent: Float = 0f
        set(value) {
            var percent = value
            if (percent < 0F) {
                percent = 0F
            } else if (percent > 1F) {
                percent = 1F
            }
            if (percent != field) {
                field = percent
                invalidate()
            }
        }


    /**
     * 背景的颜色
     */
    @ColorInt
    private var bgColor: Int = Color.BLACK

    /**
     * 进度条的颜色
     */
    @ColorInt
    private var progressColor: Int = Color.RED

    /**
     * 进度文字的颜色
     */
    @ColorInt
    private var progressTextColor: Int = Color.WHITE

    /**
     * 是否展示进度的文字
     */
    private var isShowProgressText: Boolean = false

    init {
        attrs?.let { set ->
            context.obtainStyledAttributes(set, R.styleable.FlippedArcProgressBar).apply {
                bgColor = getColor(R.styleable.FlippedArcProgressBar_arc_background_color, Color.BLACK)
                progressColor = getColor(R.styleable.FlippedArcProgressBar_arc_progress_color, Color.RED)
                progressTextColor = getColor(R.styleable.FlippedArcProgressBar_arc_progress_text_color, Color.WHITE)
                isShowProgressText = getBoolean(R.styleable.FlippedArcProgressBar_arc_is_show_progress_text, false)
                percent = getFloat(R.styleable.FlippedArcProgressBar_percent, 0F)
                recycle()
            }
        }
    }

    private val bgPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        color = bgColor
    }

    private val progressPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
        color = progressColor
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private val progressTextPaint: Paint by lazy {
        TextPaint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
            color = progressTextColor
        }
    }

    private val bgRectF = RectF()
    private val progressRectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val saveCount = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        drawBackgroundArc(canvas)
        drawProgressArc(canvas)
        canvas.restoreToCount(saveCount)
        drawProgressTextIfNeeded(canvas)
    }

    private fun drawBackgroundArc(canvas: Canvas) {
        val radius = height / 2F
        bgRectF.left = paddingStart.toFloat()
        bgRectF.top = paddingTop.toFloat()
        bgRectF.right = width - paddingEnd.toFloat()
        bgRectF.bottom = height - paddingBottom.toFloat()
        canvas.drawRoundRect(bgRectF, radius, radius, bgPaint)
    }

    /**
     * 这个方法必须在 drawBackgroundArc 之后进行调用
     */
    private fun drawProgressArc(canvas: Canvas) {
        val radius = height / 2F
        progressRectF.left = bgRectF.left
        progressRectF.top = bgRectF.top
        progressRectF.right = bgRectF.left + bgRectF.right * percent
        progressRectF.bottom = bgRectF.bottom
        canvas.drawRoundRect(progressRectF, radius, radius, progressPaint)
    }

    private fun drawProgressTextIfNeeded(canvas: Canvas) {
        if (isShowProgressText && percent > 0.1F) {
            val ts = height / 3F
            val progressText = "${(percent * 100).toInt()}%"
            progressTextPaint.run {
                textSize = ts
                fontMetrics.run {
                    canvas.drawText(
                        progressText,
                        progressRectF.right - progressTextPaint.measureText(progressText) - height / 5F,
                        height / 2F - descent + (descent - ascent) / 2F,
                        progressTextPaint
                    )
                }
            }
        }
    }

    @JvmOverloads
    fun startAnimator(
        timeInterpolator: TimeInterpolator? = AccelerateInterpolator(),
        duration: Long
    ) {
        with(ObjectAnimator.ofFloat(this, "percent", 0F, percent)) {
            interpolator = timeInterpolator
            this.duration = duration
            start()
        }
    }
}