package uz.leeway.android.lib.lollipinx

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import uz.leeway.android.lib.lollipinx.managers.AppLockConst

class AppLockActivity : PinCompatActivity() {

    private var mStepTextView: TextView? = null
    private var mForgotTextView: TextView? = null
    private var mPinCodeRoundView: TextView? = null
    private var mKeyboardView: TextView? = null
    private var mFingerprintImageView: TextView? = null
    private var mFingerprintTextView: TextView? = null

    @LayoutRes
    fun getCustomContentView(): Int = R.layout.activity_pin_code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getCustomContentView())

        initLayout(intent)
    }

    override fun getLayoutRes(): Int = R.layout.activity_pin_code

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initLayout(intent)
    }

    private fun initLayout(intent: Intent?) {
        val type = intent?.extras?.getInt(AppLockConst.Lock.EXTRA_TYPE, AppLockConst.Pin.UNLOCK)
            ?: AppLockConst.Pin.UNLOCK


    }

}