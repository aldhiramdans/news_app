package id.co.code.newsapp.ui.newsdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.data.favorite.FavoriteEntity
import id.co.code.newsapp.data.favorite.FavoriteRepository
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NewsDetailViewModel : BaseViewModel() {

    val favorites: LiveData<List<FavoriteEntity>>
    var news: List<FavoriteEntity> = ArrayList()
    private var repository: FavoriteRepository? = null

    init {
        repository = FavoriteRepository(NewsApplication.database!!.favoriteDao())
        favorites = repository!!.favorites
    }

    fun insert(news: NewsItem) = viewModelScope.launch {
        if (!isNewsExistOnRepository(news.headline!!.main!!)) {
            val favorite = FavoriteEntity(
                id = 0,
                title = news.headline.main!!,
                snippet = news.snippet!!,
                leadParagraph = news.lead_paragraph!!,
                abstractParagraph = news.abstract!!,
                date = news.pub_date!!,
                webUrl = news.web_url!!,
                imageUrl = if (news.multimedia != null && !news.multimedia.isNullOrEmpty()) news.multimedia[0].url!! else ""
            )
            repository?.insert(favorite)
        }
    }

    fun isNewsExistOnRepository(title: String): Boolean {
        for (news in this.news) {
            if (news.title.equals(title, false)) {
                return true
            }
        }
        return false
    }

    fun delete(news: NewsItem) = viewModelScope.launch {
        val favorite = FavoriteEntity(
            id = 0,
            title = news.headline!!.main!!,
            snippet = news.snippet!!,
            leadParagraph = news.lead_paragraph!!,
            abstractParagraph = news.abstract!!,
            date = news.pub_date!!,
            webUrl = news.web_url!!,
            imageUrl = if (news.multimedia != null && !news.multimedia.isNullOrEmpty()) news.multimedia[0].url!! else ""
        )
        repository?.delete(favorite)
    }
}