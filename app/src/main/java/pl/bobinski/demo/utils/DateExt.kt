package pl.bobinski.demo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toShortFormat(): String = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(this)
