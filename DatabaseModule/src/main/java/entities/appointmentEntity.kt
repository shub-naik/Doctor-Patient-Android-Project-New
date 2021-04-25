package entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class appointmentEntity(
    val doctorId: Int,
    val patientId: Int,
    val dateOfAppointment: String,
    val timeOfAppointment: String
) {
    @PrimaryKey(autoGenerate = true)
    private var appointmentId: Int = 0
}