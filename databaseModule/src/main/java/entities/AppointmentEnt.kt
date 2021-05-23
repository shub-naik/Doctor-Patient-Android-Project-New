package entities

import androidx.room.*
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    indices = [Index(
        value = ["doctorId", "dateOfAppointment", "timeOfAppointment"],
        unique = true
    )],
    foreignKeys = [
        ForeignKey(
            entity = DoctorEnt::class,
            parentColumns = ["doctorId"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PatientEnt::class,
            parentColumns = ["patientId"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppointmentEnt(
    @ColumnInfo(index = true)
    val doctorId: Long,

    @ColumnInfo(index = true)
    val patientId: Long,

    val dateOfAppointment: LocalDate,
    val timeOfAppointment: LocalTime,
    val duration: Int
) {
    @PrimaryKey(autoGenerate = true)
    var appointmentId: Long = 0L
}