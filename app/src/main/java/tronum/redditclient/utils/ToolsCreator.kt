package tronum.redditclient.utils

import tronum.redditclient.api.RedditApiService
import tronum.redditclient.db.RedditPostRepository
import tronum.redditclient.db.RedditTopDatabase
import java.util.concurrent.Executors

object ToolsCreator {
    private val ioExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    private val db by lazy {
        RedditTopDatabase.create()
    }

    private val api by lazy {
        RedditApiService.create()
    }

    fun getRepository(): RedditPostRepository {
        return RedditPostRepository(db, api, ioExecutor)
    }
}