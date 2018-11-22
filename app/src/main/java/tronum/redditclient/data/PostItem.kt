package tronum.redditclient.data

import tronum.redditclient.api.RedditModel

data class PostItem(val title: String,
                    val author: String,
                    val selftext: String,
                    val createdTime: Long,
                    val commentsCount: Int,
                    val thumbnail: String,
                    val fullImage: String?,
                    val isNsfw: Boolean) {
    companion object {
        fun parse(metadata: RedditModel.PostMetadata): PostItem {
            return PostItem(
                metadata.data.title.trim(),
                metadata.data.author.trim(),
                metadata.data.subreddit_name_prefixed.trim(),
                metadata.data.created_utc * 1000L,
                metadata.data.num_comments,
                metadata.data.thumbnail,
                metadata.data.url,
                metadata.data.over_18)
        }
    }
}