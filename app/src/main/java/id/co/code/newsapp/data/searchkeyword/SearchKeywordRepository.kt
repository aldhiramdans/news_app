package id.co.code.newsapp.data.searchkeyword

import androidx.lifecycle.LiveData

class SearchKeywordRepository(private val searchDao: SearchKeywordDao) {

    val keywords: LiveData<List<SearchKeywordEntity>> = searchDao.findAll()

    suspend fun insert(newsCache: SearchKeywordEntity) {
        searchDao.insert(newsCache)
    }

    suspend fun deleteAll() {
        searchDao.deleteAll()
    }
}