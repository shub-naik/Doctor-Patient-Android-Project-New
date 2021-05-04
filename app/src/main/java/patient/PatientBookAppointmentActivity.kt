package patient

import NON_AUTHORIZED_USER
import PATIENT_BOOK_APPOINTMENT_TITLE
import PATIENT_CREDENTIAL
import SELECTED_DOCTOR
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientBookAppointmentBinding
import com.shubham.doctorpatientandroidappnew.databinding.FutureDateItemBinding
import helperFunctions.*
import models.Appointment
import models.AppointmentDetails
import models.Doctor
import models.Patient
import patient.adapters.AvailableDoctorTimingAdapter
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class PatientBookAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientBookAppointmentBinding
    private lateinit var selectedDoctor: Doctor
    private lateinit var adapter: AvailableDoctorTimingAdapter
    private lateinit var patient: Patient
    private lateinit var availableTimeList: ArrayList<LocalTime>

    private lateinit var bookingBtn: MaterialButton

    private var dateOfAppointment = LocalDate.now().minusDays(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingBtn = binding.BookAppointmentBtnForPatient
        bookingBtn.isEnabled = false

        val sharedPreferences = getPatientSharedPreferences(this)
        val prefValue = sharedPreferences.getString(PATIENT_CREDENTIAL, "")
        if (!prefValue.isNullOrEmpty()) {
            val pat = DataBase.getPatientWithCredentials(prefValue)
            if (pat != null)
                patient = pat
            else
                closeActivity()
        } else
            closeActivity()

        this.getSupportActionBarView(PATIENT_BOOK_APPOINTMENT_TITLE)

        val intent = intent
        try {
            selectedDoctor = intent.getParcelableExtra(SELECTED_DOCTOR)!!
        } catch (exception: Exception) {
            this.closeActivityWithToast("No Doctor Found")
            return
        }

        binding.DoctorProfileUsernameForPatient.text =
            getString(R.string.doctor_name, selectedDoctor.personName)

        handleDatePickerDialog()

        // Handle RecyclerView Layout Manager.
        binding.DoctorAvailableTimingsRecyclerViewForPatient.layoutManager =
            GridLayoutManager(
                this,
                3
            )
        binding.DoctorAvailableTimingsRecyclerViewForPatient.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        // handle Future Dates for Booking i.e 3 days from now and today date is inclusive
        handleFutureDates()
    }

    private fun handleFutureDates() {
        val currentDate = LocalDate.now()
        for (i in 0..2) {
            val view = FutureDateItemBinding.inflate(LayoutInflater.from(this))
            val btn = view.FutureDateBtn
            val nextDate = currentDate.plusDays(i.toLong())
            btn.text = nextDate.toString()
            btn.setOnClickListener {
                dateOfAppointment = nextDate
                getAvailabilityOnADate(nextDate)
            }
            binding.FutureDatesLayout.addView(view.root)
        }
    }

    private fun closeActivity() {
        getToast(this, NON_AUTHORIZED_USER).show()
        finish()
    }

    private fun getAvailabilityOnADate(selectedDate: LocalDate) {
        availableTimeList = arrayListOf()
        val dateTimeMap = selectedDoctor.doctorAvailableDateTimeMap
        if (dateTimeMap.containsKey(selectedDate)) {
            val value = dateTimeMap[selectedDate]!!
            for (v in value) {
                val startTime = v.fromTime
                val endTime = v.toTime
                val slotDuration = v.slotDuration
                val numSlots =
                    (Duration.between(startTime, endTime).seconds / 60) / slotDuration
                for (i in 0 until numSlots) {
                    availableTimeList.add(startTime.plusMinutes(slotDuration * i))
                }
            }

            // availableTimeList will contain all time slots on that particular date, so i have to filter out those which are previously booked.
            val alreadyBookedOnADate =
                DataBase.getAllAppointmentOnADate(selectedDoctor, selectedDate)
            for (a in alreadyBookedOnADate) {
                availableTimeList.remove(a.appointmentDetails.timeOfAppointment)
            }

            if (availableTimeList.isEmpty()) {
                binding.NoTimeAvailableTxtView.visibility = View.VISIBLE
                val displayText =
                    "No Slots Are There For The Selected Date - $selectedDate"
                binding.NoTimeAvailableTxtView.text = displayText
                hideTimeSlotRecyclerView()

                // Disable Booking Btn Also if no slots are available
                bookingBtn.isEnabled = false
            } else {
                bookingBtn.isEnabled = true

                hideNoSlotAvailableView()
                adapter =
                    AvailableDoctorTimingAdapter(
                        availableTimeList
                    )
                binding.DoctorAvailableTimingsRecyclerViewForPatient.adapter = adapter
                showTimeSlotRecyclerView()
            }
        } else {
            hideTimeSlotRecyclerView()
            showNoSlotAvailableView()
            bookingBtn.isEnabled = false
        }
    }

    private fun handleDatePickerDialog() {
        binding.DateOfAppointmentBtn.setOnClickListener {
            this.getDatePickerDialog(
                binding.DateOfAppointmentBtn,
                getString(R.string.select_date_of_appointment)
            ) {
                dateOfAppointment = it
                getAvailabilityOnADate(it)
            }
        }

        binding.BookAppointmentBtnForPatient.setOnClickListener {
            if (this::adapter.isInitialized && this::availableTimeList.isInitialized && availableTimeList.size > 0) {
                val timeSelected = adapter.getSelected()
                if (timeSelected != null) {
                    val checkStatus = DataBase.saveAppointmentDataForPatient(
                        Appointment(
                            AppointmentDetails(
                                patient,
                                selectedDoctor,
                                dateOfAppointment,
                                LocalTime.parse(timeSelected.toString())
                            )
                        )
                    )
                    if (checkStatus)
                        getToast(this, "Appointment Booked Successfully").show()
                    else
                        getToast(
                            this,
                            "$timeSelected  Slot is Unavailable Now, It's Booked"
                        ).show()
                } else
                    getToast(this, "Please Select The Time of Appointment").show()
            } else
                getToast(this, "Unable To Book the Appointment Due to Unavailability").show()
        }
    }

    private fun showNoSlotAvailableView() {
        binding.NoTimeAvailableTxtView.visibility = View.VISIBLE
    }

    private fun showTimeSlotRecyclerView() {
        binding.DoctorAvailableTimingsRecyclerViewForPatient.visibility = View.VISIBLE
    }

    private fun hideTimeSlotRecyclerView() {
        binding.DoctorAvailableTimingsRecyclerViewForPatient.visibility = View.GONE
    }

    private fun hideNoSlotAvailableView() {
        binding.NoTimeAvailableTxtView.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}