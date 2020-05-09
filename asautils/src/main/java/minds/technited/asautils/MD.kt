package minds.technited.asautils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog

object MD {

    @JvmStatic
    fun alert(context: Context, title: String?, message: String?) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
        }
    }


    @JvmStatic
    fun alert(context: FragmentActivity, title: String?, message: String?, button: String?, container: Int, frag: Fragment = Fragment(0)) {
        MaterialDialog(context).show {
            title(text = title)
            message(text = message)
            cancelable(false)  // calls setCancelable on the underlying dialog
            cancelOnTouchOutside(false)
            positiveButton(text = button) {

                context.supportFragmentManager
                        .beginTransaction()
                        .replace(container, frag)
                        .commit()

            }
        }
    }


}


