package daos

import androidx.room.Insert
import androidx.room.Query
import entities.patientEntity

interface patientDao {
    @Insert
    suspend fun addPatient(patientEntity: patientEntity)

    @Query("SELECT * FROM patientEntity where patientPhone=:patientPhone and patientPassword=:patientPassword")
    suspend fun doctorLogin(patientPhone: String, patientPassword: String): patientEntity

}