package tronum.redditclient.fragment.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import tronum.redditclient.fragment.FullSizeImageFragment
import tronum.redditclient.presenter.base.IPresenter
import tronum.redditclient.view.base.IView

abstract class BaseDialogFragment<P : IPresenter> : DialogFragment(), IView {
    protected abstract var presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
    }
    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }
    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }
    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.d(FullSizeImageFragment.TAG, "Can't show DialogFragment", e)
        }

    }

    override fun close() {
        dismissAllowingStateLoss()
    }
}