package daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import entities.AppointmentEnt
import entities.PatientEnt
import relations.DoctorWithAvailabilities
import relations.DoctorWithCertifications
import relations.PatientWithAppointments
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: PatientEnt): Long

    @Query("Select patientId from PatientEnt where patientPhone =:patientPhone and patientPassword=:patientPassword")
    suspend fun patientLogin(patientPhone: String, patientPassword: String): List<Long>

    @Transaction
    @Query("Select * from DoctorEnt Group By doctorId")
    fun getAllDoctorWithCertifications(): LiveData<List<DoctorWithCertifications>>

    // Get the Availability
    @Transaction
    @Query("Select * from AvailabilityEnt where doctorId=:doctorId and availableDate=:date")
    fun getAvailabilityOnADate(
        doctorId: Long,
        date: LocalDate
    ): LiveData<List<DoctorWithAvailabilities>>

    // Book the Appointment By Patient.
    @Insert
    suspend fun bookAnAppointment(appointmentEnt: AppointmentEnt): Long

    // Get the Booked Availability
    @Transaction
    @Query("Select * from AppointmentEnt where doctorId=:doctorId and dateOfAppointment=:date")
    fun getBookedAvailabilityOnADate(
        doctorId: Long,
        date: LocalDate
    ): LiveData<List<AppointmentEnt>>

    // Get the Upcoming Appointments
    @Query("SELECT * FROM ((SELECT * FROM AppointmentEnt as app where app.patientId=:patientId and (dateOfAppointment > :currentDate  OR (dateOfAppointment = :currentDate AND timeOfAppointment > :currentTime))) as PatientResult) INNER JOIN DoctorEnt as doc ON doc.doctorId=PatientResult.doctorId")
    fun getUpcomingAppointmentsForPatientById(
        patientId: Long,
        currentDate: LocalDate,
        currentTime: LocalTime
    ): LiveData<List<PatientWithAppointments>>

    // Get the Past Appointments
    @Query("SELECT * FROM ((SELECT * FROM AppointmentEnt as app where app.patientId=:patientId and (dateOfAppointment < :currentDate  OR (dateOfAppointment = :currentDate AND timeOfAppointment < :currentTime))) as PatientResult) INNER JOIN DoctorEnt as doc ON doc.doctorId=PatientResult.doctorId")
    fun getPastAppointmentByPatientId(
        patientId: Long,
        currentDate: LocalDate,
        currentTime: LocalTime
    ): LiveData<List<PatientWithAppointments>>
}