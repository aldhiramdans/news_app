package id.co.code.newsapp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import id.co.code.newsapp.R
import id.co.code.newsapp.util.ViewUtil


abstract class BaseFragment<T: BaseViewModel, V: ViewDataBinding>: Fragment() {

    lateinit var viewModel: T
    lateinit var viewDataBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        viewDataBinding.lifecycleOwner = this
        setViewModel()
        return viewDataBinding.root
    }

    abstract fun setViewModel()
    abstract fun getContentView(): Int

    fun navigateActivity(context: Context, bundle: Bundle, classActivity: Class<*>) {
        BaseActivity.navigationTo(context, bundle, classActivity)
        animationOpenActivity()
    }

    fun animationOpenActivity() {
        activity!!.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left)
    }

    fun animationCloseActivity() {
        activity!!.overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right)
    }

    fun hideKeyboard() {
        ViewUtil.hideKeyboard(activity!!)
    }

    fun isNetworkConnected() : Boolean {
        return (activity as BaseActivity<*>).isNetworkConnected()
    }
}