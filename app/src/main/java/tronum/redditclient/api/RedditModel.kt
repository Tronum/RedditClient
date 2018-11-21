package tronum.redditclient.api

object RedditModel {
    data class TopResponse(val kind: String,
                           val data: TopResponseData)

    data class TopResponseData(val modhash: String,
                               val dist: Int,
                               val children: List<PostMetadata>,
                               val after: String?,
                               val before: String?)

    data class PostMetadata(val kind: String,
                            val data: Post)

    data class Post(val subreddit_name_prefixed: String,
                    val title: String,
                    val selftext: String,
                    val selftext_html: String?,
                    val author: String,
                    val created: Long,
                    val over_18: Boolean,
                    val is_self: Boolean,
                    val is_video: Boolean,
                    val created_utc: Long,
                    val thumbnail: String, //url or self or image or default
                    val thumbnail_height: Int?,
                    val thumbnail_width: Int?,
                    val preview: Preview,
                    val num_comments: Int)

    data class Preview(val images: List<PreviewImage>,
                       val enabled: Boolean)

    data class PreviewImage(val id: String,
                            val source: PreviewImageSource)

    data class PreviewImageSource(val url: String,
                                  val width: Int,
                                  val height: Int)
}
