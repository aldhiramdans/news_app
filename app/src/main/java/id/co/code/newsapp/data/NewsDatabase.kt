package id.co.code.newsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.co.code.newsapp.data.favorite.FavoriteDao
import id.co.code.newsapp.data.favorite.FavoriteEntity
import id.co.code.newsapp.data.newscache.NewsCacheDao
import id.co.code.newsapp.data.newscache.NewsCacheEntity
import id.co.code.newsapp.data.searchkeyword.SearchKeywordDao
import id.co.code.newsapp.data.searchkeyword.SearchKeywordEntity


/**
 * source : https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
 */
@Database(
    entities = [NewsCacheEntity::class, FavoriteEntity::class, SearchKeywordEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsCacheDao(): NewsCacheDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchKeywordDao(): SearchKeywordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NewsDatabase::class.java,
                    "news_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}