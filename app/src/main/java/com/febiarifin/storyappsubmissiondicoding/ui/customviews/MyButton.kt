package com.febiarifin.storyappsubmissiondicoding.ui.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.febiarifin.storyappsubmissiondicoding.R

class MyButton: AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0
    private var buttonTitle: String = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        setTextColor(txtColor)
        gravity = Gravity.CENTER
    }

    fun buttonEnabled(text: String, textSize: Float){
        this.text = text
        this.textSize = textSize
        this.background = context.getDrawable(R.drawable.bg_button)
        postInvalidate()
    }

    fun buttonDisabled(text: String){
        this.text = text
        this.textSize = textSize
        this.background = context.getDrawable(R.drawable.bg_button)
        postInvalidate()
    }

}