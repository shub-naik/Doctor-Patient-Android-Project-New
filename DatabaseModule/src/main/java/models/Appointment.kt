package models

import java.time.LocalDate

data class Appointment(
    val patient: String,
    val doctor: Doctor,
    val dateOfAppointment: LocalDate,
    val timeOfAppointment: String
)