package daos

import androidx.room.Insert
import androidx.room.Query
import entities.doctorEntity

interface doctorDao {
    @Insert
    suspend fun addDoctor(doctorEntity: doctorEntity)

    @Query("SELECT * FROM doctorEntity where doctorPhone=:doctorPhone and doctorPassword=:doctorPassword")
    suspend fun doctorLogin(doctorPhone: String, doctorPassword: String): doctorEntity

    @Query("UPDATE doctorEntity set doctorAvailableTimingList=:doctorAvailableTimingList where doctorId=:doctorId")
    suspend fun updateDoctorTimings(doctorId: Int, doctorAvailableTimingList: ArrayList<String>)

}