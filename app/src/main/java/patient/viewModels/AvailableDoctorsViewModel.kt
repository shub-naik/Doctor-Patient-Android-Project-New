package patient.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import application.ApplicationClass
import com.shubham.databasemodule.Database
import models.FilterModel
import relations.DoctorWithCertifications

class AvailableDoctorsViewModel(application: Application) : AndroidViewModel(application) {
    private val availableDoctorsList = mutableListOf<DoctorWithCertifications>()
    private val availableDoctorsLiveData = MutableLiveData<List<DoctorWithCertifications>>()
    private val availableDegrees by lazy { Database.doctorDegreeList }
    private val patientDao = (application as ApplicationClass).patientDao

    fun updateAvailableDoctorList(l: List<DoctorWithCertifications>) =
        availableDoctorsList.addAll(l)

    fun loadDegreeDetailsForAdapter() = availableDegrees.map { FilterModel(it) }.toList()

    fun getAllAvailableDoctorsList() = patientDao.getAllDoctorWithCertifications()

    fun restoreAvailableDoctorsList(): LiveData<List<DoctorWithCertifications>> {
        availableDoctorsLiveData.value = availableDoctorsList
        return availableDoctorsLiveData
    }

    fun filterAvailableDoctorData(filters: List<String>): LiveData<List<DoctorWithCertifications>> {
        val l = availableDoctorsList.filter { it ->
            it.certifications.any { it.certificationName in filters }
        }.toList()
        availableDoctorsLiveData.value = l
        return availableDoctorsLiveData
    }
}