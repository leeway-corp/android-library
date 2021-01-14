package uz.leeway.android.lib.lollipinx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import uz.leeway.android.lib.lollipinx.managers.AppLockConst
import uz.leeway.android.lib.lollipinx.managers.LockManager

abstract class PinActivity : AppCompatActivity {

    constructor() : super()
    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    private lateinit var lockManager: LockManager

    private val _receiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.lockManager = LockManager.getInstance(this)

        registerReceiver(_receiver, IntentFilter(AppLockConst.Actions.CANCEL))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(_receiver)
    }

    override fun onPause() {
        super.onPause()
        if (lockManager.isEnabledLock()) lockManager.onEventPaused(this)
    }

    override fun onResume() {
        super.onResume()
        if (lockManager.isEnabledLock()) lockManager.onEventResumed(this)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (lockManager.isEnabledLock()) lockManager.onEventUserInteraction(this)
    }

}