package tronum.redditclient.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import tronum.redditclient.data.PostData
import tronum.redditclient.model.RedditTopListViewModel
import tronum.redditclient.model.State
import tronum.redditclient.presenter.MainScreenPresenter
import tronum.redditclient.utils.ToolsCreator
import tronum.redditclient.utils.logTag
import tronum.redditclient.utils.showSnackbar

class MainScreenFragment : BaseFragment<IMainScreenPresenter>(), IMainScreenView {
    private lateinit var adapter: RedditTopListAdapter
    private lateinit var viewModel: RedditTopListViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var isRefreshing = false

    private val dataObserver = Observer<PagedList<PostData>> { adapter.submitList(it) }
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
        initSwipeToRefresh()
        if (savedInstanceState == null){
            viewModel.showData()
        }
    }

    private fun initSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            isRefreshing = true
            viewModel.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initListView() {
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    private fun initAdapter() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        adapter = RedditTopListAdapter { viewModel.retry() }
        adapter.onItemClickListener = object : RedditTopListAdapter.OnItemClickListener {
            override fun onItemClicked(item: PostData) {
                presenter.onItemClicked(item.fullImageUrl, item.isImage, item.isGif)
            }
        }
    }

    private fun initViewModel() {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RedditTopListViewModel(ToolsCreator.getRepository()) as T
            }
        }

        viewModel = ViewModelProviders.of(this, factory).get(RedditTopListViewModel::class.java)
        viewModel.data.observe(this, dataObserver)
        viewModel.networkState.observe(this, stateObserver)
    }

    private fun onContentStateChanged(state: State) {
        Log.v(logTag(), "networkState ${state.name}")

        swipeRefreshLayout.isRefreshing = viewModel.listIsEmpty() && state == State.RUNNING
        if (!viewModel.listIsEmpty()) adapter.setState(state)

        when (state) {
            State.RUNNING -> {}
            State.SUCCESS -> {
                showContent()
                isRefreshing = false
            }
            State.EMPTY -> {
                showEmptyList()
                isRefreshing = false
            }
            State.FAILED -> {
                if (viewModel.listIsEmpty() || isRefreshing) showEmptyList()
                isRefreshing = false
            }
        }
    }

    private fun showEmptyList() {
        recyclerView.isVisible = false
        empty_view.isVisible = true
    }

    private fun showContent() {
        recyclerView.isVisible = true
        empty_view.isVisible = false
    }

    override fun showOpenThumbnailError() {
        activity?.let {
            showSnackbar(it, "No full size picture.")
        }
    }

    override fun showFullSizeImage(url: String, isGif: Boolean) {
        FullSizeImageFragment.newInstance(url, isGif).show(childFragmentManager, FullSizeImageFragment.TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }
    }
}