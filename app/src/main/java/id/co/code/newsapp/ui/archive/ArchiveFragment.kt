package id.co.code.newsapp.ui.archive

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.FragmentArchiveBinding
import id.co.code.newsapp.listener.OnItemClickListener
import id.co.code.newsapp.ui.base.BaseFragment
import id.co.code.newsapp.ui.newsdetail.NewsDetailActivity
import id.co.code.newsapp.util.Constants

class ArchiveFragment : BaseFragment<ArchiveViewModel, FragmentArchiveBinding>(),
    OnItemClickListener {

    private lateinit var mAdapter: ArchiveAdapter

    override fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
    }

    override fun getContentView(): Int = R.layout.fragment_archive

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = ArchiveAdapter()
        mAdapter.listener = this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.recyclerview.hasFixedSize()
        setupRecyclerOrientation(activity!!.resources.configuration.orientation)
        viewDataBinding.recyclerview.adapter = mAdapter
        handleViewModelOberserver()

        Handler().postDelayed({
            mAdapter.setupData(viewModel.currentNews)
        }, 500)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupRecyclerOrientation(newConfig.orientation)
    }

    override fun onItemClick(view: View, position: Int) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(Constants.NEWS_LIST, mAdapter.data)
        bundle.putBoolean(Constants.IS_FROM_ARCHIVE, true)
        bundle.putInt(Constants.INDEX_PAGER, position)

        val intent = Intent(activity!!, NewsDetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        animationOpenActivity()
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
        viewModel.favorites.observe(this, Observer {
            if (it != null) {
                viewModel.setNewsItems(it)
            }
        })
    }
}