package tronum.redditclient.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import tronum.redditclient.R
import tronum.redditclient.adapter.RedditTopListAdapter
import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.fragment.base.BaseFragment
import tronum.redditclient.model.PostItem
import tronum.redditclient.model.RedditTopListViewModel
import tronum.redditclient.model.State
import tronum.redditclient.presenter.MainScreenPresenter
import tronum.redditclient.utils.logTag
import tronum.redditclient.utils.showSnackbar

class MainScreenFragment : BaseFragment<IMainScreenPresenter>(), IMainScreenView {
    private lateinit var adapter: RedditTopListAdapter
    private lateinit var viewModel: RedditTopListViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var osRefreshing = false

    private val dataObserver = Observer<PagedList<PostItem>> { adapter.submitList(it) }
    private val stateObserver = Observer<State> { onContentStateChanged(it) }

    override var presenter: IMainScreenPresenter = MainScreenPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initAdapter()
        initListView()
        initRefreshLayout()
    }

    private fun initRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            osRefreshing = true
            viewModel.refresh()
        }
    }

    private fun initListView() {
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = RedditTopListAdapter { viewModel.retry() }
        adapter.onThumbnailClickListener = object : RedditTopListAdapter.OnThumbnailClickListener {
            override fun onThumbnailClicked(url: String, isImage: Boolean) {
                presenter?.onThumbnailClicked(url, isImage)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(RedditTopListViewModel::class.java)
        viewModel.data.observe(this, dataObserver)
        viewModel.state.observe(this, stateObserver)
    }

    private fun onContentStateChanged(state: State) {
        Log.v(logTag(), "state ${state.name}")

        swipeRefreshLayout.isRefreshing = viewModel.listIsEmpty() && state == State.LOADING
        if (!viewModel.listIsEmpty()) adapter.setState(state)

        when (state) {
            State.LOADING -> {}
            State.CONTENT -> showContent()
            State.EMPTY_DATA -> showEmptyList()
            State.ERROR -> {
                if (viewModel.listIsEmpty() || osRefreshing) showEmptyList()
                osRefreshing = false
            }
        }
    }

    private fun showEmptyList() {
        osRefreshing = false
        recyclerView.isVisible = false
        empty_view.isVisible = true
    }

    private fun showContent() {
        osRefreshing = false
        recyclerView.isVisible = true
        empty_view.isVisible = false
    }

    override fun showOpenThumbnailError() {
        activity?.let {
            showSnackbar(it, "No data to open full size")
        }
    }

    override fun showFullSizeImage(url: String) {
        FullSizeImageFragment.newInstance(url).show(childFragmentManager, FullSizeImageFragment.TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }
    }
}