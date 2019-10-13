package id.co.code.newsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.data.searchkeyword.SearchKeywordEntity
import id.co.code.newsapp.data.searchkeyword.SearchKeywordRepository
import id.co.code.newsapp.model.News
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.network.NetworkConfig
import id.co.code.newsapp.ui.base.BaseViewModel
import id.co.code.newsapp.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : BaseViewModel() {

    var keywordList: LiveData<List<SearchKeywordEntity>>? = null
    var currentList: List<SearchKeywordEntity>? = ArrayList()

    private var data = MutableLiveData<ArrayList<NewsItem>>()
    private var status = MutableLiveData<Boolean>()
    private var repository: SearchKeywordRepository? = null

    private var page = 0

    var isAppend = false

    init {
        repository = SearchKeywordRepository(NewsApplication.database!!.searchKeywordDao())
        keywordList = repository!!.keywords
    }

    fun searchNews(page: String, query: String, isAppend: Boolean) {
        this.isAppend = isAppend
        if (!isAppend) {
            this.page = 0
        }
        status.value = true
        NetworkConfig().api().findNews(page, query, Constants.API_KEY)
            .enqueue(object : Callback<News> {
                override fun onFailure(call: Call<News>, t: Throwable) {
                    status.value = false
                }

                override fun onResponse(call: Call<News>, response: Response<News>) {
                    status.value = true
                    insert(query) // insert into db
                    if (response.isSuccessful) {
                        data.value = response.body()!!.response!!.docs as ArrayList<NewsItem>?
                    } else {
                        status.value = false
                    }
                }
            })
    }

    fun getMoreNews(query: String, isAppend: Boolean) {
        page++
        searchNews(page.toString(), query, isAppend)
    }

    fun getStatus(): MutableLiveData<Boolean> {
        return status
    }

    fun getNewsItem(): MutableLiveData<ArrayList<NewsItem>> {
        return data
    }

    fun insert(keyword: String) = viewModelScope.launch {
        var isAvailable: Boolean = true
        for (key in currentList!!) {
            if (key.keyword.equals(keyword, true)) {
                isAvailable = false
                break
            }
        }
        if (isAvailable) repository?.insert(SearchKeywordEntity(id = 0, keyword = keyword))
    }
}