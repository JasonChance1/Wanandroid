package com.example.common.extensions

import com.google.android.material.textfield.TextInputEditText

/**
 * @author wandervogel
 * @date 2026-01-06  星期二
 * @description
 */
val TextInputEditText.value
    get()=this.text.toString().trim()