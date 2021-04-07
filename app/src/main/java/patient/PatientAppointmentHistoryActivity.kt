package patient

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientAppointmentHistoryBinding
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import patient.adapters.AppointmentHistoryAdapter

class PatientAppointmentHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAppointmentHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAppointmentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getPatientSharedPreferences(this)
        val patientCredential = sharedPref.getString(getString(R.string.PatientCredential), null)
        if (patientCredential == null) {
            getToast(this, "Some Error Occurred !!!")
            finish()
            return
        }

        val appointmentDetails = DataBase.getAppointmentDetails(patientCredential)
        if (appointmentDetails.first.equals("EmptyList", true)) {
            getToast(this, "You Haven't done any appointment yet !!!").show()
            finish()
            return
        }

        val appointmentList = appointmentDetails.second
        val appointHistoryRecyclerView = binding.AppointmentHistoryRecyclerView
        val adapter = AppointmentHistoryAdapter(appointmentList)
        appointHistoryRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(this)
        appointHistoryRecyclerView.layoutManager = manager
    }
}