package tronum.redditclient.contract

import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

interface IFullSizeImageView: IView {
    fun loadImage(url: String)
    fun loadGif(url: String)
    fun requestPermissions(permissions: Array<String>)
    fun showNoInternetMessage()
    fun saveImage(url: String)
}

interface IFullSizeImagePresenter: IPresenter {
    fun onCloseButtonPressed()
    fun onDownloadImagePressed()
    fun updateValues(url: String, isGif: Boolean)
}