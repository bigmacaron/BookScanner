package kr.kro.fatcats.bookscanner.api

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var DB_NAME  = "book-info-database"

    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,RoomBookInfoDatabase::class.java, DB_NAME
    ).build()

}