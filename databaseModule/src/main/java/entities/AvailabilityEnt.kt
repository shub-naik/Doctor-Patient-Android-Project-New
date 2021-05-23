package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    indices = [Index(
        value = ["doctorId", "availableDate", "fromTime", "toTime"],
        unique = true
    )]
)
data class AvailabilityEnt(
    val doctorId: Long,
    val availableDate: LocalDate,
    val fromTime: LocalTime,
    val toTime: LocalTime,
    val slotDuration: Int
) {
    @PrimaryKey(autoGenerate = true)
    var availabilityId: Long = 0
}