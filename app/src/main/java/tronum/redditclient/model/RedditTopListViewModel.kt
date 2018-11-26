package tronum.redditclient.model

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import tronum.redditclient.db.RedditPostRepository

class RedditTopListViewModel(repository: RedditPostRepository): ViewModel() {
    private val triggerData = MutableLiveData<Any>()
    private val repoResult = map(triggerData) {
        repository.getPosts()
    }

    val data = switchMap(repoResult) { it.data }!!
    val networkState = switchMap(repoResult) { it.networkState }!!

    fun retry() {
        repoResult?.value?.retry?.invoke()
    }

    fun refresh() {
        data.value?.dataSource?.invalidate()
    }

    fun showData() {
        triggerData.value = ""
    }

    fun listIsEmpty(): Boolean {
        return data.value?.isEmpty() ?: true
    }
}