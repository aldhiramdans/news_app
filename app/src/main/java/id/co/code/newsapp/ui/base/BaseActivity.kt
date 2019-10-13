package id.co.code.newsapp.ui.base

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import id.co.code.newsapp.NewsApplication
import id.co.code.newsapp.R
import id.co.code.newsapp.service.ConnectionReciever

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity(),
    ConnectionReciever.ConnectivityReceiverListener {

    lateinit var viewDataBinding: V
    lateinit var mIntentFilter: IntentFilter
    lateinit var mConnectivityReceiver: ConnectionReciever

    companion object {
        fun navigationTo(context: Context, bundle: Bundle, activityClass: Class<*>) {
            val intent = Intent(context, activityClass)
            intent.putExtras(bundle)
            ContextCompat.startActivity(context, intent, bundle)
        }

        fun startIntent(context: Context, bundle: Bundle, activityClass: Class<*>): Intent {
            var intent = Intent(context, activityClass)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, getContentView())
        viewDataBinding.lifecycleOwner = this
        mIntentFilter = IntentFilter()
        mIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mConnectivityReceiver = ConnectionReciever()
    }

    override fun onResume() {
        super.onResume()
        NewsApplication.setConnectivityListener(this)
        registerReceiver(mConnectivityReceiver, mIntentFilter)
        checkConnection()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mConnectivityReceiver)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        animationCloseActivity()
    }

    // Method to manually check connection status
    fun checkConnection() {
        val isConnected = ConnectionReciever.isConnected()
        onNetworkConnectionChanged(isConnected)
    }

    fun isNetworkConnected(): Boolean {
        return ConnectionReciever.isConnected()
    }

    abstract fun getContentView(): Int

    fun navigateActivity(context: Context, bundle: Bundle, activityClass: Class<*>) {
        navigationTo(context, bundle, activityClass)
        animationOpenActivity()
    }

    fun animationOpenActivity() {
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
    }

    fun animationCloseActivity() {
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }
}