package daos

import androidx.room.Query
import entities.appointmentEntity

interface appointmentDao {

//    (SELECT dpa.patientPhone,d.doctorPhone,d.doctorName,dpa.dateOfAppointment,dpa.timeOfAppointment
//    FROM DoctorPatientAppointment as dpa INNER JOIN doctor as d on dpa.doctorPhone=d.doctorPhone where patientPhone="1")

    @Query("SELECT * FROM appointmentEntity INNER JOIN doctorEntity as d on d.doctorId=appointmentEntity.doctorId where patientId=:patientId")
    suspend fun getAllAppointments(patientId: String): List<appointmentEntity>
}