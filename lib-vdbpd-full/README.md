# ViewDataBindingPropertyDelegate

Make work with [Android View Binding](https://developer.android.com/topic/libraries/view-binding) simpler

## IMPORTANT: Enable ViewDataBinding before use the library
Every Gradle module of your project need to enable ViewDataBinding. How to do that you can find in the [official guide](https://d.android.com/topic/libraries/view-binding)

## Add library to a project

```groovy
allprojects {
  repositories {
    jcenter()

    // or for the ASAP availability
    maven {url 'https://jitpack.io'}
  }
}

dependencies {
    implementation 'com.github.leeway-corp.android-library:lib-vdbpd-full:$latest_version'
    
    // To use only without reflection variants of viewDataBinding
    implementation 'com.github.leeway-corp.android-library:lib-vdbpd-noreflection:$latest_version'
}
```

## Samples

```kotlin
class ProfileFragment : Fragment(R.layout.profile) {

    // Using reflection API under the hood and ViewDataBinding.bind
    private val viewDataBinding: ProfileBinding by viewDataBinding()

    // Using reflection API under the hood and ViewDataBinding.inflate
    private val viewDataBinding: ProfileBinding by viewDataBinding(createMethod = CreateMethod.INFLATE)

    // Without reflection
    private val viewDataBinding by viewDataBinding(ProfileBinding::bind)
}
```

```kotlin
class ProfileActivity : AppCompatActivity(R.layout.profile) {

    // Using reflection API under the hood
    private val viewDataBinding: ProfileBinding by viewDataBinding(R.id.container)

    // Without reflection
    private val viewDataBinding by viewDataBinding(ProfileBinding::bind, R.id.container)
}
```

It's very important that for some cases in `DialogFragment` you need to use `dialogViewDataBinding`
instead of `viewDataBinding`

```kotlin
class ProfileDialogFragment : DialogFragment() {

    // Using reflection API under the hood
    private val viewDataBinding: ProfileBinding by dialogViewDataBinding(R.id.container)

    // Without reflection
    private val viewDataBinding by dialogViewDataBinding(ProfileBinding::bind, R.id.container)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.profile)
            .create()
    }
}
```

```kotlin
class ProfileDialogFragment : DialogFragment() {

    // Using reflection API under the hood
    private val viewDataBinding: ProfileBinding by viewDataBinding()

    // Without reflection
    private val viewDataBinding by viewDataBinding(ProfileBinding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile, container, false)
    }
}
```
