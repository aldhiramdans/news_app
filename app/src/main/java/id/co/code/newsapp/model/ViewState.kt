package id.co.code.newsapp.model

import android.view.View
import id.co.code.newsapp.databinding.ViewStateBinding

data class ViewState(
    var stateStatus: Status = Status.GONE,
    var stringMessage: String? = null,
    var isErrorBlocking: Boolean = false,
    var errorType: ErrorType = ErrorType.DEFAULT
) {
    enum class Status {
        PROGRESSING,
        ERROR,
        UNAUTHORIZED,
        OBSOLETED,
        GONE;
    }

    enum class ErrorType {
        BANNER,
        DEFAULT
    }

    interface RetryRequest{
        fun retry(errorType: ErrorType)
        fun handleUnauthorized()
    }
}

fun ViewStateBinding.setErrorLayoutEnable(mainContainer: View?, isErrorBlocking: Boolean, message: String?) {
    this.layoutLoading.visibility = View.GONE
    this.tvErrorMessage.text = message

    if (isErrorBlocking) {
        mainContainer?.visibility = View.GONE
        this.layoutError.visibility = View.VISIBLE
    } else {
        mainContainer?.visibility = View.VISIBLE
        this.layoutError.visibility = View.GONE
    }
}

fun ViewStateBinding.setLoadingEnable(mainContainer: View?) {
    mainContainer?.visibility = View.GONE
    this.layoutError.visibility = View.GONE
    this.layoutLoading.visibility = View.VISIBLE
}

fun ViewStateBinding.setMainContainerEnable(mainContainer: View?) {
    mainContainer?.visibility = View.VISIBLE
    this.layoutError.visibility = View.GONE
    this.layoutLoading.visibility = View.GONE
}
