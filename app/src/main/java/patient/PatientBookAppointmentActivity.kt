package patient

import NON_AUTHORIZED_USER
import PATIENT_BOOK_APPOINTMENT_TITLE
import SELECTED_DOCTOR_ID
import SELECTED_DOCTOR_NAME
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityPatientBookAppointmentBinding
import com.shubham.doctorpatientandroidappnew.databinding.FutureDateItemBinding
import entities.AppointmentEnt
import helperFunctions.*
import kotlinx.coroutines.launch
import patient.adapters.AvailableDoctorTimingAdapter
import patient.viewModels.BookingAppointmentViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

class PatientBookAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPatientBookAppointmentBinding
    private lateinit var adapter: AvailableDoctorTimingAdapter
    private val availableTimeList by lazy { arrayListOf<LocalTime>() }
    private var doctorId: Long = 0
    private var patCredential: Long = 0
    private lateinit var bookingBtn: MaterialButton
    private var previousBtn: MaterialButton? = null
    private var dateOfAppointment = LocalDate.now().minusDays(1)

    private var isPageLoadedForFirstTime = true

    private val context by lazy { this }

    private lateinit var viewModel: BookingAppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookingBtn = binding.BookAppointmentBtnForPatient
        bookingBtn.isEnabled = false
        this.getSupportActionBarView(PATIENT_BOOK_APPOINTMENT_TITLE)

        doctorId = intent.getLongExtra(SELECTED_DOCTOR_ID, 0)

        patCredential = this.getPatientCredentials()
        if (!(patCredential > 0 || doctorId > 0)) {
            closeActivity()
            return
        }

        viewModel = ViewModelProvider(this).get(BookingAppointmentViewModel::class.java)

        binding.DoctorProfileUsernameForPatient.text =
            getString(R.string.doctor_name, intent.getStringExtra(SELECTED_DOCTOR_NAME))

        handleDatePickerDialog()

        // handle Future Dates for Booking i.e 3 days from now and today date is inclusive
        handleFutureDates()

        // Handle RecyclerView Layout Manager.
        binding.AvailableTimingsRcyView.layoutManager =
            GridLayoutManager(
                this,
                3
            )
        binding.AvailableTimingsRcyView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        binding.ChooseTimingTxtViewForUser.text = getString(R.string.choose_the_date_of_appointment)

        // Get the Booking for the current date by default
        getAvailabilityOnADate(LocalDate.now())
    }

    private fun getAvailabilityOnADate(selectedDate: LocalDate) {


        viewModel.getAvailabilityOnADate(doctorId, selectedDate).observe(this, Observer { it ->
            availableTimeList.clear()
            if (it.isNotEmpty()) {
                for (v in it) {
                    val value = v.availabilities
                    val startTime = value.fromTime
                    val endTime = value.toTime
                    val slotDuration = value.slotDuration
                    val numSlots =
                        (Duration.between(startTime, endTime).seconds / 60) / slotDuration
                    for (i in 0 until numSlots) {
                        availableTimeList.add(startTime.plusMinutes(slotDuration * i))
                    }
                }
                viewModel.alreadyBookedAvailabilityOnADate(doctorId, selectedDate).observe(this,
                    Observer {
                        for (a in it) {
                            availableTimeList.remove(a.timeOfAppointment)
                        }
                        if (availableTimeList.isEmpty()) {
                            binding.NoTimeAvailableTxtView.visibility = View.VISIBLE
                            val displayText =
                                getString(R.string.no_slots_available, selectedDate.toString())
                            binding.ChooseTimingTxtViewForUser.text = displayText
                            binding.NoTimeAvailableTxtView.text = displayText
                            hideTimeSlotRecyclerView()

                            // Disable Booking Btn Also if no slots are available
                            bookingBtn.isEnabled = false
                        } else {
                            bookingBtn.isEnabled = true
                            hideNoSlotAvailableView()
                            this.adapter =
                                AvailableDoctorTimingAdapter(
                                    availableTimeList
                                )
                            binding.ChooseTimingTxtViewForUser.text =
                                getString(R.string.choose_the_time_of_appointment)
                            binding.AvailableTimingsRcyView.adapter = this.adapter
                            showTimeSlotRecyclerView()
                        }
                    })
            } else {
                binding.ChooseTimingTxtViewForUser.text =
                    getString(R.string.no_slots_available, selectedDate.toString())
                hideTimeSlotRecyclerView()
                showNoSlotAvailableView()
                bookingBtn.isEnabled = false
            }
        })
    }

    private fun handleFutureDates() {
        val currentDate = LocalDate.now()
        for (i in 0..2) {
            val view = FutureDateItemBinding.inflate(LayoutInflater.from(this))
            val btn = view.FutureDateBtn
            val nextDate = currentDate.plusDays(i.toLong())
            btn.text = nextDate.toString()

            // Will only run for First Time i.e when page load.
            if (isPageLoadedForFirstTime) {
                isPageLoadedForFirstTime = false
                previousBtn = btn
                btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.selected
                    )
                )
            }

            btn.setOnClickListener {
                dateOfAppointment = nextDate
                getAvailabilityOnADate(nextDate)

                btn.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.selected
                    )
                )
                if (previousBtn != null) {
                    (previousBtn as MaterialButton).setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.purple_500
                        )
                    )
                }
                previousBtn = btn
            }
            binding.FutureDatesLayout.addView(view.root)
        }
    }

    private fun closeActivity() {
        getToast(this, NON_AUTHORIZED_USER).show()
        finish()
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
            if (this::adapter.isInitialized && availableTimeList.size > 0 && patCredential > 0) {
                val timeSelected = this.adapter.getSelected()
                if (timeSelected != null) {
                    val appointmentEnt = AppointmentEnt(
                        doctorId, patCredential, dateOfAppointment, timeSelected, 20
                    )
                    lifecycleScope.launch {
                        val bookingId = viewModel.bookAnAppointment(
                            appointmentEnt
                        )
                        context.getAlertDialog(
                            getString(
                                R.string.appointment_booked_status,
                                bookingId
                            ),
                            SweetAlertDialog.SUCCESS_TYPE,
                            false
                        ) { alertDialog ->
                            alertDialog.cancel()
                            finish()
                        }
                    }
                } else
                    this.getAlertDialog(
                        getString(R.string.timing_selection_required),
                        SweetAlertDialog.ERROR_TYPE
                    )
            } else
                this.getAlertDialog(
                    getString(R.string.unavailable_time),
                    SweetAlertDialog.ERROR_TYPE
                )
        }
    }

    private fun showNoSlotAvailableView() {
        binding.NoTimeAvailableTxtView.visibility = View.VISIBLE
    }

    private fun showTimeSlotRecyclerView() {
        binding.AvailableTimingsRcyView.visibility = View.VISIBLE
    }

    private fun hideTimeSlotRecyclerView() {
        binding.AvailableTimingsRcyView.visibility = View.GONE
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