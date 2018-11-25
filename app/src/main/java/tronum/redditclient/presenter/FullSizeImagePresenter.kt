package tronum.redditclient.presenter

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import tronum.redditclient.App
import tronum.redditclient.contract.IFullSizeImagePresenter
import tronum.redditclient.contract.IFullSizeImageView
import tronum.redditclient.presenter.base.Presenter
import tronum.redditclient.utils.isInternetAvailable

class FullSizeImagePresenter(view: IFullSizeImageView) : Presenter<IFullSizeImageView>(view), IFullSizeImagePresenter {
    override fun onFileDownloadedEvent() {
        view?.showFileDownloaded()
    }

    override fun onCloseButtonPressed() {
        view?.close()
    }

    override fun onDownloadImagePressed() {
        if (!isWritePermissionGranted()) {
            view?.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        } else if (!App.instance.isInternetAvailable()) {
            view?.showNoInternetMessage()
        } else {
            view?.saveImage()
        }
    }

    private fun isWritePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}