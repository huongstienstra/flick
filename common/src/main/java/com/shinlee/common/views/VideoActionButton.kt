package com.shinlee.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.shinlee.common.R
import com.shinlee.common.databinding.CustomVideoActionButtonBinding


class VideoActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomVideoActionButtonBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomVideoActionButtonBinding.inflate(inflater, this, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.VideoActionButton, 0, 0)
            val iconResId = typedArray.getResourceId(R.styleable.VideoActionButton_iconSrc, 0)
            val text = typedArray.getString(R.styleable.VideoActionButton_label)

            if (iconResId != 0) {
                setIcon(iconResId)
            }

            setText(text ?: "")
            typedArray.recycle()
        }
    }

     fun setIcon(iconResId: Int) {
        binding.customButtonIcon.setBackgroundResource(iconResId)
    }

     fun setText(text: String) {
        if(text.isNotEmpty()) {
            binding.customButtonText.text = text
            binding.customButtonText.visibility = VISIBLE
        } else {
            binding.customButtonText.visibility = GONE
        }
    }

}