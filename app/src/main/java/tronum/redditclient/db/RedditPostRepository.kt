package tronum.redditclient.db

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tronum.redditclient.api.RedditApiService
import tronum.redditclient.api.RedditModel
import tronum.redditclient.data.Listing
import tronum.redditclient.data.PostData
import tronum.redditclient.model.RedditTopBoundaryCallback
import tronum.redditclient.model.State
import java.util.concurrent.Executor

class RedditPostRepository(
    val db: RedditTopDatabase,
    private val apiService: RedditApiService,
    private val ioExecutor: Executor,
    private val networkPageSize: Int = DEFAULT_NETWORK_PAGE_SIZE
) {

    companion object {
        private const val DEFAULT_NETWORK_PAGE_SIZE = 10
    }

    @MainThread
    fun getPosts(): Listing {
        val boundaryCallback = RedditTopBoundaryCallback(
            apiService,
            this::insertResultIntoDb,
            ioExecutor,
            networkPageSize
        )

        val config = PagedList.Config.Builder()
            .setPageSize(DEFAULT_NETWORK_PAGE_SIZE)
            .setInitialLoadSizeHint(DEFAULT_NETWORK_PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = Transformations.switchMap(refreshTrigger) {
            refresh()
        }

        val livePagedList = LivePagedListBuilder<Int, PostData>(db.topPostDao().getPosts(), config)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listing(
            livePagedList,
            boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    private fun insertResultIntoDb(body: RedditModel.TopResponse?) {
        body!!.data.children.let { posts ->
            db.runInTransaction {
                val items = posts.map { PostData.parse(it) }
                db.topPostDao().insertPosts(items)
            }
        }
    }

    @MainThread
    private fun refresh(): LiveData<State> {
        val networkState = MutableLiveData<State>()
        networkState.value = State.RUNNING
        apiService.getTop(networkPageSize).enqueue(
            object : Callback<RedditModel.TopResponse> {
                override fun onFailure(call: Call<RedditModel.TopResponse>, t: Throwable) {
                    networkState.value = State.FAILED
                }

                override fun onResponse(
                    call: Call<RedditModel.TopResponse>,
                    response: Response<RedditModel.TopResponse>
                ) {
                    ioExecutor.execute {
                        db.runInTransaction {
                            insertResultIntoDb(response.body())
                        }
                        networkState.postValue(State.SUCCESS)
                    }
                }
            }
        )
        return networkState
    }
}