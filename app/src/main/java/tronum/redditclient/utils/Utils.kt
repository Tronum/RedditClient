package tronum.redditclient.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import tronum.redditclient.R

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction{replace(frameId, fragment)}
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun ImageView.setImage(url: String) {
    Glide
        .with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
        .into(this)
}

fun Any.logTag(): String {
    return this.javaClass.simpleName
}

fun Boolean?.valueOrDefault(default: Boolean): Boolean {
    return if (this == null) default else this
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Fragment.showSnackbar(activity: Activity, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    val snackBar = Snackbar.make(activity.findViewById(android.R.id.content), message, duration)
    snackBar.show()
}
