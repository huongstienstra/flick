package com.shinlee.common.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.shinlee.common.R

class CustomBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var selectedItemId = R.id.nav_home
    private var onItemSelectedListener: ((Int) -> Boolean)? = null

    private val navItems = listOf(
        R.id.nav_home,
        R.id.nav_contest,
        R.id.nav_upload,
        R.id.nav_search,
        R.id.nav_profile
    )

    init {
        inflate(context, R.layout.custom_bottom_nav, this)
        setupClickListeners()
        updateSelection(selectedItemId, true)
    }

    private fun setupClickListeners() {
        navItems.forEach { itemId ->
            findViewById<View>(itemId).setOnClickListener { selectItem(itemId) }
        }
    }

    private fun selectItem(itemId: Int) {
        if (itemId != selectedItemId) {
            onItemSelectedListener?.let { listener ->
                if (listener(itemId)) {
                    updateSelection(selectedItemId, false)
                    updateSelection(itemId, true)
                    selectedItemId = itemId
                }
            }
        }
    }

    private fun updateSelection(itemId: Int, isSelected: Boolean) {
        val containerView = findViewById<View>(itemId)
        val iconView = containerView.findViewById<AppCompatImageView>(getIconViewId(itemId))
        val textView = containerView.findViewById<AppCompatTextView>(getTextViewId(itemId))

        containerView.isSelected = isSelected
        iconView.isSelected = isSelected

        textView.setTextColor(if (isSelected) selectedTextColor else unselectedTextColor)
    }

    private fun getIconViewId(itemId: Int): Int {
        return when (itemId) {
            R.id.nav_home -> R.id.nav_home_icon
            R.id.nav_contest -> R.id.nav_contest_icon
            R.id.nav_upload -> R.id.nav_upload_icon
            R.id.nav_search -> R.id.nav_search_icon
            R.id.nav_profile -> R.id.nav_profile_icon
            else -> throw IllegalArgumentException("Unknown itemId: $itemId")
        }
    }

    private fun getTextViewId(itemId: Int): Int {
        return when (itemId) {
            R.id.nav_home -> R.id.nav_home_text
            R.id.nav_contest -> R.id.nav_contest_text
            R.id.nav_upload -> R.id.nav_upload_text
            R.id.nav_search -> R.id.nav_search_text
            R.id.nav_profile -> R.id.nav_profile_text
            else -> throw IllegalArgumentException("Unknown itemId: $itemId")
        }
    }

    fun setOnItemSelectedListener(listener: (Int) -> Boolean) {
        onItemSelectedListener = listener
    }

    companion object {
        private val selectedTextColor = Color.parseColor("#FF0000")
        private val unselectedTextColor = Color.parseColor("#808080")
    }
}