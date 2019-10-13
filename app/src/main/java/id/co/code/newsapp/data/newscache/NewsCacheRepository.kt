package id.co.code.newsapp.data.newscache

import androidx.lifecycle.LiveData

class NewsCacheRepository(private val newsCacheDao: NewsCacheDao) {

    val news: LiveData<List<NewsCacheEntity>> = newsCacheDao.findAll()

    suspend fun insert(newsCache: NewsCacheEntity) {
        newsCacheDao.insert(newsCache)
    }

    suspend fun deleteAll() {
        newsCacheDao.deleteAll()
    }
}