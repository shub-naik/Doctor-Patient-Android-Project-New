package patient

import PATIENT_BOOKING_HISTORY_TITLE
import PATIENT_CREDENTIAL
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientAppointmentHistoryBinding
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSupportActionBarView
import helperFunctions.getToast
import models.Appointment
import models.AppointmentDate
import patient.adapters.AppointmentDateItem
import patient.adapters.AppointmentDetailItem
import patient.adapters.AppointmentHistoryAdapter
import patient.adapters.AppointmentListItem


class PatientAppointmentHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientAppointmentHistoryBinding

    @SuppressLint("LongLogTag")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientAppointmentHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.getSupportActionBarView(PATIENT_BOOKING_HISTORY_TITLE)

        val sharedPref = getPatientSharedPreferences(this)
        val patientCredential = sharedPref.getString(PATIENT_CREDENTIAL, null)
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

        // Group Appointment Data According To Date
        val groupedHashMap = groupDataAccordingToDate(appointmentList)

        // Sort the above HashMap into the arrayList for adapter.
        val transformedAppointmentList = transformMapToSortedArrayList(groupedHashMap)

        val adapter = AppointmentHistoryAdapter(transformedAppointmentList, this)
        appointHistoryRecyclerView.adapter = adapter
        val manager = LinearLayoutManager(this)
        appointHistoryRecyclerView.layoutManager = manager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transformMapToSortedArrayList(groupedHashMap: Map<String, List<Appointment>>): List<AppointmentListItem> {
        val consolidatedList: ArrayList<AppointmentListItem> = ArrayList()
        for ((dateAsAKey, value) in groupedHashMap.entries) {
            consolidatedList.add(AppointmentDateItem(AppointmentDate(dateAsAKey)))
            consolidatedList.addAll(value.run { map { AppointmentDetailItem(it.appointmentDetails) }.toList() })
        }
        return consolidatedList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun groupDataAccordingToDate(appointmentList: List<Appointment>): Map<String, List<Appointment>> =
        appointmentList.groupBy { it.appointmentDate.dateOfAppointment }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
}