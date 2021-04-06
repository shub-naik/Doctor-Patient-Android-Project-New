package models

class Patient(
    private val patientId: String,
    patientName: String,
    patientPhone: String,
    val patientPassword: String
) : Person(patientName, patientPhone)