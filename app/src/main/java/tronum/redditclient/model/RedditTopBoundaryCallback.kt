package tronum.redditclient.model

import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import tronum.redditclient.api.RedditApiService
import tronum.redditclient.data.PostData
import tronum.redditclient.utils.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tronum.redditclient.api.RedditModel
import java.util.concurrent.Executor

class RedditTopBoundaryCallback(
    private val apiService: RedditApiService,
    private val handleResponse: (RedditModel.TopResponse?) -> Unit,
    private val ioExecutor: Executor,
    private val networkPageSize: Int
) : PagedList.BoundaryCallback<PostData>() {
    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    @MainThread
    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            apiService.getTop(networkPageSize).enqueue(createWebserviceCallback(it))
        }
    }

    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: PostData) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            apiService.getTop(networkPageSize, itemAtEnd.name).enqueue(createWebserviceCallback(it))
        }
    }

    private fun insertItemsIntoDb(
        response: Response<RedditModel.TopResponse>,
        it: PagingRequestHelper.Request.Callback
    ) {
        ioExecutor.execute {
            handleResponse(response.body())
            it.recordSuccess()
        }
    }

    override fun onItemAtFrontLoaded(itemAtFront: PostData) {}

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback): Callback<RedditModel.TopResponse> {
        return object : Callback<RedditModel.TopResponse> {
            override fun onFailure(
                call: Call<RedditModel.TopResponse>,
                t: Throwable
            ) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<RedditModel.TopResponse>,
                response: Response<RedditModel.TopResponse>
            ) {
                insertItemsIntoDb(response, it)
            }
        }
    }
}