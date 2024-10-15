package com.example.webtooninfo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class WebtoonData(val image: String = "",
                       val title: String = "",
                       val author: String = "",
                       val description: String = "",
                       val rating: Double = 0.0)

fun WebtoonData.toLocal(): WebtoonLocal {
    return WebtoonLocal(
        image = this.image,
        title = this.title,
        author = this.author,
        description = this.description,
        rating = this.rating
    )
}

@Entity(tableName = "webtoons")
data class WebtoonLocal(
    val image: String = "",
    @PrimaryKey val title: String,
    val author: String,
    val description: String,
    val rating: Double
)

fun WebtoonLocal.toData(): WebtoonData {
    return WebtoonData(
        title = this.title,
        author = this.author,
        image = this.image,
        description = this.description,
        rating = this.rating
    )
}



