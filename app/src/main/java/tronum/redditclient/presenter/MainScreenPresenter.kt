package tronum.redditclient.presenter

import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.presenter.base.Presenter

class MainScreenPresenter(view: IMainScreenView) : Presenter<IMainScreenView>(view), IMainScreenPresenter {
    override fun onThumbnailClicked(url: String, isImage: Boolean) {
        if (isImage)
            view?.showFullSizeImage(url)
        else
            view?.showOpenThumbnailError()
    }
}