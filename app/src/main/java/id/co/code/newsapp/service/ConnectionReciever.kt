package id.co.code.newsapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.NewsApplication.Companion.connectivityReceiverListener

class ConnectionReciever : BroadcastReceiver() {
    companion object {

        fun isConnected(): Boolean {
            val cm =
                NewsApplication.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }

    override fun onReceive(context: Context, arg1: Intent) {
        val isConnected = isConnected()

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}