package com.example.app_mvvm_kotlin.extensions

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.app_mvvm_kotlin.App

/**
 * @author wandervogel
 * @date 2026-01-16  星期五
 * @description
 */

fun String?.toast(context:Context = App.instance.applicationContext){
    this?.takeIf { this.isNotEmpty() }?.let {
        Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
    }
}
fun EditText?.onSearch(searchFun: (() -> Unit)) {
    this?.setOnEditorActionListener { _, actionId, event ->
        var result = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event.keyCode == KeyEvent.KEYCODE_ENTER)) {
            searchFun.invoke()
            result = true
        }
        result
    }
}