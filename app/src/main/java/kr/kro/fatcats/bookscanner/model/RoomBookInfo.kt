package kr.kro.fatcats.bookscanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomBookInfo (

    @PrimaryKey val isbn : Long?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "publisher") val publisher: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "time") val time: String?
    )