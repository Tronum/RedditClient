package tronum.redditclient.presenter

import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.presenter.base.Presenter

class MainScreenPresenter(view: IMainScreenView) : Presenter<IMainScreenView>(view), IMainScreenPresenter {
    override fun onItemClicked(url: String?, isImage: Boolean, isGif: Boolean) {
        if (isImage)
            url?.let { view?.showFullSizeImage(url, isGif) }
        else
            view?.showOpenThumbnailError()
    }
}