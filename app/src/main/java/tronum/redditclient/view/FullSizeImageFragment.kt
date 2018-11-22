package tronum.redditclient.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_full_size_image.*
import tronum.redditclient.R

class FullSizeImageFragment: DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.ModalDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full_size_image, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            showImage(it.getString(KEY_URL))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
                dismissAllowingStateLoss()
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d(TAG, "Can't show DialogFragment", e)
        }

    }

    private fun showImage(url: String?) {
        activity?.let {
            Glide
                .with(it)
                .load(url)
                .apply(
                    RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(imageView)
        }
    }

    companion object {
        private const val KEY_URL = "key_url"
        val TAG = FullSizeImageFragment.javaClass.simpleName

        @JvmStatic
        fun newInstance(url: String): FullSizeImageFragment {
            var fragment = FullSizeImageFragment()
            var bound = Bundle()
            bound.putString(KEY_URL, url)
            fragment.arguments = bound
            return fragment
        }
    }
}