package tronum.redditclient.presenter

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tronum.redditclient.api.RedditApiService
import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.data.PostItem
import tronum.redditclient.presenter.base.Presenter


class MainScreenPresenter(view: IMainScreenView) : Presenter<IMainScreenView>(view), IMainScreenPresenter {
    private val redditApiService by lazy {
        RedditApiService.create()
    }

    override fun onStart() {
        super.onStart()
        beginTopFetching()
    }

    private fun beginTopFetching() {
        redditApiService.getTop()
            .map { it.data.children }
            .map { it.map { item -> PostItem(item.data.title) }.toMutableList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ showPosts(it) }, { showError(it.message) })
    }

    private fun showPosts(posts: List<PostItem>) {
        view?.showPosts(posts)
    }

    private fun showError(message: String?) {
        message?.let {
            view?.showError(it)
        }
    }
}