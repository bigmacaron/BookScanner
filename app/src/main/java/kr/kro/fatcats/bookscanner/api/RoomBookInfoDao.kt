package kr.kro.fatcats.bookscanner.api

import androidx.room.*
import kr.kro.fatcats.bookscanner.model.RoomBookInfo

@Dao
interface RoomBookInfoDao {
    @Insert
    fun insert(roomBookInfo: RoomBookInfo)

    @Update
    fun update(roomBookInfo: RoomBookInfo)

    @Delete
    fun delete(roomBookInfo: RoomBookInfo)

    @Query("SELECT * FROM RoomBookInfo")
    fun getAll() : List<RoomBookInfo>

    @Query("SELECT * FROM RoomBookInfo Where isbn = :isbn")
    fun getDataForIsbn(isbn : Long) : RoomBookInfo?
}