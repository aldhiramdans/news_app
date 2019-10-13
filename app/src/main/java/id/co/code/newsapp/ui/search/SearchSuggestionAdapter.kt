package id.co.code.newsapp.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.code.newsapp.R
import id.co.code.newsapp.data.searchkeyword.SearchKeywordEntity
import id.co.code.newsapp.databinding.ItemSuggestionBinding
import id.co.code.newsapp.listener.OnItemClickListener

class SearchSuggestionAdapter :
    RecyclerView.Adapter<SearchSuggestionAdapter.SuggestionViewHolder>() {

    lateinit var itemViewDataBinding: ItemSuggestionBinding
    lateinit var onItemClickListener: OnItemClickListener

    var items: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        itemViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_suggestion,
            parent,
            false
        )
        return SuggestionViewHolder(itemViewDataBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    fun getAdapterPosition(position: Int): String {
        return items[position]
    }

    fun addItems(keywords: List<SearchKeywordEntity>) {
        if (!keywords.isNullOrEmpty()) {
            items.clear()
            for (searchKeyword in keywords) {
                if (items.size < 10) {
                    items.add(searchKeyword.keyword)
                } else {
                    break
                }
            }
            notifyDataSetChanged()
        }
    }

    inner class SuggestionViewHolder(private val holderViewBinding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(holderViewBinding.root) {
        lateinit var itemViewBinding: ItemSuggestionBinding

        fun bindView(keyword: String) {
            itemViewBinding = holderViewBinding
            itemViewBinding.tvSuggestion.text = keyword
            itemViewBinding.tvSuggestion.setOnClickListener {
                onItemClickListener.onItemClick(
                    it,
                    adapterPosition
                )
            }
        }
    }
}