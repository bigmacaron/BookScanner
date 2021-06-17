package kr.kro.fatcats.bookscanner.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class ListInfo (

    val isbn : Long?,
    val title: String?,
    val author: String?,
    val publisher: String?,
    val url: String?,
    val ct_date : String?,
    val mod_date : String?,
    val time: Long?
        )