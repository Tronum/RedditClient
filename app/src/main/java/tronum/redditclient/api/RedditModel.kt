package tronum.redditclient.api

object RedditModel {
    data class TopResponse(val data: TopResponseData)

    data class TopResponseData(
        val children: List<PostMetadata>,
        val after: String?,
        val before: String?
    )

    data class PostMetadata(val data: Post)

    data class Post(
        val name: String,
        val subreddit_name_prefixed: String,
        val title: String,
        val id: String,
        val selftext: String,
        val selftext_html: String?,
        val author: String,
        val created: Int,
        val over_18: Boolean,
        val is_self: Boolean,
        val is_video: Boolean,
        val created_utc: Int,
        val thumbnail: String, //url or self or image or default
        val thumbnail_height: Int?,
        val thumbnail_width: Int?,
        val preview: Preview?,
        val url: String?,
        val post_hint: String,
        val num_comments: Int
    )

    data class Preview(
        val images: List<PreviewImage>,
        val reddit_video_preview: RedditVideoPreview?,
        val enabled: Boolean
    )

    data class RedditVideoPreview(val is_gif: Boolean)

    data class PreviewImage(
        val id: String,
        val source: PreviewImageSource
    )

    data class PreviewImageSource(
        val url: String,
        val width: Int,
        val height: Int
    )
}
