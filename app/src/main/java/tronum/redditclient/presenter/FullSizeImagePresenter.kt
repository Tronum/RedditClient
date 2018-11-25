package tronum.redditclient.presenter

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import tronum.redditclient.App
import tronum.redditclient.contract.IFullSizeImagePresenter
import tronum.redditclient.contract.IFullSizeImageView
import tronum.redditclient.presenter.base.Presenter
import tronum.redditclient.utils.isInternetAvailable

class FullSizeImagePresenter(view: IFullSizeImageView) : Presenter<IFullSizeImageView>(view), IFullSizeImagePresenter {
    private var url: String? = null
    private var isGif = false

    override fun updateValues(url: String, isGif: Boolean) {
        this.url = url
        this.isGif = isGif
        if (!isGif) {
            view?.loadImage(url)
        } else {
            view?.loadGif(url)
        }
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
            url?.let { view?.saveImage(it)  }
        }
    }

    private fun isWritePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}