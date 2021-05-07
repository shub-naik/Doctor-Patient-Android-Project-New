package patient.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shubham.databasemodule.DataBase
import models.Doctor
import models.FilterModel

class AvailableDoctorsViewModel : ViewModel() {
    private val availableDoctorsList = mutableListOf<Doctor>()
    private val availableDoctorsLiveData = MutableLiveData<MutableList<Doctor>>()

    fun loadDegreeDetailsForAdapter() =
        DataBase.doctorDegreeList.map { FilterModel(it) }.toList()

    fun getAllAvailableDoctorsList(): LiveData<MutableList<Doctor>> {
        //        for (i in 1..20) {
//            availableDoctorsList.add(
//                Doctor(
//                    "$i",
//                    "$i",
//                    "$i",
//                    "$i",
//                    DataBase.getRandomDegreeList(),
//                    HashMap()
//                )
//            )
//        }
        availableDoctorsList.addAll(ArrayList(DataBase.getRegisteredDoctorList()))
        availableDoctorsLiveData.value = availableDoctorsList
        return availableDoctorsLiveData
    }

    fun restoreAvailableDoctorsList(): LiveData<MutableList<Doctor>> {
        availableDoctorsLiveData.value = availableDoctorsList
        return availableDoctorsLiveData
    }

    fun filterAvailableDoctorData(filters: List<String>): LiveData<MutableList<Doctor>> {
        val l = availableDoctorsList.filter { it ->
            it.doctorDegreeList.any { it.certificationName in filters }
        }.toMutableList()
        Log.e("VModel", "filterAvailableDoctorData: ${l.size}")
        availableDoctorsLiveData.value = l
        return availableDoctorsLiveData
    }
}