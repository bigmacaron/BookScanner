package kr.kro.fatcats.bookscanner.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("author")
    val author: String,
    @SerializedName("image")
    val url: String,
    @SerializedName("isbn")
    val isbn: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("title")
    val title: String
)