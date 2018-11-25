package tronum.redditclient.fragment

import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_full_size_image.*
import tronum.redditclient.R
import android.net.Uri
import android.os.Environment
import com.google.android.material.snackbar.Snackbar
import tronum.redditclient.contract.IFullSizeImagePresenter
import tronum.redditclient.contract.IFullSizeImageView
import tronum.redditclient.fragment.base.BaseDialogFragment
import tronum.redditclient.presenter.FullSizeImagePresenter

class FullSizeImageFragment: BaseDialogFragment<IFullSizeImagePresenter>(), IFullSizeImageView {
    override var presenter: IFullSizeImagePresenter = FullSizeImagePresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ModalDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full_size_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { presenter.updateValues(it.getString(KEY_URL)!!, it.getBoolean(KEY_IS_GIF)) }
        closeBtn.setOnClickListener { presenter.onCloseButtonPressed() }
        downloadBtn.setOnClickListener { presenter.onDownloadImagePressed() }
    }

    override fun requestPermissions(permissions: Array<String>) {
        requestPermissions(permissions, REQUEST_PERMISSION_CODE)
    }

    override fun showNoInternetMessage() {
        Snackbar.make(container, "Internet unavailable", Snackbar.LENGTH_SHORT).show()
    }

    override fun saveImage(url: String) {
        activity?.let {context ->
            arguments?.let {

                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = Uri.parse(url)
                val request = DownloadManager.Request(uri)
                val fileName = generateFileName()

                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                downloadManager.enqueue(request)
            }
        }
    }

    override fun loadGif(url: String) {
        activity?.let {
            Glide
                .with(it)
                .asGif()
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_full_size)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(imageView)
        }
    }

    private fun generateFileName(): String {
        val name = java.util.UUID.randomUUID().toString()
        return "Photo-$name.png"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                close()
            }
        }
    }

    override fun loadImage(url: String) {
        activity?.let {
            Glide
                .with(it)
                .load(url)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_full_size)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(imageView)
        }
    }

    companion object {
        val TAG = FullSizeImageFragment::class.java.simpleName
        private const val KEY_URL = "key_url"
        private const val KEY_IS_GIF = "key_is_gif"
        private const val REQUEST_PERMISSION_CODE = 101

        @JvmStatic
        fun newInstance(url: String, isGif: Boolean): FullSizeImageFragment {
            var fragment = FullSizeImageFragment()
            var bound = Bundle()
            bound.putString(KEY_URL, url)
            bound.putBoolean(KEY_IS_GIF, isGif)
            fragment.arguments = bound
            return fragment
        }
    }
}