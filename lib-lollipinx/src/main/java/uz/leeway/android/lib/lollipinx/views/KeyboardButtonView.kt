package uz.leeway.android.lib.lollipinx.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import uz.leeway.android.lib.lollipinx.R

class KeyboardButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardButtonView)
        val text = typedArray.getString(R.styleable.KeyboardButtonView_lp_keyboard_button_text)
        val image = typedArray.getDrawable(R.styleable.KeyboardButtonView_lp_keyboard_button_image)
        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_keyboard_button, this)

        view.findViewById<TextView>(R.id.keyboard_button_textview)?.text = text
        if (image != null) {
            view.findViewById<ImageView>(R.id.keyboard_button_imageview)?.setImageDrawable(image)
            view.visibility = VISIBLE
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onTouchEvent(ev)
        return false
    }
}