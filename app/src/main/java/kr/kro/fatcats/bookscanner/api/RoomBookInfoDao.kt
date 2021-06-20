package kr.kro.fatcats.bookscanner.api

import androidx.room.*
import kr.kro.fatcats.bookscanner.model.ListInfo
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
    fun getAll() : List<ListInfo>

    @Query("SELECT * FROM RoomBookInfo Where isbn = :isbn")
    fun getDataForIsbn(isbn : Long) : ListInfo?

    @Query("DELETE FROM RoomBookInfo")
    fun deleteAll()

    @Query("DELETE FROM RoomBookInfo Where isbn = :isbn ")
    fun deleteItem(isbn: Long)

    @Query("UPDATE RoomBookInfo SET time = :time WHERE isbn = :isbn")
    fun updateTime(time : Long?,isbn: Long)

    @Query("UPDATE RoomBookInfo SET mod_date = :now WHERE isbn = :isbn")
    fun updateLastDate(now : String?,isbn: Long)
}