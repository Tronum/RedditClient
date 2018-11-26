package tronum.redditclient.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tronum.redditclient.App
import tronum.redditclient.data.PostData

@Database(
    entities = [PostData::class],
    version = 1,
    exportSchema = false
)
abstract class RedditTopDatabase : RoomDatabase() {
    abstract fun topPostDao(): RedditTopDao

    companion object {
        fun create(): RedditTopDatabase {
            return Room.databaseBuilder(App.instance, RedditTopDatabase::class.java, "reddit_top.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}