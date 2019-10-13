package id.co.code.newsapp.util

import java.text.SimpleDateFormat
import java.util.*

class StringHelper {

    companion object {
        fun formatStringFromRFC3339Format(resourceDate: String, outputFormat: String): String {
            val df = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ", Locale.US)
            val result: Date
            var date = ""

            try {
                result = df.parse(resourceDate)!!
                val sdf = SimpleDateFormat(outputFormat, Locale.US)
                date = sdf.format(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date
        }
    }
}