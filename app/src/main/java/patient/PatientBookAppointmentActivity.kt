package patient

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientBookAppointmentBinding
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import helperFunctions.getToast
import models.Doctor
import patient.adapters.AvailableDoctorTimingAdapter
import java.time.LocalDate
import java.util.*

class PatientBookAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientBookAppointmentBinding
    private lateinit var adapter: AvailableDoctorTimingAdapter
    private lateinit var dateOfAppointmentObject: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val selectedDoctor = intent.getParcelableExtra<Doctor>("SelectedDoctor")
        if (selectedDoctor != null) {
            binding.DoctorProfileImgViewForPatient.setImageResource(R.drawable.ic_baseline_video_call_24)
            binding.DoctorProfileUsernameForPatient.text = selectedDoctor.personName

            binding.DoctorAvailableTimingsRecyclerViewForPatient.layoutManager = GridLayoutManager(
                this,
                2
            )
            binding.DoctorAvailableTimingsRecyclerViewForPatient.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )
            Log.e("PatientBookApp", "onCreate: ${selectedDoctor.doctorAvailableTimingList}")
            adapter = AvailableDoctorTimingAdapter(this, selectedDoctor.doctorAvailableTimingList)
            binding.DoctorAvailableTimingsRecyclerViewForPatient.adapter = adapter

            // from Date Picker Dialog
            binding.DateOfAppointmentBtn.setOnClickListener {
                handleDatePickerDialog()
            }

            // Handle `Book an Appointment` Button onClick
            binding.BookAppointmentBtnForPatient.setOnClickListener {
                val timeSelected = adapter.getSelected()
                if (this::dateOfAppointmentObject.isInitialized && timeSelected != null) {
                    val sharedPref = getPatientSharedPreferences(this)
                    val patientCredential =
                        sharedPref.getString(getString(R.string.PatientCredential), null)
                    if (patientCredential != null) {
                        val mapData = mapOf(
                            "patientCredentials" to patientCredential,
                            "selectedDoctor" to selectedDoctor,
                            "selectedDate" to dateOfAppointmentObject,
                            "selectedTime" to timeSelected
                        )
                        DataBase.saveAppointmentDetails(mapData)
                        getToast(this, "Appointment Data Saved Successfully !!!").show()
                        finish()
                    } else
                        getSnackBar(
                            binding.SnackBarLayout,
                            "Some Error Occurred in your app , please try again after some time"
                        ).show()
                } else
                    getSnackBar(
                        binding.SnackBarLayout,
                        "Date and Time has to be Selected inorder to book an appointment"
                    ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH]
        val currentDate = calendar[Calendar.DATE]

        val datePickerDialog =
            DatePickerDialog(
                this,
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dateOfAppointmentObject = LocalDate.of(year, month + 1, dayOfMonth)
                    binding.DateOfAppointmentBtn.text = dateOfAppointmentObject.toString()
                }, currentYear, currentMonth, currentDate
            )
        datePickerDialog.setCancelable(false)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis + 1000 * 60 * 60 * 24 * 14
        datePickerDialog.setTitle(getString(R.string.select_date_of_appointment))
        datePickerDialog.show()
    }
}