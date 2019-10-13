package id.co.code.newsapp.ui.archive

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.data.favorite.FavoriteEntity
import id.co.code.newsapp.data.favorite.FavoriteRepository
import id.co.code.newsapp.model.Headline
import id.co.code.newsapp.model.Multimedia
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ArchiveViewModel : BaseViewModel() {

    val favorites: LiveData<List<FavoriteEntity>>
    var news: List<FavoriteEntity> = ArrayList()
    var currentNews: ArrayList<NewsItem> = ArrayList()
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

    fun setNewsItems(news: List<FavoriteEntity>) {
        var newsList = ArrayList<NewsItem>()
        for (favorite in news) {
            val headline = Headline(main = favorite.title)
            val multimedia = Multimedia(url = favorite.imageUrl)
            val item = NewsItem(
                snippet = favorite.snippet,
                lead_paragraph = favorite.leadParagraph,
                web_url = favorite.webUrl,
                headline = headline,
                pub_date = favorite.date,
                multimedia = listOf(multimedia)
            )
            newsList.add(item)
        }
        currentNews = newsList
    }

    private fun isNewsExistOnRepository(title: String): Boolean {
        for (news in this.news) {
            if (news.title.equals(title, false)) {
                return true
            }
        }
        return false
    }
}