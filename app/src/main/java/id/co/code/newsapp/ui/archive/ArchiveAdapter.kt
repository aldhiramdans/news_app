package id.co.code.newsapp.ui.archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import id.co.code.newsapp.R
import id.co.code.newsapp.data.favorite.FavoriteEntity
import id.co.code.newsapp.databinding.NewsItemBinding
import id.co.code.newsapp.listener.OnItemClickListener
import id.co.code.newsapp.listener.RecyclerAdapterListener
import id.co.code.newsapp.model.NewsItem
import id.co.code.newsapp.util.Constants
import id.co.code.newsapp.util.StringHelper

class ArchiveAdapter : RecyclerView.Adapter<ArchiveAdapter.NewsViewHolder>() {
    var data: ArrayList<NewsItem> = ArrayList()
    var listener: OnItemClickListener? = null

    private lateinit var itemViewDataBinding: NewsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        itemViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.news_item,
            parent,
            false
        )
        return NewsViewHolder(itemViewDataBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindView(data[position])
        holder.itemView.setOnClickListener {
            if (listener != null) listener?.onItemClick(it, position)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setupData(newsItem: ArrayList<NewsItem>) {
        data.clear()
        data.addAll(newsItem)
        notifyDataSetChanged()
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
            if (item.multimedia != null && item.multimedia.isNotEmpty()) {
                Glide.with(itemViewBinding.ivThumbnail.context)
                    .load(Constants.BASE_IMAGE_URL + item.multimedia[0].url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemViewBinding.ivThumbnail)
            } else {
                itemViewBinding.ivThumbnail.setImageResource(R.drawable.sample_news)
            }
        }
    }
}