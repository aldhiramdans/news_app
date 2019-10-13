package id.co.code.newsapp.data.newscache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_news")
data class NewsCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "snippet") val snippet: String,
    @ColumnInfo(name = "lead_paragraph") val leadParagraph: String,
    @ColumnInfo(name = "abstract") val abstractParagraph: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "web_url") val webUrl: String,
    @ColumnInfo(name = "image_url") val imageUrl: String
)