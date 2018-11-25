package tronum.redditclient.utils

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Fragment.showSnackbar(activity: Activity, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(activity.findViewById(android.R.id.content), message, duration).show()
}

fun DialogFragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}