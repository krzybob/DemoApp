package pl.bobinski.demo.core

import android.content.Context

interface Initializer {
    fun onInitialize(applicationContext: Context)
}