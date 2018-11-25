package tronum.redditclient.contract

import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

interface IMainScreenView: IView {
    fun showFullSizeImage(url: String)
    fun showOpenThumbnailError()
}

interface IMainScreenPresenter: IPresenter {
    fun onThumbnailClicked(url: String, isImage: Boolean)
}