package patient.interfaces

import models.Doctor

interface AvailableDoctorItemInterface {
    fun onItemClick(doctor: Doctor)
}