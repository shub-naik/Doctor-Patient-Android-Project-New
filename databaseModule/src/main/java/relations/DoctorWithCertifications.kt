package relations

import androidx.room.Embedded
import androidx.room.Relation
import entities.CertificationEnt

data class DoctorWithCertifications(
    @Embedded val doctor: DoctorDetails,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "doctorId"
    )
    val certifications: List<CertificationEnt>
)