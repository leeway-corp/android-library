package uz.leeway.android.lib.lollipinx.views

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class KeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var listener: ButtonListener? = null
    private val buttons = mutableListOf<KeyboardButtonView>()

    interface ButtonListener {
        fun onKeyboardClick(button: ButtonEnum)
    }

    enum class ButtonEnum(val value: Int) {
        BUTTON_0(0),
        BUTTON_1(1),
        BUTTON_2(2),
        BUTTON_3(3),
        BUTTON_4(4),
        BUTTON_5(5),
        BUTTON_6(6),
        BUTTON_7(7),
        BUTTON_8(8),
        BUTTON_9(9),
        BUTTON_CLEAR(-1);
    }
}