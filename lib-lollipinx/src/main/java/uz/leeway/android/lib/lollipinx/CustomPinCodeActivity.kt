package uz.leeway.android.lib.lollipinx

import uz.leeway.android.lib.lollipinx.databinding.ActivityPinCodeBinding
import uz.leeway.android.lib.lollipinx.ext.pinMakeToast
import uz.leeway.android.lib.lollipinx.managers.PinCodeState

class CustomPinCodeActivity : AbstractLockActivity<ActivityPinCodeBinding>() {

    override fun getCustomContentView(): Int = R.layout.activity_pin_code

    override fun getPinLength(): Int = 4

    override fun onPinSuccess(attemptCount: Int) {
        pinMakeToast("onPinSuccess")
    }

    override fun onPinFailure(attemptCount: Int) {
        pinMakeToast("onPinFailure")
    }

    override fun getCanBackStates() = PinCodeState.values().asList()

    override fun getPinCodeRoundView() = binding.pinCodeRoundView

    override fun getKeyboardGroup() = binding.keyboardGroup

}