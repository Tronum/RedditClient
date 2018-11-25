package tronum.redditclient.model

import tronum.redditclient.api.RedditModel
import tronum.redditclient.utils.valueOrDefault

data class PostItem(val title: String,
                    val id: String,
                    val author: String,
                    val selftext: String,
                    val createdTime: Long,
                    val commentsCount: Int,
                    val thumbnail: String,
                    val fullImageUrl: String?,
                    val isImage: Boolean,
                    val isGif: Boolean) {
    companion object {
        fun parse(metadata: RedditModel.PostMetadata): PostItem {
            return PostItem(
                metadata.data.title.trim(),
                metadata.data.id,
                metadata.data.author.trim(),
                metadata.data.subreddit_name_prefixed.trim(),
                metadata.data.created_utc * 1000L,
                metadata.data.num_comments,
                metadata.data.thumbnail,
                metadata.data.url,
                metadata.data.post_hint == "image",
                metadata.data.preview?.reddit_video_preview?.is_gif.valueOrDefault(false))
        }
    }
}