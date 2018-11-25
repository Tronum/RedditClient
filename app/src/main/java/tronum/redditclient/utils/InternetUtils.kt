package tronum.redditclient.utils

import android.content.Context
import android.net.ConnectivityManager

fun Context.isInternetAvailable(): Boolean {
    val connection = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connection.activeNetworkInfo
    return networkInfo?.isConnected ?: false
}
