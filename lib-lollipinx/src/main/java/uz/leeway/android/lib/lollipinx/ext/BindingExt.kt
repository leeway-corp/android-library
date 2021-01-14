package uz.leeway.android.lib.lollipinx.ext

import androidx.databinding.BindingAdapter
import uz.leeway.android.lib.lollipinx.views.PinCodeRoundView

@BindingAdapter(value = ["lp_pin_length"])
fun PinCodeRoundView.setPinLengthBinding(length: Int) = setPinLength(length)