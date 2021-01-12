package uz.leeway.android.lib.lollipinx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import uz.leeway.android.lib.lollipinx.managers.AppLockConst
import uz.leeway.android.lib.lollipinx.managers.PinCodeState

abstract class AbstractLockActivity<VB : ViewDataBinding> : BaseAbstractLockActivity<VB>() {

    private var _isEnableAppLock: Boolean = true
    private val _receiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, getCustomContentView())
        registerReceiver(_receiver, IntentFilter(AppLockConst.Actions.CANCEL))
        initLayout(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initLayout(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(_receiver)
    }

    override fun getCanBackStates() = listOf(PinCodeState.DISABLE, PinCodeState.CHANGE)

    override fun enableAppLock() {
        this._isEnableAppLock = true
    }

    override fun disableAppLock() {
        this._isEnableAppLock = false
    }

    override fun onPause() {
        super.onPause()
        if (_isEnableAppLock) lockManager.onEventPaused(this)
    }

    override fun onResume() {
        super.onResume()
        if (_isEnableAppLock) lockManager.onEventResumed(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (_isEnableAppLock) lockManager.onEventUserInteraction(this)
    }

    override fun onSignOutClick() {
        // empty
    }

    override fun onForgotClick() {
        // empty
    }

}