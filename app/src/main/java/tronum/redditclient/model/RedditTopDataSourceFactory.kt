package tronum.redditclient.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import tronum.redditclient.api.RedditApiService

class RedditTopDataSourceFactory(
    private val networkService: RedditApiService,
    private val compositeDisposable: CompositeDisposable,
    private val maxSize: Int
) : DataSource.Factory<String, PostItem>() {

    val topDataSourceLiveData = MutableLiveData<RedditTopDataSource>()

    override fun create(): DataSource<String, PostItem> {
        val newsDataSource = RedditTopDataSource(networkService, compositeDisposable, maxSize)
        topDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}