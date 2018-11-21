package tronum.redditclient.contract

import tronum.redditclient.data.PostItem
import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

interface IMainScreenView: IView {
    fun showError(message: String)
    fun showPosts(posts: List<PostItem>)
}

interface IMainScreenPresenter: IPresenter