package tronum.redditclient.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tronum.redditclient.api.RedditModel
import tronum.redditclient.utils.valueOrDefault

@Entity
data class PostData(
    @PrimaryKey
    val name: String,
    val title: String,
    val id: String,
    val author: String,
    val selftext: String,
    @ColumnInfo(name = "created_time")
    val createdTime: Long,
    @ColumnInfo(name = "comments_count")
    val commentsCount: Int,
    val thumbnail: String,
    @ColumnInfo(name = "full_image_url")
    val fullImageUrl: String?,
    @ColumnInfo(name = "is_image")
    val isImage: Boolean,
    @ColumnInfo(name = "is_gif")
    val isGif: Boolean
) {

    companion object {
        fun parse(metadata: RedditModel.PostMetadata): PostData {
            return PostData(
                metadata.data.name,
                metadata.data.title.trim(),
                metadata.data.id,
                metadata.data.author.trim(),
                metadata.data.subreddit_name_prefixed.trim(),
                metadata.data.created_utc * 1000L,
                metadata.data.num_comments,
                metadata.data.thumbnail,
                metadata.data.url,
                metadata.data.post_hint == "image",
                metadata.data.preview?.reddit_video_preview?.is_gif.valueOrDefault(false)
            )
        }
    }
}