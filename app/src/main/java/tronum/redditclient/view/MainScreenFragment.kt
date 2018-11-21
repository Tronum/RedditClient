package tronum.redditclient.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.*
import tronum.redditclient.R
import tronum.redditclient.adapter.PostAdapter
import tronum.redditclient.contract.IMainScreenPresenter
import tronum.redditclient.contract.IMainScreenView
import tronum.redditclient.data.PostItem
import tronum.redditclient.presenter.MainScreenPresenter
import tronum.redditclient.view.base.BaseFragment

class MainScreenFragment: BaseFragment<IMainScreenPresenter>(), IMainScreenView {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: PostAdapter

    override var presenter: IMainScreenPresenter = MainScreenPresenter(this)

    companion object {
        @JvmStatic
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context)
        adapter = PostAdapter()
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    override fun showPosts(posts: List<PostItem>) {
        adapter.items = posts
    }

    override fun showError(message: String) {
        showToast(message)
    }

    private fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}