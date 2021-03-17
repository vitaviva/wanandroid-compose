import android.app.Activity
import android.app.Application
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentHostCallback
import com.wanandroid.compose.ComposeFragment

fun mockFragment(): ComposeFragment {

    val activity = FragmentActivity()
    Activity::class.java.getDeclaredField("mApplication").apply {
        isAccessible = true
    }.set(activity, Application())

    val frag = ComposeFragment {}
    Fragment::class.java.getDeclaredField("mHost").apply {
        isAccessible = true
    }.set(frag, object :
        FragmentHostCallback<FragmentActivity>(activity, Handler(), 0) {
        override fun onGetHost(): FragmentActivity {
            return activity
        }

    })

    return frag
}