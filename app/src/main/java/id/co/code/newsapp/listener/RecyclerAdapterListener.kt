package id.co.code.newsapp.listener

import android.view.View

interface RecyclerAdapterListener {
    fun onItemAdapterClick(v: View, position: Int)
    fun onLoadMore()
}