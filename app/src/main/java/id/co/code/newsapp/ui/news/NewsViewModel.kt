package id.co.code.newsapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.data.newscache.NewsCacheEntity
import id.co.code.newsapp.data.newscache.NewsCacheRepository
import id.co.code.newsapp.model.Headline
import id.co.code.newsapp.model.Multimedia
import id.co.code.newsapp.model.News
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.network.NetworkConfig
import id.co.code.newsapp.ui.base.BaseViewModel
import id.co.code.newsapp.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsViewModel : BaseViewModel() {

    val cacheData: LiveData<List<NewsCacheEntity>>
    private var data = MutableLiveData<ArrayList<NewsItem>>()

    private var repository: NewsCacheRepository? = null
    var status = MutableLiveData<Boolean>()
    var newsItemCache: ArrayList<NewsItem>

    var page = 0
    var isAppend = false

    init {
        //getNews("0", isAppend) // default page 0, get 0-10 index data
        newsItemCache = ArrayList()
        repository = NewsCacheRepository(NewsApplication.database!!.newsCacheDao())
        cacheData = repository!!.news
    }

    fun getNews(page: String, isAppend: Boolean) {
        this.isAppend = isAppend
        if (!isAppend) {
            this.page = 0
        }
        status.value = true
        NetworkConfig().api().getNews(page, Constants.API_KEY).enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                status.value = false
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                status.value = true
                if (response.isSuccessful) {
                    val items = response.body()!!.response!!.docs as ArrayList<NewsItem>?
                    data.value = items
                    if (page == "0") {
                        delete() // clear table
                        insert(items!!) // insert
                    }
                } else {
                    status.value = false
                }
            }
        })
    }

    fun getMoreNews(isAppend: Boolean) {
        page++
        getNews(page.toString(), isAppend)
    }

    fun getNewsItem(): MutableLiveData<ArrayList<NewsItem>> {
        return data
    }

    // viewModelScop is a variable from ViewModel(), to handle lifecycle and run on background thread
    fun insert(newsItem: ArrayList<NewsItem>) = viewModelScope.launch {
        for (item in newsItem) {
            val favorite = NewsCacheEntity(
                id = 0,
                title = item.headline!!.main!!,
                snippet = item.snippet!!,
                leadParagraph = item.lead_paragraph!!,
                abstractParagraph = item.abstract!!,
                date = item.pub_date!!,
                webUrl = item.web_url!!,
                imageUrl = if (item.multimedia != null && !item.multimedia.isNullOrEmpty()) item.multimedia[0].url!! else ""
            )
            repository?.insert(favorite)
        }
    }

    fun delete() = viewModelScope.launch {
        repository?.deleteAll()
    }

    fun setItemChace(newsCaches: List<NewsCacheEntity>) {
        val news: ArrayList<NewsItem> = ArrayList()
        for (favorite in newsCaches) {
            val headline = Headline(main = favorite.title)
            val multimedia = Multimedia(url = favorite.imageUrl)
            val item = NewsItem(
                snippet = favorite.snippet,
                web_url = favorite.webUrl,
                headline = headline,
                pub_date = favorite.date,
                abstract = favorite.abstractParagraph,
                lead_paragraph = favorite.leadParagraph,
                multimedia = listOf(multimedia)
            )
            news.add(item)
        }
        newsItemCache = news
    }
}