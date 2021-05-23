package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["doctorPhone"], unique = true)])
data class DoctorEnt(val doctorName: String, val doctorPhone: String, val doctorPassword: String) {
    @PrimaryKey(autoGenerate = true)
    var doctorId: Long = 0L
}