package uz.leeway.android.lib.lollipinx.ext

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import uz.leeway.android.lib.lollipinx.R
import uz.leeway.android.lib.lollipinx.managers.PrefUtils

fun Intent?.getIntNotBlank(key: String, defaultValue: Int): Int =
    this?.getIntExtra(key, defaultValue) ?: defaultValue

fun Fragment.refreshLastActiveMillis() {
    context?.let { it.refreshLastActiveMillis() }
}

fun Context?.refreshLastActiveMillis() {
    this?.let { PrefUtils(it).setLastActiveMillis() }
}

fun ViewGroup?.shake() {
    this?.let { startAnimation(AnimationUtils.loadAnimation(it.context, R.anim.shake)) }
}

fun Context?.pinMakeToast(message: String) {
    this?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
}