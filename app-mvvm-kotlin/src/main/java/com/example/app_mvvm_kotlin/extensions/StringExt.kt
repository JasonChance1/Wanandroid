package com.example.app_mvvm_kotlin.extensions

import android.content.Context
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