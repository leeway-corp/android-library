package uz.leeway.android.lib.lollipinx

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.lollipinx.interfaces.KeyboardCallback
import uz.leeway.android.lib.lollipinx.managers.PinCodeState

abstract class PinCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ViewDataBinding>(this, getLayoutRes())
        binding.setVariable(BR.callback, KeyboardListener())
        binding.setVariable(BR.state, PinCodeState.UNLOCK)
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int


    private fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private inner class KeyboardListener : KeyboardCallback {
        override fun onSignOutClick() {
            makeToast("onSignOutClick")
        }

        override fun onForgotClick() {
            makeToast("onForgotClick")
        }

        override fun onButtonClick(index: Int) {
            makeToast("onButtonClick[$index]")
        }

        override fun onClearClick() {
            makeToast("onClearClick")
        }

        override fun onBioClick() {
            makeToast("onBioClick")
        }
    }

}