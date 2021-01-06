package uz.leeway.android.library.sample

import android.os.Bundle
import uz.leeway.android.lib.lollipinx.PinCompatActivity

class MainActivity : PinCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_main
}