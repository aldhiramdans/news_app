package id.co.code.newsapp.ui.newsdetail

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.ActivityNewsDetailBinding
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.ui.base.BaseActivity
import id.co.code.newsapp.util.Constants
import java.util.*

class NewsDetailActivity : BaseActivity<ActivityNewsDetailBinding>(),
    ViewPager.OnPageChangeListener {
    private lateinit var viewModel: NewsDetailViewModel
    private lateinit var pagerAdapter: NewsDetailPagerAdapter

    var viewPosition = 0
    var isFromArchive = false

    override fun getContentView(): Int = R.layout.activity_news_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)

        viewModel.favorites.observe(this, androidx.lifecycle.Observer {
            viewModel.news = it
        })

        val bundle = intent.extras
        var newsList: ArrayList<NewsItem>?

        pagerAdapter = NewsDetailPagerAdapter(supportFragmentManager)
        viewDataBinding.viewPager.addOnPageChangeListener(this)
        viewDataBinding.viewPager.adapter = pagerAdapter

        if (bundle != null) {
            isFromArchive = bundle.getBoolean(Constants.IS_FROM_ARCHIVE, false)
            viewPosition = bundle.getInt(Constants.INDEX_PAGER, 0)
            newsList = bundle.getParcelableArrayList<NewsItem>(Constants.NEWS_LIST)
            pagerAdapter.addViews(newsList!!)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorPrimary)
        }

        viewDataBinding.toolbar.setNavigationOnClickListener { onBackPressed() }

        viewDataBinding.ivFavorite.setOnClickListener {
            val newsItem = pagerAdapter.news?.get(viewPosition)
            viewModel.insert(newsItem!!)
            viewDataBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
            Snackbar.make(viewDataBinding.container, "Saved successfull", Snackbar.LENGTH_SHORT)
                .show()
        }

        viewDataBinding.ivDelete.setOnClickListener {
            val newsItem = pagerAdapter.news?.get(viewPosition)
            //viewModel.delete(newsItem!!)
            viewDataBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_not_active)
            Handler().post {
                finish()
                animationCloseActivity()
            }
        }

        if (isFromArchive) {
            viewDataBinding.ivDelete.visibility = View.VISIBLE
        }

        Handler().postDelayed({
            viewDataBinding.viewPager.currentItem = viewPosition
            handleBadgesObserver(viewPosition)
        }, 500)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        viewPosition = position
        handleBadgesObserver(viewPosition)
    }

    private fun handleBadgesObserver(position: Int) {
        val news = pagerAdapter.news?.get(position)
        if (viewModel.isNewsExistOnRepository(news!!.headline!!.main!!)) {
            viewDataBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_active)
        } else {
            viewDataBinding.ivFavorite.setImageResource(R.drawable.ic_favorite_not_active)
        }
    }
}