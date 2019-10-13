package id.co.code.newsapp.ui.archive

import android.os.Build
import android.os.Bundle
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.ActivityArchiveBinding
import id.co.code.newsapp.databinding.ActivitySearchBinding
import id.co.code.newsapp.ui.base.BaseActivity

class ArchiveActivity : BaseActivity<ActivityArchiveBinding>() {
    override fun getContentView(): Int {
        return R.layout.activity_archive
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.colorYellowCyber)
        }
        viewDataBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}