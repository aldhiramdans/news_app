package id.co.code.newsapp.data.favorite

import androidx.lifecycle.LiveData

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    val favorites: LiveData<List<FavoriteEntity>> = favoriteDao.findAll()

    suspend fun insert(newsCache: FavoriteEntity) {
        favoriteDao.insert(newsCache)
    }

    suspend fun deleteAll() {
        favoriteDao.deleteAll()
    }

    suspend fun delete(favoriteEntity: FavoriteEntity) {
        favoriteDao.delete(favoriteEntity)
    }
}