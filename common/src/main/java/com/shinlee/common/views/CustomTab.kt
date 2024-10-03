package com.shinlee.common.views

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.animation.ValueAnimator
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.StyleRes
import com.shinlee.common.R

import androidx.appcompat.widget.AppCompatTextView


class CustomTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var tabLeft: AppCompatTextView
    private lateinit var tabRight: AppCompatTextView
    private lateinit var tabIndicator: View

    private var selectedTab: AppCompatTextView? = null
    private var onTabSelectedListener: ((Int) -> Unit)? = null
    private var currentAnimator: Animator? = null

    private val handler = Handler(Looper.getMainLooper())
    private var initialSelectionRunnable: Runnable? = null

    init {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.custom_tab, this, true)
        initViews()
        setupListeners()

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomTab,
            0, 0
        ).apply {
            try {
                val leftTabText = getString(R.styleable.CustomTab_leftTabText) ?: "Left Tab"
                val rightTabText = getString(R.styleable.CustomTab_rightTabText) ?: "Right Tab"
                tabLeft.text = leftTabText
                tabRight.text = rightTabText

                val textAppearance = getResourceId(R.styleable.CustomTab_textAppearance, 0)
                if (textAppearance != 0) {
                    setTextAppearance(textAppearance)
                }
            } finally {
                recycle()
            }
        }

        initialSelectionRunnable = Runnable { selectDefaultTab() }
        handler.post(initialSelectionRunnable!!)

    }

    private fun initViews() {
        tabLeft = findViewById(R.id.tabFollowing)
        tabRight = findViewById(R.id.tabForYou)
        tabIndicator = findViewById(R.id.tabIndicator)
    }

    private fun setTextAppearance(@StyleRes style: Int) {
        tabLeft.setTextAppearance(context, style)
        tabRight.setTextAppearance(context, style)
    }

    private fun setupListeners() {
        tabLeft.setOnClickListener { selectTab(0) }
        tabRight.setOnClickListener { selectTab(1) }
    }

    private fun selectTab(index: Int) {
        val selectedTab = when (index) {
            0 -> tabLeft
            1 -> tabRight
            else -> return
        }
        animateIndicator(selectedTab)
        updateTabAppearance(selectedTab)
        onTabSelectedListener?.invoke(index)
    }

    private fun selectDefaultTab() {
        val selectedTab = tabRight
        positionIndicator(selectedTab)
        onTabSelectedListener?.invoke(1)
    }

    private fun animateIndicator(selectedTab: AppCompatTextView) {
        currentAnimator?.cancel()

        val indicatorWidth = selectedTab.width / 3
        val params = tabIndicator.layoutParams
        params.width = indicatorWidth
        tabIndicator.layoutParams = params

        val targetX = selectedTab.x + (selectedTab.width - indicatorWidth) / 2

        val animator = ValueAnimator.ofFloat(tabIndicator.x, targetX)
        animator.addUpdateListener { animation ->
            tabIndicator.x = animation.animatedValue as Float
        }
        animator.duration = 300
        animator.start()

        currentAnimator = animator
    }

    private fun positionIndicator(selectedTab: AppCompatTextView) {
        val indicatorWidth = selectedTab.width / 3
        val params = tabIndicator.layoutParams
        params.width = indicatorWidth
        tabIndicator.layoutParams = params
        tabIndicator.x = selectedTab.x + (selectedTab.width - indicatorWidth) / 2

        updateTabAppearance(selectedTab)
    }

    private fun updateTabAppearance(selectedTab: AppCompatTextView) {
        this.selectedTab = selectedTab
        tabLeft.isSelected = selectedTab == tabLeft
        tabRight.isSelected = selectedTab == tabRight
        tabLeft.apply {
            isSelected = this == selectedTab
            setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
        tabRight.apply {
            isSelected = this == selectedTab
            setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    fun setOnTabSelectedListener(listener: (Int) -> Unit) {
        onTabSelectedListener = listener
    }

    fun setTabTexts(leftText: String, rightText: String) {
        tabLeft.text = leftText
        tabRight.text = rightText
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cleanup()
    }

    private fun cleanup() {
        currentAnimator?.cancel()
        currentAnimator = null
        initialSelectionRunnable?.let { handler.removeCallbacks(it) }
        initialSelectionRunnable = null
        onTabSelectedListener = null
    }
}