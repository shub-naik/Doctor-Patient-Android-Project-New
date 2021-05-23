package patient.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import application.ApplicationClass
import entities.AppointmentEnt
import kotlinx.coroutines.async
import relations.DoctorWithAvailabilities
import java.time.LocalDate

class BookingAppointmentViewModel(application: Application) : AndroidViewModel(application) {
    private val patientDao = (application as ApplicationClass).patientDao

    fun getAvailabilityOnADate(
        doctorId: Long,
        selectedDate: LocalDate
    ): LiveData<List<DoctorWithAvailabilities>> =
        patientDao.getAvailabilityOnADate(doctorId, selectedDate)

    fun alreadyBookedAvailabilityOnADate(
        doctorId: Long,
        selectedDate: LocalDate
    ): LiveData<List<AppointmentEnt>> =
        patientDao.getBookedAvailabilityOnADate(doctorId, selectedDate)

    suspend fun bookAnAppointment(
        appointmentEnt: AppointmentEnt
    ): Long {
        val jobResult = viewModelScope.async {
            return@async patientDao.bookAnAppointment(appointmentEnt)
        }
        return jobResult.await()
    }
}