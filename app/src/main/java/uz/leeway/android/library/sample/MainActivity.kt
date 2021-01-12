package uz.leeway.android.library.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.leeway.android.lib.lollipinx.CustomPinCodeActivity
import uz.leeway.android.lib.lollipinx.managers.LockManager
import uz.leeway.android.library.sample.databinding.ActivityTestBinding

class MainActivity : AppCompatActivity(R.layout.activity_test) {

    private val viewBinding: ActivityTestBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.hello.text = "Hello world333"
        viewBinding.root.postDelayed({
            viewBinding.message = "Tes messsage"
        }, 3000)

        viewBinding.root.setOnClickListener {
            jumpToCustomPinActivity()
        }
    }

    private fun jumpToCustomPinActivity() {
        with(LockManager.getInstance(this)) {
            this.addIgnoredActivity(MainActivity::class.java)
            this.setTimeout(1000)

        }
        startActivity(Intent(this, CustomPinCodeActivity::class.java))
    }

}