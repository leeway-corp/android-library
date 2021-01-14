package uz.leeway.android.lib.lollipinx

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.lollipinx.ext.getIntNotBlank
import uz.leeway.android.lib.lollipinx.ext.pinMakeToast
import uz.leeway.android.lib.lollipinx.ext.shake
import uz.leeway.android.lib.lollipinx.interfaces.KeyboardCallback
import uz.leeway.android.lib.lollipinx.managers.AppLockConst
import uz.leeway.android.lib.lollipinx.managers.LockManager
import uz.leeway.android.lib.lollipinx.managers.PinCodeState
import uz.leeway.android.lib.lollipinx.views.PinCodeRoundView

abstract class AbstractLockActivity<VB : ViewDataBinding> : PinActivity(), KeyboardCallback {

    protected lateinit var binding: VB
    private lateinit var lockManager: LockManager

    private var _pinCodeState = PinCodeState.UNLOCK
    private var _pinCode: String = ""
    private var _oldPinCode: String = ""
    private var _isCodeSuccessful = false
    private var _attempts = 1

    /**
     * Gets the resource id to the {@link View} to be set with {@link #setContentView(int)}.
     * The custom layout must include the following:
     *
     * @return the resource id to the {@link View}
     */
    @LayoutRes
    abstract fun getCustomContentView(): Int

    /**
     * Gets the number of digits in the pin code.  Subclasses can override this to change the
     * length of the pin.
     *
     * @return the number of digits in the PIN
     */
    abstract fun getPinLength(): Int

    /**
     * When the user has succeeded at a pin challenge
     *
     * @param attemptCount the number of attempts the user had used
     */
    abstract fun onPinSuccess(attemptCount: Int)

    /**
     * When the user has failed a pin challenge
     *
     * @param attemptCount the number of attempts the user has used
     */
    abstract fun onPinFailure(attemptCount: Int)

    /**
     * Gets the list of {@link AppLock} types that are acceptable to be backed out of using
     * the device's back button
     *
     * @return an {@link List<PinCodeState>} of {@link AppLock} types which are backable
     */
    abstract fun getCanBackStates(): List<PinCodeState>

    abstract fun getPinCodeRoundView(): PinCodeRoundView
    abstract fun getKeyboardGroup(): ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, getCustomContentView())
        initLayout(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initLayout(intent)
    }

    private fun initLayout(intent: Intent?) {
        overridePendingTransition(R.anim.nothing, R.anim.nothing)

        this.lockManager = LockManager.getInstance(this)
        this.lockManager.setPinChallengeCancelled(false)

        with(intent.getIntNotBlank(AppLockConst.Lock.EXTRA_STATE_CODE, PinCodeState.UNLOCK.code)) {
            binding.setVariable(BR.callback, this@AbstractLockActivity)
            binding.setVariable(BR.pinLength, getPinLength())
            setPinCodeState(
                if (lockManager.isPassCodeSet()) PinCodeState.getByCode(this)
                else PinCodeState.CREATE
            )
        }

    }

    fun getPinCodeState() = _pinCodeState

    private fun setPinCodeState(state: PinCodeState) {
        this._pinCodeState = state
        this.binding.setVariable(BR.state, _pinCodeState)
    }

    private fun setPinCode(code: String) {
        this._pinCode = code
        this.getPinCodeRoundView().refresh(_pinCode.length)
    }

    override fun onButtonClick(index: Int) {
        if (_pinCode.length < this.getPinLength()) {
            setPinCode(_pinCode + index)
        }
        if (_pinCode.length == this.getPinLength()) {
            updateUI()
        }
    }

    override fun onClearClick() {
        if (_pinCode.length < this.getPinLength()) {
            if (_pinCode.isNotEmpty()) {
                setPinCode(_pinCode.substring(0, _pinCode.length - 1))
            } else {
                setPinCode("")
            }
        }
    }

    override fun onBioClick() {
        pinMakeToast("bio options is not implemented")
    }

    private fun updateUI() =
        when (_pinCodeState) {
            PinCodeState.DISABLE -> onStateDisableUI()
            PinCodeState.CREATE -> onStateCreateUI()
            PinCodeState.CONFIRM -> onStateConfirmUI()
            PinCodeState.CHANGE -> onStateChangeUI()
            PinCodeState.UNLOCK -> onStateUnlockUI()
        }

    private fun onStateDisableUI() {
        if (this.lockManager.checkPassCode(_pinCode)) {
            setResult(RESULT_OK)
            setPrefPassCode(null)
            onEnteredSuccessPin()
            this.finish()
        } else {
            onEnteredErrorPin()
        }
    }

    private fun setPrefPassCode(pin: String?) {
        if (this.lockManager.setPassCode(pin)) {
            lockManager.enableAppLock()
        } else {
            lockManager.disableAppLock()
        }
    }

    private fun onStateCreateUI() {
        this._oldPinCode = _pinCode
        setPinCode("")
        setPinCodeState(PinCodeState.CONFIRM)
    }

    private fun onStateConfirmUI() {
        if (_pinCode == _oldPinCode) {
            setResult(RESULT_OK)
            setPrefPassCode(_pinCode)
            onEnteredSuccessPin()
            finish()
        } else {
            _oldPinCode = ""
            setPinCode("")
            setPinCodeState(PinCodeState.CREATE)
            onEnteredErrorPin()
        }
    }

    private fun onStateChangeUI() {
        if (lockManager.checkPassCode(_pinCode)) {
            setPinCode("")
            setPinCodeState(PinCodeState.CREATE)
            onEnteredSuccessPin()
        } else {
            onEnteredErrorPin()
        }
    }

    private fun onStateUnlockUI() {
        if (lockManager.checkPassCode(_pinCode)) {
            setResult(RESULT_OK)
            onEnteredSuccessPin()
            finish()
        } else {
            onEnteredErrorPin()
        }
    }

    private fun onEnteredSuccessPin() {
        this._isCodeSuccessful = true
        onPinSuccess(_attempts)
        this._attempts = 1
    }

    private fun onEnteredErrorPin() {
        onPinFailure(_attempts++)
        runOnUiThread {
            getPinCodeRoundView().refresh(_pinCode.length)
            getKeyboardGroup().shake { setPinCode("") }
        }
    }

    override fun finish() {
        /* If code successful, reset the timer */
        if (_isCodeSuccessful) refreshActiveMillis()

        overridePendingTransition(R.anim.nothing, R.anim.slide_down)
        super.finish()
    }

    override fun onBackPressed() {
        if (getCanBackStates().contains(_pinCodeState)) {
            if (_pinCodeState == PinCodeState.UNLOCK) {
                lockManager.setPinChallengeCancelled(true)
                sendBroadcast(Intent().setAction(AppLockConst.Actions.CANCEL))
            }
            super.onBackPressed()
        }
    }

    fun refreshActiveMillis() = lockManager.refreshActiveMillis()

}