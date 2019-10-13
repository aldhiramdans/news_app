package id.co.code.newsapp.ui.newsdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.FragmentNewsDetailBinding
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.ui.base.BaseFragment
import id.co.code.newsapp.util.Constants
import id.co.code.newsapp.util.StringHelper

class NewsDetailFragment : BaseFragment<NewsDetailViewModel, FragmentNewsDetailBinding>() {

    var news: NewsItem? = null

    companion object {
        fun newInstance(bundle: Bundle?, fragment: Fragment?): Fragment {
            if (bundle != null && fragment != null) {
                fragment.arguments = bundle
            }
            return fragment!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null && arguments!!.containsKey(Constants.NEWS_ITEM)) {
            news = arguments!!.getParcelable(Constants.NEWS_ITEM)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.tvDate.text =
            StringHelper.formatStringFromRFC3339Format(news!!.pub_date!!, "dd MMMM yyyy")
        if (news!!.multimedia != null && news!!.multimedia!!.isNotEmpty()) {
            Glide.with(viewDataBinding.ivThumbnail.context)
                .load(Constants.BASE_IMAGE_URL + news!!.multimedia?.get(0)!!.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewDataBinding.ivThumbnail)
        } else {
            viewDataBinding.ivThumbnail.setImageResource(R.drawable.sample_news)
        }
    }

    override fun setViewModel() {
        viewModel = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)
        viewDataBinding.news = news
    }

    override fun getContentView(): Int = R.layout.fragment_news_detail
}