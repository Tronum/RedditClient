package tronum.redditclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.post_item.view.*
import tronum.redditclient.R
import tronum.redditclient.data.PostItem
import java.util.*
import java.util.concurrent.TimeUnit


class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    var onThumbnailClickListener: OnThumbnailClickListener? = null

    var items: List<PostItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addItems(list: List<PostItem>) {
        items += list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: PostItem) {
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
                        onThumbnailClickListener?.onThumbnailClicked(url)
                    }
                }
                Glide
                    .with(context)
                    .load(item.thumbnail)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    )
                    .into(itemView.thumbnail)
            } else {
                itemView.thumbnail.isVisible = false
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

    interface OnThumbnailClickListener {
        fun onThumbnailClicked(url: String)
    }
}
