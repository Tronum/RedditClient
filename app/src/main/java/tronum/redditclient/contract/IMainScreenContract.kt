package tronum.redditclient.contract

import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

interface IMainScreenView: IView {
    fun showFullSizeImage(url: String, isGif: Boolean)
    fun showOpenThumbnailError()
}

interface IMainScreenPresenter: IPresenter {
    fun onItemClicked(url: String?, isImage: Boolean, isGif: Boolean)
}