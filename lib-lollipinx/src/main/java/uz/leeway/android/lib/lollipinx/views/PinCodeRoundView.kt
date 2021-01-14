package uz.leeway.android.lib.lollipinx.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import uz.leeway.android.lib.lollipinx.R
import uz.leeway.android.lib.lollipinx.managers.AppLockConst

class PinCodeRoundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var roundViews = mutableListOf<ImageView>()
    private var nCurrentLength: Int = AppLockConst.Lock.DEFAULT_PIN_LENGTH
    private var nRoundContainer: ViewGroup? = null

    private var mEmptyDotDrawable: Drawable? = null
    private var mFullDotDrawable: Drawable? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PinCodeRoundView)
        mEmptyDotDrawable = typedArray.getDrawable(R.styleable.PinCodeRoundView_lp_empty_pin_dot)
        mFullDotDrawable = typedArray.getDrawable(R.styleable.PinCodeRoundView_lp_full_pin_dot)
        typedArray.recycle()

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_round_pin_code, this)
        nRoundContainer = view.findViewById(R.id.round_container)
    }

    fun refresh(pinLength: Int) {
        nCurrentLength = pinLength
        roundViews.forEachIndexed { index, roundView ->
            val isNotEmpty = pinLength - 1 >= index
            roundView.setImageDrawable(if (isNotEmpty) mFullDotDrawable else mEmptyDotDrawable)
        }
    }

    fun getCurrentLength() = this.nCurrentLength

    fun setEmptyDotDrawable(@DrawableRes drawableResId: Int) {
        this.mEmptyDotDrawable = ContextCompat.getDrawable(context, drawableResId)
    }

    fun setEmptyDotDrawable(drawable: Drawable) {
        this.mEmptyDotDrawable = drawable
    }

    fun setFullDotDrawable(@DrawableRes drawableResId: Int) {
        this.mFullDotDrawable = ContextCompat.getDrawable(context, drawableResId)
    }

    fun setFullDotDrawable(drawable: Drawable) {
        this.mFullDotDrawable = drawable
    }

    fun setPinLength(pinLength: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        nRoundContainer?.let { container ->
            container.removeAllViews()
            val temps = mutableListOf<ImageView>()
            for (i in 1..pinLength) {
                val roundView =
                    if (i < roundViews.size) {
                        roundViews[i]
                    } else {
                        inflater.inflate(R.layout.view_round, nRoundContainer, false) as ImageView
                    }
                container.addView(roundView)
                temps.add(roundView)
            }
            roundViews.clear()
            roundViews.addAll(temps)
            refresh(0)
        }
    }
}