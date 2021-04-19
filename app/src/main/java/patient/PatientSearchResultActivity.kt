package patient

import AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
import SELECTED_DOCTOR
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientSearchResultBinding
import helperFunctions.getToast
import models.Doctor
import patient.adapters.AvailableDoctorsAdapter
import patient.interfaces.AvailableDoctorItemInterface

class PatientSearchResultActivity : AppCompatActivity(), AvailableDoctorItemInterface {
    private lateinit var binding: ActivityPatientSearchResultBinding
    private val availableDoctorsAdapter: AvailableDoctorsAdapter by lazy {
        AvailableDoctorsAdapter(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.action) {
            Intent.ACTION_SEARCH -> {
                val query =
                    intent.getStringExtra(SearchManager.QUERY) // Comment it for now , no use for now but can be used for search from remote database
                val availableDoctorsList = intent.getParcelableArrayListExtra<Doctor>(
                    AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
                )!!

                availableDoctorsList.map {
                    Log.e("TpKing", "onCreate: ${it.personName}")
                }

                if (availableDoctorsList.isEmpty()) {
                    getToast(this, "Not Able To Find For the Related Query").show()
                    finish()
                }
                val recyclerView = binding.SearchResultRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(this)
                availableDoctorsAdapter.setAvailableDoctorsAdapterData(availableDoctorsList)
                recyclerView.adapter = availableDoctorsAdapter
            }
            else -> {
                getToast(this, "Not the Intended Action To Perform").show()
                finish()
            }
        }
    }

    override fun onItemClick(doctor: Doctor) {
        val intent = Intent(this, PatientBookAppointmentActivity::class.java)
        intent.putExtra(SELECTED_DOCTOR, doctor)
        startActivity(intent)
    }
}