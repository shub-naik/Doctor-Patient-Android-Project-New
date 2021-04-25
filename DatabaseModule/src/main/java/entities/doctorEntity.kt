package entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["doctorPhone"], unique = true)])
data class doctorEntity(
    val doctorName: String,

    val doctorPhone: String,

    val doctorPassword: String,

    val doctorDegreeList: ArrayList<String>,

    val doctorAvailableTimingList: ArrayList<String>
) {
    @PrimaryKey(autoGenerate = true)
    private var doctorId: Int = 0
}