package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["patientPhone"], unique = true)])
data class PatientEnt(
    val patientName: String,
    val patientPhone: String,
    val patientPassword: String
) {
    @PrimaryKey(autoGenerate = true)
    var patientId: Long = 0L
}