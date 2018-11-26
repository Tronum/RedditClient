package tronum.redditclient.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import tronum.redditclient.model.State

fun Any.logTag(): String {
    return this.javaClass.simpleName
}

fun Boolean?.valueOrDefault(default: Boolean): Boolean {
    return this ?: default
}

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<State> {
    val liveData = MutableLiveData<State>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(State.RUNNING)
            report.hasError() -> liveData.postValue(State.FAILED)
            else -> liveData.postValue(State.SUCCESS)
        }
    }
    return liveData
}
