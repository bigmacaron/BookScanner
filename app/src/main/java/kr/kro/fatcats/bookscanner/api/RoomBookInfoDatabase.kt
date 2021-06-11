package kr.kro.fatcats.bookscanner.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.kro.fatcats.bookscanner.model.RoomBookInfo

@Database(entities = [RoomBookInfo::class],version = 1)
abstract class RoomBookInfoDatabase  : RoomDatabase(){
    abstract fun roomBookInfoDao() : RoomBookInfoDao

    companion object{
        private var instance : RoomBookInfoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RoomBookInfoDatabase?{
            if (instance == null) {
                synchronized(RoomBookInfoDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomBookInfoDatabase::class.java,
                        "book-info-database"
                    ).build()
                }
            }
            return instance
        }
    }
}