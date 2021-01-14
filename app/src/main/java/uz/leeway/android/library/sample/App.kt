package uz.leeway.android.library.sample

import android.app.Application
import uz.leeway.android.lib.lollipinx.managers.LockManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        with(LockManager.getInstance(this)) {
            this.setTimeout(5000)
//            this.setPassCode("1234")
        }
    }
}