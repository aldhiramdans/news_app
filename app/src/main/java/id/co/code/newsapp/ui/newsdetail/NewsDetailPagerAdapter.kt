package id.co.code.newsapp.ui.newsdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.util.Constants

class NewsDetailPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var news: ArrayList<NewsItem>? = ArrayList()
    var views: ArrayList<Fragment>? = ArrayList()

    override fun getItem(position: Int): Fragment = views!![position]

    override fun getCount(): Int = views!!.size

    fun addViews(list: ArrayList<NewsItem>) {
        news!!.clear()
        news!!.addAll(list)
        for (item in news!!) {
            val bundle = Bundle()
            bundle.putParcelable(Constants.NEWS_ITEM, item)
            views!!.add(NewsDetailFragment.newInstance(bundle = bundle, fragment = NewsDetailFragment()))
        }
        notifyDataSetChanged()
    }
}