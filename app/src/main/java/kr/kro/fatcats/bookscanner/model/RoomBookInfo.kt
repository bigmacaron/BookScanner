package kr.kro.fatcats.bookscanner.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RoomBookInfo (

    @PrimaryKey(autoGenerate = true) var isbn : Long?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "publisher") var publisher: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name = "time") var time: String?

        )