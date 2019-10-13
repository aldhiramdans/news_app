package id.co.code.newsapp.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class ViewUtil {

    companion object {
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                activity.window.decorView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}