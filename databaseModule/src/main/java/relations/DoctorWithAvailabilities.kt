package relations

import androidx.room.Embedded
import androidx.room.Relation
import entities.AvailabilityEnt
import entities.DoctorEnt

data class DoctorWithAvailabilities(
    @Embedded val availabilities: AvailabilityEnt,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "doctorId",
        entity = DoctorEnt::class
    )
    val doctor: DoctorDetails
)