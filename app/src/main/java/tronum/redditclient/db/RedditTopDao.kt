package tronum.redditclient.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tronum.redditclient.data.PostData

@Dao
interface RedditTopDao {
    @Query("SELECT * FROM PostData")
    fun getPosts(): DataSource.Factory<Int, PostData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<PostData>)
}