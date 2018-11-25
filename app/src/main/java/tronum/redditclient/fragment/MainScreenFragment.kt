package tronum.redditclient.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import tronum.redditclient.R
import tronum.redditclient.adapter.RedditTopListAdapter
import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.fragment.base.BaseFragment
import tronum.redditclient.model.RedditTopListViewModel
import tronum.redditclient.model.State
import tronum.redditclient.presenter.MainScreenPresenter
import tronum.redditclient.utils.logTag
import tronum.redditclient.utils.showSnackbar
import tronum.redditclient.utils.showToast

class MainScreenFragment: BaseFragment<IMainScreenPresenter>(), IMainScreenView {
    private lateinit var adapter: RedditTopListAdapter
    private lateinit var viewModel: RedditTopListViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    override var presenter: IMainScreenPresenter = MainScreenPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RedditTopListViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = RedditTopListAdapter{ viewModel.retry() }
        adapter.onThumbnailClickListener = object : RedditTopListAdapter.OnThumbnailClickListener {
            override fun onThumbnailClicked(url: String, isImage: Boolean) {
                presenter?.onThumbnailClicked(url, isImage)
            }
        }
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
        viewModel.topData.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.getState().observe(this, Observer {
            Log.v(logTag(), "state ${it.name}")
            swipeRefreshLayout.isRefreshing = viewModel.listIsEmpty() && it == State.LOADING
            recyclerView.isVisible = !viewModel.listIsEmpty()
            empty_view.isVisible = viewModel.listIsEmpty() && it != State.LOADING
            if (!viewModel.listIsEmpty()) {
                adapter.setState(it ?: State.DONE)
            }
            if (it == State.ERROR) {
                showToast("Error. Can't load data")
            }
        })
    }

    override fun showOpenThumbnailError() {
        activity?.let { notNullActivity ->
            showSnackbar(notNullActivity, "No data to open full size")
        }
    }

    override fun showFullSizeImage(url: String) {
        FullSizeImageFragment.newInstance(url).show(childFragmentManager, FullSizeImageFragment.TAG)
    }

    companion object {
        private const val maxListSize = 50
        @JvmStatic
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }
    }
}