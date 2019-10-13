package id.co.code.newsapp.data.newscache

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsCacheDao {
    @Query("SELECT * from tbl_news ORDER BY id ASC LIMIT 10")
    fun findAll(): LiveData<List<NewsCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsCache: NewsCacheEntity)

    @Query("DELETE FROM tbl_news")
    suspend fun deleteAll()
}