package id.co.code.newsapp.data.favorite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * from tbl_favorite ORDER BY id")
    fun findAll(): LiveData<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsCache: FavoriteEntity)

    @Query("DELETE FROM tbl_favorite")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(favorite: FavoriteEntity)
}