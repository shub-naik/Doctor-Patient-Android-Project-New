package daos

import androidx.lifecycle.LiveData
import androidx.room.*
import entities.AvailabilityEnt
import entities.CertificationEnt
import entities.DoctorEnt

@Dao
interface DoctorDao {
    @Insert
    suspend fun insertDoctor(doctor: DoctorEnt): Long

    @Query("Select doctorId from DoctorEnt where doctorPhone =:doctorPhone and doctorPassword=:doctorPassword limit 1")
    suspend fun doctorLogin(doctorPhone: String, doctorPassword: String): List<Long>

    @Query("Select * from DoctorEnt where doctorId=:doctorId limit 1")
    fun getDoctorById(doctorId: Long): LiveData<List<DoctorEnt>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCertifications(certifications: List<CertificationEnt>): List<Long>

    @Insert
    suspend fun insertAvailabilities(availabilities: List<AvailabilityEnt>): List<Long>
}