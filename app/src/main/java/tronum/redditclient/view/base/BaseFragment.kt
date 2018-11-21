package tronum.redditclient.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import tronum.redditclient.presenter.base.IPresenter

abstract class BaseFragment <P : IPresenter> : Fragment(), IView {
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

    /**
     * @return True if this fragment handled the back event itself. False otherwise.
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    override fun close() {
        if (isAdded) activity?.onBackPressed()
    }
}