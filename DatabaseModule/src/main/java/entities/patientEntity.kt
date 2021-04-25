package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["patientPhone"], unique = true)])
data class patientEntity(
    val patientName: String,

    val patientPhone: String,

    val patientPassword: String
) {
    @PrimaryKey(autoGenerate = true)
    private var patientId: Int = 0
}