package tronum.redditclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post_item.view.*
import tronum.redditclient.R
import tronum.redditclient.data.PostItem

class PostAdapter: RecyclerView.Adapter<PostAdapter.ViewHolder>() {
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
            itemView.title.text = item.title
        }
    }
}
