package com.febiarifin.storyappsubmissiondicoding.ui.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.febiarifin.storyappsubmissiondicoding.R

class MyButton: AppCompatButton {
    private var txtColor: Int = 0

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
        this.background = AppCompatResources.getDrawable(context, R.drawable.bg_button)
        postInvalidate()
    }

}