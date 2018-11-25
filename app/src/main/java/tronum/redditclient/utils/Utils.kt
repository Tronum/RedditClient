package tronum.redditclient.utils

fun Any.logTag(): String {
    return this.javaClass.simpleName
}

fun Boolean?.valueOrDefault(default: Boolean): Boolean {
    return this ?: default
}
