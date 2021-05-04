package models

class Patient(
    val patientId: String,
    patientName: String,
    patientPhone: String,
    val patientPassword: String
) : Person(patientName, patientPhone)