package id.co.code.newsapp.data.searchkeyword

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchKeywordDao {
    @Query("SELECT * from tbl_search_key ORDER BY id DESC LIMIT 10")
    fun findAll(): LiveData<List<SearchKeywordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsCache: SearchKeywordEntity)

    @Query("DELETE FROM tbl_search_key")
    suspend fun deleteAll()
}