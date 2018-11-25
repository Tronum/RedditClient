package tronum.redditclient.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import tronum.redditclient.api.RedditApiService
import tronum.redditclient.utils.logTag

class RedditTopDataSource(
    private val service: RedditApiService,
    private val compositeDisposable: CompositeDisposable,
    private val maxSize: Int
) : PageKeyedDataSource<String, PostItem>() {
    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null
    private var currentSize = 0

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, PostItem>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            service.getTop(params.requestedLoadSize, "")
                .subscribe(
                    { response ->
                        if (response.data.children.isEmpty()) {
                            updateState(State.EMPTY_DATA)
                        } else {
                            updateState(State.CONTENT)
                            currentSize += response.data.children.size
                        }
                        callback.onResult(
                            response.data.children.map { PostItem.parse(it) },
                            null,
                            response.data.after
                        )
                    },
                    {
                        Log.e(logTag(), "Can't get data", it)
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, PostItem>) {
        if (currentSize < maxSize) {
            updateState(State.LOADING)
            compositeDisposable.add(
                service.getTop(params.requestedLoadSize, params.key)
                    .subscribe(
                        { response ->
                            updateState(State.CONTENT)
                            currentSize += response.data.children.size
                            callback.onResult(
                                response.data.children.map { PostItem.parse(it) },
                                response.data.after
                            )
                        },
                        {
                            Log.e(logTag(), "Can't get data", it)
                            updateState(State.ERROR)
                            setRetry(Action { loadAfter(params, callback) })
                        }
                    )
            )
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, PostItem>) {
        //not needed
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}