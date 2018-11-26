package tronum.redditclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_footer.view.*
import kotlinx.android.synthetic.main.item_list_top.view.*
import tronum.redditclient.R
import tronum.redditclient.data.PostData
import tronum.redditclient.model.State
import tronum.redditclient.utils.setImage
import java.util.*
import java.util.concurrent.TimeUnit

class RedditTopListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<PostData, RecyclerView.ViewHolder>(diffCallback) {
    var onItemClickListener: OnItemClickListener? = null
    private var state = State.RUNNING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE)
            ItemViewHolder.create(parent, onItemClickListener)
        else FooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE)
            (holder as ItemViewHolder).bindItems(getItem(position))
        else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            FOOTER_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
    }

    private fun hasExtraRow(): Boolean {
        return state == State.RUNNING || state == State.FAILED
    }

    fun setState(state: State) {
        val previousState = this.state
        val previousExtraRow = hasExtraRow()
        this.state = state
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== state) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private const val ITEM_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2

        val diffCallback = object : DiffUtil.ItemCallback<PostData>() {
            override fun areItemsTheSame(oldItem: PostData, newItem: PostData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostData, newItem: PostData): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: PostData)
    }

    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(status: State?) {
            itemView.progressBar.visibility = if (status == State.RUNNING) VISIBLE else INVISIBLE
            itemView.errorMsg.visibility = if (status == State.FAILED) VISIBLE else INVISIBLE
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_footer, parent, false)
                view.errorMsg.setOnClickListener { retry() }
                return FooterViewHolder(view)
            }
        }
    }

    class ItemViewHolder(
        itemView: View,
        private val listener: OnItemClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: PostData?) {
            item?.let {
                val context = itemView.context
                itemView.selftext.text = item.selftext
                itemView.postedInfo.text = context.getString(
                    R.string.posted_by,
                    item.author,
                    formatRelativeTime(item.createdTime)
                )
                itemView.title.text = item.title
                itemView.comments.text = context.getString(R.string.comments, item.commentsCount)
                if (hasThumbnail(item.thumbnail)) {
                    itemView.thumbnail.isVisible = true
                    itemView.thumbnail.setOnClickListener { item.fullImageUrl?.let { listener?.onItemClicked(item) } }
                    itemView.thumbnail.setImage(item.thumbnail)
                } else {
                    itemView.thumbnail.isVisible = false
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, onThumbnailClickListener: OnItemClickListener?): ItemViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_top, parent, false)
                return ItemViewHolder(view, onThumbnailClickListener)
            }
        }

        private fun hasThumbnail(thumbnail: String): Boolean {
            return thumbnail !in arrayOf("self", "image", "default")
        }

        private fun formatRelativeTime(time: Long): String {
            var past = Date(time)
            val now = Date()

            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

            return when {
                seconds < 60 -> "$seconds seconds ago"
                minutes < 60 -> "$minutes minutes ago"
                hours < 24 -> "$hours hours ago"
                else -> "$days days ago"
            }
        }
    }
}
