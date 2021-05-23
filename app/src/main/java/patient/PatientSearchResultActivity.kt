package patient

import AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
import SELECTED_DOCTOR_ID
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.doctorpatientandroidappnew.R
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
                val availableDoctorsList = intent.getParcelableArrayListExtra<Doctor>(
                    AVAILABLE_DOCTORS_SEARCH_LIST_CONSTANT
                )!!

                if (availableDoctorsList.isEmpty()) {
                    getToast(this, getString(R.string.not_found)).show()
                    finish()
                }
                val recyclerView = binding.SearchResultRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(this)
                availableDoctorsAdapter.setAvailableDoctorsAdapterData(availableDoctorsList)
                recyclerView.adapter = availableDoctorsAdapter
            }
            else -> {
                getToast(this, getString(R.string.error_302)).show()
                finish()
            }
        }
    }

    override fun onItemClick(doctor: Doctor) {
        val intent = Intent(this, PatientBookAppointmentActivity::class.java)
        intent.putExtra(SELECTED_DOCTOR_ID, doctor)
        startActivity(intent)
    }
}