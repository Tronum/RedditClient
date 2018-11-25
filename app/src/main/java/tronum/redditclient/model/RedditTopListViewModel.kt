package tronum.redditclient.model

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import tronum.redditclient.api.RedditApiService

class RedditTopListViewModel: ViewModel() {
    private val redditApiService = RedditApiService.create()
    private val redditTopDataSourceFactory: RedditTopDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    var data: LiveData<PagedList<PostItem>>
    var state: LiveData<State>

    init {
        redditTopDataSourceFactory = RedditTopDataSourceFactory(redditApiService, compositeDisposable, maxSize)
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()
        data = LivePagedListBuilder<String, PostItem>(redditTopDataSourceFactory, config).build()
        state = Transformations.switchMap<RedditTopDataSource, State>(redditTopDataSourceFactory.topDataSourceLiveData, RedditTopDataSource::state)
    }

    fun retry() {
        redditTopDataSourceFactory.topDataSourceLiveData.value?.retry()
    }

    fun refresh() {
        redditTopDataSourceFactory.topDataSourceLiveData.value?.invalidate()
    }

    fun listIsEmpty(): Boolean {
        return data.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    companion object {
        private const val pageSize = 10
        private const val maxSize = 50
    }
}