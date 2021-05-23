package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(indices = [Index(value = ["doctorId", "certificationName"], unique = true)])
data class CertificationEnt(
    val doctorId: Long,
    val certificationName: String,
    val graduationDate: LocalDate
) {
    @PrimaryKey(autoGenerate = true)
    var certificationId: Long = 0
}