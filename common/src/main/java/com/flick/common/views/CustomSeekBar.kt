package com.flick.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar

class CustomSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    // Increase touch target height while keeping visual height small
    private val touchTargetHeight = context.dpToPx(32f)

    init {
        minimumHeight = touchTargetHeight.toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(touchTargetHeight.toInt(), MeasureSpec.EXACTLY)
        )
    }

    private fun Context.dpToPx(dp: Float): Float {
        return dp * resources.displayMetrics.density
    }
}