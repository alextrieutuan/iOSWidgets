package com.alext.ioswidgets

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.alext.ioswidgets.R

internal class SpinView : ImageView {

    private var mRotateDegrees: Float = 0.toFloat()
    private var mFrameTime: Int = 0
    private var mNeedToUpdateView: Boolean = false
    private var mUpdateViewRunnable: Runnable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        setImageResource(R.drawable.ui_alert_loading_spinner)
        mFrameTime = 1000 / 12
        mUpdateViewRunnable = object : Runnable {
            override fun run() {
                mRotateDegrees += 30f
                mRotateDegrees = if (mRotateDegrees < 360) mRotateDegrees else mRotateDegrees - 360
                invalidate()
                if (mNeedToUpdateView) {
                    postDelayed(this, mFrameTime.toLong())
                }
            }
        }
    }

    fun setAnimationSpeed(scale: Float) {
        mFrameTime = (1000f / 12f / scale).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(mRotateDegrees, (width / 2).toFloat(), (height / 2).toFloat())
        super.onDraw(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mNeedToUpdateView = true
        post(mUpdateViewRunnable)
    }

    override fun onDetachedFromWindow() {
        mNeedToUpdateView = false
        super.onDetachedFromWindow()
    }
}
