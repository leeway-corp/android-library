package uz.leeway.android.lib.vdbpd

import androidx.databinding.ViewDataBinding


/**
 * Method that will be used to create [ViewDataBinding].
 */
enum class CreateMethod {
    /**
     * Use `ViewDataBinding.bind(View)`
     */
    BIND,

    /**
     * Use `ViewDataBinding.inflate(LayoutInflater, ViewGroup, boolean)`
     */
    INFLATE
}
