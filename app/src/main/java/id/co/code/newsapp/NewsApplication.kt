package id.co.code.newsapp

import android.app.Application
import android.content.Context
import id.co.code.newsapp.data.NewsDatabase
import id.co.code.newsapp.service.ConnectionReciever

class NewsApplication : Application() {

    companion object {
        var context: Context? = null
        var database: NewsDatabase? = null
        var connectivityReceiverListener: ConnectionReciever.ConnectivityReceiverListener? = null

        fun get(): NewsApplication {
            return context!!.applicationContext as NewsApplication
        }

        fun setConnectivityListener(listener: ConnectionReciever.ConnectivityReceiverListener) {
            connectivityReceiverListener = listener
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        database = NewsDatabase.getDatabase(this)
    }
}