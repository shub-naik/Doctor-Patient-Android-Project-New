package relations

import androidx.room.Embedded
import entities.DoctorEnt
import java.time.LocalDate
import java.time.LocalTime

data class DateTimePojo(
    val dateOfAppointment: LocalDate,
    val timeOfAppointment: LocalTime,
    val duration: Int
)

data class PatientWithAppointments(
    val appointmentId: Long,
    val patientId: Long,
    @Embedded val doctor: DoctorEnt,
    @Embedded val dateTimePojo: DateTimePojo
)