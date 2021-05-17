package models

import java.time.LocalDate
import java.time.LocalTime

data class AppointmentDetails(
    val patient: Patient,
    val doctor: Doctor,
    val dateOfAppointment: LocalDate,
    val timeOfAppointment: LocalTime
)