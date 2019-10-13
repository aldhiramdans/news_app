package id.co.code.newsapp.ui.news

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.FragmentHomeBinding
import id.co.code.newsapp.listener.RecyclerAdapterListener
import id.co.code.newsapp.ui.base.BaseFragment
import id.co.code.newsapp.ui.news.adapter.NewsAdapter
import id.co.code.newsapp.ui.newsdetail.NewsDetailActivity
import id.co.code.newsapp.util.Constants
import kotlinx.android.synthetic.main.fragment_home.*

class NewsFragment : BaseFragment<NewsViewModel, FragmentHomeBinding>(), RecyclerAdapterListener,
    SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = NewsAdapter()
        mAdapter.setAdapterListener(this)
    }

    override fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        viewDataBinding.viewmodel = viewModel
    }

    override fun getContentView(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.swipeRefresh.setOnRefreshListener(this)
        viewDataBinding.swipeRefresh.isRefreshing = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.recyclerview.hasFixedSize()
        setupRecyclerOrientation(activity!!.resources.configuration.orientation)
        viewDataBinding.recyclerview.adapter = mAdapter
        handleViewModelOberserver()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerOrientation(newConfig.orientation)
    }

    override fun onItemAdapterClick(v: View, position: Int) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(Constants.NEWS_LIST, mAdapter.data)
        bundle.putInt(Constants.INDEX_PAGER, position)

        val intent = Intent(activity!!, NewsDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        animationOpenActivity()
    }

    override fun onLoadMore() {
        if (isNetworkConnected()) viewModel.getMoreNews(true)
    }

    override fun onRefresh() {
        viewDataBinding.swipeRefresh.isRefreshing = true
        if (isNetworkConnected()) {
            viewModel.getNews("0", false)
        } else {
            viewDataBinding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecyclerOrientation(orientation: Int) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                viewDataBinding.recyclerview.layoutManager =
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                viewDataBinding.recyclerview.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun handleViewModelOberserver() {
        viewModel.status.observe(this, Observer { t ->
            if (!t) {
                viewDataBinding.recyclerview.visibility = View.GONE
                viewDataBinding.textStatus.visibility = View.VISIBLE
                viewDataBinding.swipeRefresh.isRefreshing = false
            } else {
                recyclerview.visibility = View.VISIBLE
                textStatus.visibility = View.GONE
            }
        })
        viewModel.getNewsItem().observe(this, Observer { listItem ->
            if (listItem != null) {
                mAdapter.setupData(listItem, viewModel.isAppend)
                viewDataBinding.swipeRefresh.isRefreshing = false
            }
        })
        viewModel.cacheData.observe(this, Observer {
            if (it.size == 10) { // make sure if size == 10
                viewModel.setItemChace(it)
            }
        })

        if (isNetworkConnected()) {
            viewModel.getNews("0", false)
        } else {
            view!!.postDelayed({
                if (viewModel.newsItemCache.isNotEmpty()) {
                    mAdapter.setupData(viewModel.newsItemCache, false)
                } else {
                    viewModel.status.value = false
                }
                viewDataBinding.swipeRefresh.isRefreshing = false
            }, 500)
        }
    }
}