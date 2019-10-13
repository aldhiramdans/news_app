package id.co.code.newsapp.ui.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.NewsItemBinding
import id.co.code.newsapp.listener.RecyclerAdapterListener
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.util.Constants
import id.co.code.newsapp.util.StringHelper

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM = 0
        const val TYPE_LOADING = 1
    }

    lateinit var itemViewDataBinding: NewsItemBinding
    var listener: RecyclerAdapterListener? = null

    var data: ArrayList<NewsItem> = ArrayList()
    var isLoading: Boolean = false

    fun setAdapterListener(listener: RecyclerAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        itemViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.news_item,
            parent,
            false
        )
        val itemHolder =
            NewsViewHolder(itemViewDataBinding)
        return when (viewType) {
            TYPE_ITEM -> itemHolder
            else -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = data[position].typeItem

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == data.size - 2) {
            listener!!.onLoadMore()
        }

        when (holder) {
            is NewsViewHolder -> {
                holder.bindView(data[position])
                holder.itemView.setOnClickListener {
                    listener?.onItemAdapterClick(it, position)
                }
            }
        }
    }

    fun setupData(newsItem: ArrayList<NewsItem>, isAppend: Boolean) {
        if (isAppend) {
            removeFooter()
        } else {
            data.clear()
        }

        data.addAll(newsItem)
        addFooter() // add loading
        notifyDataSetChanged()
    }

    private fun addFooter() {
        isLoading = true
        data.add(NewsItem(typeItem = TYPE_LOADING)) // default 0
        notifyItemChanged(data.size - 1)
    }

    private fun removeFooter() {
        isLoading = false
        if (data.isNotEmpty()) data.removeAt(data.size - 1)
        notifyItemRemoved(data.size)
    }

    class NewsViewHolder(private val itemViewDataBinding: NewsItemBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root) {
        lateinit var itemViewBinding: NewsItemBinding

        fun bindView(item: NewsItem) {
            itemViewBinding = itemViewDataBinding
            itemViewBinding.tvTitle.text = item.headline?.main
            itemViewBinding.tvSnippet.text = item.snippet
            itemViewBinding.tvDate.text =
                StringHelper.formatStringFromRFC3339Format(item.pub_date!!, "dd MMMM yyyy")
            if (item.multimedia != null && item.multimedia!!.isNotEmpty()) {
                Glide.with(itemViewBinding.ivThumbnail.context)
                    .load(Constants.BASE_IMAGE_URL + item.multimedia?.get(0)!!.url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemViewBinding.ivThumbnail)
            } else {
                itemViewBinding.ivThumbnail.setImageResource(R.drawable.sample_news)
            }
        }
    }

    // Loading view holder
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}