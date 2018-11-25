package tronum.redditclient.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import tronum.redditclient.api.RedditApiService

class RedditTopListViewModel: ViewModel() {
    private val redditApiService = RedditApiService.create()
    private val redditTopDataSourceFactory: RedditTopDataSourceFactory
    private val compositeDisposable = CompositeDisposable()
    var topData: LiveData<PagedList<PostItem>>

    init {
        redditTopDataSourceFactory = RedditTopDataSourceFactory(redditApiService, compositeDisposable)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        topData = LivePagedListBuilder<String, PostItem>(redditTopDataSourceFactory, config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<RedditTopDataSource,
            State>(redditTopDataSourceFactory.topDataSourceLiveData, RedditTopDataSource::state)

    fun retry() {
        redditTopDataSourceFactory.topDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return topData.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val pageSize = 10
    }
}