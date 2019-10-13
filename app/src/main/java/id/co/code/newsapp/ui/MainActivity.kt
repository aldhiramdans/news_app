package id.co.code.newsapp.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import id.co.code.newsapp.R
import id.co.code.newsapp.databinding.ActivityMainBinding
import id.co.code.newsapp.ui.archive.ArchiveActivity
import id.co.code.newsapp.ui.base.BaseActivity
import id.co.code.newsapp.ui.search.SearchActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.handler = Handlers()
        setSupportActionBar(viewDataBinding.container.toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_news,
                R.id.nav_favourite
            ), viewDataBinding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        viewDataBinding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun goToSearch() {
        navigateActivity(this, Bundle.EMPTY, SearchActivity::class.java)
    }

    private fun goToArchive() {
        navigateActivity(this, Bundle.EMPTY, ArchiveActivity::class.java)
    }

    inner class Handlers {
        fun onSearchClick(view: View) {
            goToSearch()
        }

        fun onArchiveClick(view: View) {
            goToArchive()
        }
    }
}
