package com.example.webtooninfo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WebtoonDAO {
    @Insert
    suspend fun insertWebtoon(webtoon: WebtoonLocal)

    @Delete
    suspend fun deleteWebtoon(webtoon: WebtoonLocal)

    @Query("SELECT * FROM webtoons")
    suspend fun getWebtoon(): List<WebtoonLocal>

    @Query("SELECT * FROM webtoons WHERE title = :title")
    suspend fun getWebtoonByTitle(title: String): WebtoonLocal

}