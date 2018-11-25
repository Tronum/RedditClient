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
import tronum.redditclient.model.PostItem
import tronum.redditclient.model.State
import tronum.redditclient.utils.setImage
import java.util.*
import java.util.concurrent.TimeUnit

class RedditTopListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<PostItem, RecyclerView.ViewHolder>(diffCallback) {
    var onThumbnailClickListener: OnThumbnailClickListener? = null
    private var state = State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE)
            ItemViewHolder.create(parent, onThumbnailClickListener)
        else FooterViewHolder.create(retry, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE)
            (holder as ItemViewHolder).bindItems(getItem(position))
        else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) ITEM_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    companion object {
        private val ITEM_VIEW_TYPE = 1
        private val FOOTER_VIEW_TYPE = 2

        val diffCallback = object : DiffUtil.ItemCallback<PostItem>() {
            override fun areItemsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostItem, newItem: PostItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnThumbnailClickListener {
        fun onThumbnailClicked(url: String, isImage: Boolean)
    }


    class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(status: State?) {
            itemView.progressBar.visibility = if (status == State.LOADING) VISIBLE else INVISIBLE
            itemView.errorMsg.visibility = if (status == State.ERROR) VISIBLE else INVISIBLE
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
        private val onThumbnailClickListener: OnThumbnailClickListener?
    ) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: PostItem?) {
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
                    itemView.thumbnail.setOnClickListener {
                        item.fullImage?.let { url ->
                            val isImage = !item.isVideo && !item.isSelf && !item.isGif
                            onThumbnailClickListener?.onThumbnailClicked(url, isImage)
                        }
                    }
                    itemView.thumbnail.setImage(item.thumbnail)
                } else {
                    itemView.thumbnail.isVisible = false
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup, onThumbnailClickListener: OnThumbnailClickListener?): ItemViewHolder {
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