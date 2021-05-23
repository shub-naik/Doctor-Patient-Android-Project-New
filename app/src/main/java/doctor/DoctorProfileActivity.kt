package doctor

import DOCTOR_CREDENTIAL
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.shubham.databasemodule.Database
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorProfileBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddDateTimeSlotLayoutBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddNewDegreeItemBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddTimeSlotBinding
import entities.AvailabilityEnt
import entities.CertificationEnt
import entities.DoctorEnt
import helperFunctions.*
import kotlinx.coroutines.launch
import models.AvailableTimingSlot
import models.Certification
import patient.PatientLoginSignUpActivity
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.set

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorProfileBinding
    private lateinit var addNewDateTimeBtn: Button
    private lateinit var dateTimeLinearLayout: LinearLayout
    private val dateTimeSlotMap = HashMap<LocalDate, ArrayList<AvailableTimingSlot>>()
    private var doctorCredential: Long = 0
    private val certificationList = arrayListOf<CertificationEnt>()
    private val degreeList = Database.doctorDegreeList
    private val context: Context by lazy { this }
    private val doctorDao by lazy { (this.application as ApplicationClass).doctorDao }

    private lateinit var doctorDegreeList: ArrayList<Certification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Doctor Id From the Shared Pref.
        val sharedPref = getDoctorSharedPreferences(this)
        doctorCredential = sharedPref.getLong(DOCTOR_CREDENTIAL, 0)

        dateTimeLinearLayout = binding.AddDateTimingSlotsLayout
        addNewDateTimeBtn = binding.AddNewDateTimeBtn

        if (doctorCredential != 0.toLong()) {
            val doctorEnt = doctorDao.getDoctorById(doctorCredential)

            doctorEnt.observe(this, androidx.lifecycle.Observer {
                if (it.isNotEmpty()) {
                    val doctor = it[0]
                    binding.DoctorProfileUsername.text = doctor.doctorName
                    binding.DoctorProfilePhoneNumber.text = doctor.doctorPhone

                    handleClickListeners(doctor)
                } else
                    finishActivity(getString(R.string.error_500))
            })
        } else
            finishActivity(getString(R.string.app_error))
    }

    private fun finishActivity(message: String) {
        getToast(this, message).show()
        finish()
        return
    }

    private fun deleteDegreeView(view: View) {
        binding.DoctorDegreeLinearLayout.removeView(view)
    }

    private fun handleClickListeners(doctorEnt: DoctorEnt) {
        handleDegreeClickListeners(doctorEnt)
        handleTimingClickListeners()
    }

    private fun handleTimingClickListeners() {
        // For Adding New Date Time Slot.
        binding.AddNewDateTimeBtn.setOnClickListener {
            val outerLayout = AddDateTimeSlotLayoutBinding.inflate(LayoutInflater.from(this))
            val innerLayout = AddTimeSlotBinding.inflate(LayoutInflater.from(this))

            // DatePicker for the outerLayout
            outerLayout.OpenDatePickerItemBtn.setOnClickListener {
                this.getDatePickerDialog(
                    outerLayout.OpenDatePickerItemBtn,
                    getString(R.string.select_date_of_appointment)
                )
            }

            // for the very first time, inflate the add_time_slot after that only on the click of the add_new_btn
            innerLayout.fromTimePckBtn.setOnClickListener {
                this.getTimePickerDialog(innerLayout.fromTimePckBtn, "Select From Time : ")
            }
            innerLayout.toTimePckBtn.setOnClickListener {
                this.getTimePickerDialog(innerLayout.toTimePckBtn, "Select To Time : ")
            }
            outerLayout.addTimeSlotsLayout.addView(innerLayout.root)
            // Inner Layout Inflating for the First Time Ends Here

            outerLayout.addNewTimeSlotBtn.setOnClickListener {
                val siblingInnerLayout = AddTimeSlotBinding.inflate(LayoutInflater.from(this))
                siblingInnerLayout.fromTimePckBtn.setOnClickListener {
                    this.getTimePickerDialog(
                        siblingInnerLayout.fromTimePckBtn,
                        "Select From Time : "
                    )
                }
                siblingInnerLayout.toTimePckBtn.setOnClickListener {
                    this.getTimePickerDialog(siblingInnerLayout.toTimePckBtn, "Select To Time : ")
                }
                outerLayout.addTimeSlotsLayout.addView(siblingInnerLayout.root)
            }
            outerLayout.DeleteTimeItemImgBtn.setOnClickListener {
                removeDateTimeSlot(outerLayout.root)
            }

            dateTimeLinearLayout.addView(outerLayout.root)
        }

        // Save Date Time Data after Validation.
        binding.UpdateTimingDetailBtn.setOnClickListener {
            val count = dateTimeLinearLayout.childCount
            if (count > 0) {
                if (validateAndStoreData(count)) {
                    if (doctorCredential > 0) {
                        try {
                            val l = dateTimeSlotMap.entries.map { entry ->
                                entry.value.map {
                                    AvailabilityEnt(
                                        doctorCredential,
                                        entry.key,
                                        it.fromTime,
                                        it.toTime,
                                        it.slotDuration
                                    )
                                }
                            }.flatten()
                            lifecycleScope.launch {
                                doctorDao.insertAvailabilities(l)
                                getAlertDialog(
                                    getString(R.string.date_time_slot_updated),
                                    SweetAlertDialog.SUCCESS_TYPE
                                )
                            }
                        } catch (exception: SQLiteConstraintException) {
                            getAlertDialog(
                                getString(R.string.error_500),
                                SweetAlertDialog.ERROR_TYPE
                            )
                        }
                    }
                    dateTimeLinearLayout.removeAllViews()
                } else
                    getToast(this, getString(R.string.date_time_selection_error)).show()
            } else
                getAlertDialog(getString(R.string.date_time_required), SweetAlertDialog.ERROR_TYPE)
        }
    }

    private fun handleDegreeClickListeners(doctor: DoctorEnt) {
        binding.AddNewDegreeBtn.setOnClickListener {
            val view = AddNewDegreeItemBinding.inflate(LayoutInflater.from(this))

            view.DeleteDegreeBtn.setOnClickListener {
                deleteDegreeView(view.root)
            }

            view.DoctorDegreeExpDateBtn.setOnClickListener {
                getDatePickerDialog(
                    view.DoctorDegreeExpDateBtn,
                    getString(R.string.graduation_date)
                )
            }

            // For Doctor Degree DropDown
            val dropdownAdapter =
                ArrayAdapter(this, R.layout.doctor_degree_dropdown_item, degreeList)
            view.DoctorDegreeDrpDown.setText(degreeList[0])
            view.DoctorDegreeDrpDown.setAdapter(dropdownAdapter)

            binding.DoctorDegreeLinearLayout.addView(view.root)
        }

        // For Saving Doctor Degree Details
        binding.UpdateDegreeDetailsBtn.setOnClickListener {
            val parentLayout = binding.DoctorDegreeLinearLayout
            val count = parentLayout.childCount
            if (count > 0) {
                for (i in 0 until count) {
                    val view = parentLayout.getChildAt(i)
                    val bind = AddNewDegreeItemBinding.bind(view)
                    val degree = bind.DoctorDegreeDrpDown.text.toString()
                    val date = getLocalDateObject(bind.DoctorDegreeExpDateBtn.text.toString())
                    val certificationEnt = CertificationEnt(doctor.doctorId, degree, date)
                    if (!alreadyExistsCheck(certificationEnt))
                        certificationList.add(certificationEnt)
                }
                lifecycleScope.launch {
                    doctorDao.insertCertifications(certificationList.toList())
                    binding.DoctorDegreeLinearLayout.removeAllViews()
                    getAlertDialog(
                        getString(R.string.degree_details_saved),
                        SweetAlertDialog.SUCCESS_TYPE
                    )
                }
            } else
                getAlertDialog(
                    getString(R.string.degree_required),
                    SweetAlertDialog.ERROR_TYPE
                )
        }
    }

    private fun alreadyExistsCheck(certificationEnt: CertificationEnt): Boolean {
        if (this::doctorDegreeList.isInitialized) {
            doctorDegreeList.any { it.certificationName == certificationEnt.certificationName }
        }
        return false
    }

    private fun validateAndStoreData(count: Int): Boolean {
        dateTimeSlotMap.clear()
        var result = true
        for (index in 0 until count) {
            val outerLayout = dateTimeLinearLayout.getChildAt(index)

            val datePickerBtn = outerLayout.findViewById<MaterialButton>(R.id.OpenDatePickerItemBtn)
            val timeSlotLayout = outerLayout.findViewById<LinearLayout>(R.id.addTimeSlotsLayout)
            if (!datePickerBtn.text.toString()
                    .equals(resources.getString(R.string.select_date_of_appointment), true)
            ) {
                val innerLayoutsCount = timeSlotLayout.childCount
                for (i in 0 until innerLayoutsCount) {
                    val innerLayout = timeSlotLayout.getChildAt(i)
                    val fromTimePicker =
                        innerLayout.findViewById<MaterialButton>(R.id.fromTimePckBtn)
                    val toTimePicker = innerLayout.findViewById<MaterialButton>(R.id.toTimePckBtn)
                    val timeSlotEt = innerLayout.findViewById<EditText>(R.id.timeDurationEt)
                    if (!validateTimeSlotLayout(fromTimePicker, toTimePicker, timeSlotEt)) {
                        result =
                            storeDataInMap(datePickerBtn, fromTimePicker, toTimePicker, timeSlotEt)
                        if (!result)
                            return result
                    } else {
                        result = false
                        getToast(this, "Time Picker Error in ${index + 1}").show()
                    }
                }
            } else {
                result = false
                getToast(this, "Date Picker Error in ${index + 1} Item").show()
            }
        }
        return result
    }

    private fun storeDataInMap(
        datePickerBtn: MaterialButton,
        fromTimePickerBtn: MaterialButton,
        toTimePickerBtn: MaterialButton,
        timeSlotEt: EditText
    ): Boolean {
        val fromTime = LocalTime.parse(fromTimePickerBtn.text.toString())
        val toTime = LocalTime.parse(toTimePickerBtn.text.toString())
        val timeSlot = timeSlotEt.text.toString().toInt()

        val key = LocalDate.parse(datePickerBtn.text.toString())
        val value = AvailableTimingSlot(fromTime, toTime, timeSlot)

        if (key != null && dateTimeSlotMap.containsKey(key)) {
            val list = dateTimeSlotMap[key]!!
            for (i in 0 until list.size) {
                val obj = list[i]
                val checkInRange =
                    ((fromTime.isAfter(obj.fromTime) && fromTime.isBefore(obj.toTime)) || (toTime.isAfter(
                        obj.fromTime
                    ) && toTime.isBefore(obj.toTime)))
                if (checkInRange) {
                    getToast(this, "TimePicker Range Error").show()
                    return false
                }
            }
            dateTimeSlotMap[key]?.add(value)
        } else {
            val list = arrayListOf<AvailableTimingSlot>()
            list.add(value)
            dateTimeSlotMap[key] = list
        }
        return true
    }

    private fun validateTimeSlotLayout(
        fromTimePicker: MaterialButton,
        toTimePicker: MaterialButton,
        slotEt: EditText
    ): Boolean {
        return fromTimePicker.text.toString()
            .equals(getString(R.string.from_timepicker), true) || toTimePicker.text.toString()
            .equals(getString(R.string.from_timepicker), true) || slotEt.text.toString().isEmpty()
    }

    private fun removeDateTimeSlot(view: View) {
        dateTimeLinearLayout.removeView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.doctor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.DoctorLogoutMenuItem -> {
                val sharedPref = getDoctorSharedPreferences(this)
                sharedPref.edit().apply {
                    clear()
                    apply()
                }
                getToast(this, getString(R.string.roleLogout, "Doctor")).show()
                startActivity(Intent(this, PatientLoginSignUpActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}