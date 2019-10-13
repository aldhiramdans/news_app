package id.co.code.newsapp.model

import android.os.Parcel
import android.os.Parcelable

data class News(
    val status: String? = null,
    val response: Docs? = null
)

data class Docs(
    val docs: ArrayList<NewsItem?>? = null
)


data class NewsItem(
    val web_url: String? = null,
    val snippet: String? = null,
    val lead_paragraph: String? = null,
    val abstract: String? = null,
    val source: String? = null,
    val multimedia: List<Multimedia>? = null,
    val headline: Headline? = null,
    val pub_date: String? = null,
    val typeItem: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Multimedia.CREATOR),
        parcel.readParcelable<Headline>(Headline.javaClass.classLoader),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(web_url)
        parcel.writeString(snippet)
        parcel.writeString(lead_paragraph)
        parcel.writeString(abstract)
        parcel.writeString(source)
        parcel.writeTypedList(multimedia)
        parcel.writeParcelable(headline, flags)
        parcel.writeString(pub_date)
        parcel.writeInt(typeItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsItem> {
        override fun createFromParcel(parcel: Parcel): NewsItem {
            return NewsItem(parcel)
        }

        override fun newArray(size: Int): Array<NewsItem?> {
            return arrayOfNulls(size)
        }
    }
}

data class Multimedia(val url: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Multimedia> {
        override fun createFromParcel(parcel: Parcel): Multimedia {
            return Multimedia(parcel)
        }

        override fun newArray(size: Int): Array<Multimedia?> {
            return arrayOfNulls(size)
        }
    }

}

data class Headline(val main: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(main)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Headline> {
        override fun createFromParcel(parcel: Parcel): Headline {
            return Headline(parcel)
        }

        override fun newArray(size: Int): Array<Headline?> {
            return arrayOfNulls(size)
        }
    }

}