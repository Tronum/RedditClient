package tronum.redditclient.contract

import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

interface IFullSizeImageView: IView {
    fun requestPermissions(permissions: Array<String>)
    fun showNoInternetMessage()
    fun saveImage()
    fun showFileDownloaded()
}

interface IFullSizeImagePresenter: IPresenter {
    fun onCloseButtonPressed()
    fun onDownloadImagePressed()
    fun onFileDownloadedEvent()
}