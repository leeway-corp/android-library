package uz.leeway.android.lib.lollipinx.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class SquareImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight > measuredWidth) {
            this.setMeasuredDimension(measuredWidth, measuredWidth)
        } else {
            this.setMeasuredDimension(measuredHeight, measuredHeight)
        }
    }
}