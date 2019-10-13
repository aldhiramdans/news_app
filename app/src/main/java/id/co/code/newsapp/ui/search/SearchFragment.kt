package id.co.code.newsapp.ui.search

import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.FragmentSearchBinding
import id.co.code.newsapp.listener.OnItemClickListener
import id.co.code.newsapp.listener.RecyclerAdapterListener
import id.co.code.newsapp.ui.base.BaseFragment

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>(),
    RecyclerAdapterListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {

    private lateinit var mAdapter: SearchAdapter
    private lateinit var mSuggestionAdapter: SearchSuggestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = SearchAdapter()
        mAdapter.setAdapterListener(this)
        mSuggestionAdapter = SearchSuggestionAdapter()
        mSuggestionAdapter.onItemClickListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.swipeRefresh.setOnRefreshListener(this)
    }

    override fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewDataBinding.viewmodel = viewModel
        viewDataBinding.handlers = SearchEventHandler()
    }

    override fun getContentView(): Int = R.layout.fragment_search

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(viewDataBinding.searchToolbar)
            viewDataBinding.searchToolbar.navigationIcon =
                resources.getDrawable(R.drawable.ic_arrow_back_black)
            viewDataBinding.searchToolbar.setNavigationOnClickListener {
                activity!!.onBackPressed()
                animationCloseActivity()
            }
            activity!!.window.statusBarColor =
                context!!.resources.getColor(R.color.colorYellowCyber)
        }

        viewDataBinding.swipeRefresh.isEnabled = false
        viewDataBinding.etSearch.setOnClickListener {
            setVisibleSuggestion(View.VISIBLE)
        }
        viewDataBinding.etSearch.setOnFocusChangeListener { _, b ->
            setVisibleSuggestion(if (b) View.VISIBLE else View.GONE)
        }
        setupAdapter()
        handleViewObserver()
        viewDataBinding.recyclerNews.requestFocus()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerOrientation(newConfig.orientation)
    }

    override fun onItemClick(view: View, position: Int) {
        viewDataBinding.etSearch.setText(mSuggestionAdapter.getAdapterPosition(position))
        if (isNetworkConnected()) {
            search(viewDataBinding.etSearch.text.toString())
        }
    }

    override fun onItemAdapterClick(v: View, position: Int) {

    }

    override fun onRefresh() {
        viewDataBinding.swipeRefresh.isRefreshing = false
    }

    override fun onLoadMore() {
        if (isNetworkConnected()) viewModel.getMoreNews(
            viewDataBinding.etSearch.text.toString(),
            true
        )
    }

    private fun setupRecyclerOrientation(orientation: Int) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                viewDataBinding.recyclerNews.layoutManager =
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                viewDataBinding.recyclerNews.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun handleViewObserver() {
        viewModel.getStatus().observe(this, Observer { t ->
            if (!t) {
                viewDataBinding.recyclerNews.visibility = View.GONE
                viewDataBinding.textStatus.visibility = View.VISIBLE
            } else {
                viewDataBinding.recyclerNews.visibility = View.VISIBLE
                viewDataBinding.textStatus.visibility = View.GONE
            }
        })
        viewModel.getNewsItem().observe(this, Observer { listItem ->
            if (listItem != null) {
                mAdapter.setupData(listItem, viewModel.isAppend)
                viewDataBinding.swipeRefresh.isRefreshing = false
            }
        })
        viewModel.keywordList!!.observe(this, Observer {
            if (it.isNotEmpty()) {
                mSuggestionAdapter.addItems(it)
                viewModel.currentList = it
            }
        })
    }

    private fun setupAdapter() {
        viewDataBinding.recyclerNews.hasFixedSize()
        setupRecyclerOrientation(activity!!.resources.configuration.orientation)
        viewDataBinding.recyclerNews.adapter = mAdapter

        viewDataBinding.recyclerSuggestion.hasFixedSize()
        viewDataBinding.recyclerSuggestion.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        viewDataBinding.recyclerSuggestion.adapter = mSuggestionAdapter
    }

    private fun search(query: String) {
        hideKeyboard()
        setVisibleSuggestion(View.GONE)
        viewDataBinding.recyclerNews.requestFocus()
        viewDataBinding.swipeRefresh.isRefreshing = true
        viewModel.searchNews("0", query, false)
    }

    private fun setVisibleSuggestion(visible: Int) {
        viewDataBinding.recyclerSuggestion.visibility = visible
    }

    inner class SearchEventHandler {
        fun onSearchClick(view: View) {
            val key = viewDataBinding.etSearch.text.toString()
            if (isNetworkConnected() && key.isNotEmpty()) {
                search(key)
            }
        }
    }
}