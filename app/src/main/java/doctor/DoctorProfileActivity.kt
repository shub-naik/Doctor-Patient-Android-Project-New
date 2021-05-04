package doctor

import DOCTOR_CREDENTIAL
import android.content.Intent
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
import com.google.android.material.button.MaterialButton
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorProfileBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddDateTimeSlotLayoutBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddNewDegreeItemBinding
import com.shubham.doctorpatientandroidappnew.databinding.AddTimeSlotBinding
import helperFunctions.*
import models.AvailableTimingSlot
import models.Certification
import models.Doctor
import patient.PatientLoginSignUpActivity
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.set

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorProfileBinding
    private lateinit var addNewDateTimeBtn: Button
    private lateinit var dateTimeLinearLayout: LinearLayout
    private val dateTimeSlotMap = HashMap<LocalDate, ArrayList<AvailableTimingSlot>>()
    private var doctorCredential: String? = null
    private val certificationList = arrayListOf<Certification>()
    private val degreeList = DataBase.doctorDegreeList

    private lateinit var doctorDegreeList: ArrayList<Certification>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateTimeLinearLayout = binding.AddDateTimingSlotsLayout
        addNewDateTimeBtn = binding.AddNewDateTimeBtn

        val sharedPref = getDoctorSharedPreferences(this)
        doctorCredential = sharedPref.getString(DOCTOR_CREDENTIAL, null)

        if (doctorCredential != null) {
            val doctor = DataBase.getDoctorWithCredentials(doctorCredential as String)
            if (doctor != null) {
                binding.DoctorProfileUsername.text = doctor.personName
                binding.DoctorProfilePhoneNumber.text = doctor.personPhone

                doctorDegreeList = doctor.doctorDegreeList

                handleClickListeners(doctor)
            } else
                finishActivity("Some DataBase Error Occurred In the App")
        } else
            finishActivity("Some Error Occurred In the App")
    }

    private fun finishActivity(message: String) {
        getToast(this, message).show()
        finish()
        return
    }

    private fun deleteDegreeView(view: View) {
        binding.DoctorDegreeLinearLayout.removeView(view)
    }

    private fun handleClickListeners(doctor: Doctor) {
//        dateTimeLinearLayout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//
//        }

        // For Adding New Degree Details
        binding.AddNewDegreeBtn.setOnClickListener {
            val view = AddNewDegreeItemBinding.inflate(LayoutInflater.from(this))

            view.DeleteDegreeBtn.setOnClickListener {
                deleteDegreeView(view.root)
            }

            view.DoctorDegreeExpDateBtn.setOnClickListener {
                getDatePickerDialog(view.DoctorDegreeExpDateBtn, "Select The Date of Exp")
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
                    val date = getDateObject(bind.DoctorDegreeExpDateBtn.text.toString())
                    val certification = Certification(degree, date)
                    if (!alreadyExistsCheck(certification))
                        certificationList.add(certification)
                    else
                        getToast(this, "Doctor Degree Already Exists").show()
                }
                DataBase.updateDegreeDetails(doctor, certificationList)
                binding.DoctorDegreeLinearLayout.removeAllViews()
                getToast(this, "Doctor Degree Updated Successfully").show()
            } else
                getToast(this, "No Degree Details Selected").show()
        }

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
                    // Save Date Time Slot if all goes fine.
                    if (doctorCredential != null) {
                        DataBase.updateDoctorTimingDetails(
                            doctorCredential.toString(),
                            dateTimeSlotMap
                        )
                        getToast(this, "Doctor Timings Slot Updated Successfully").show()
                    }

                    dateTimeLinearLayout.removeAllViews()
                } else
                    getToast(this, "Some Error in Your Date Time Values").show()
            } else
                getToast(this, "All Date Time Fields are Required.").show()
        }
    }

    private fun alreadyExistsCheck(certification: Certification): Boolean {
        if (this::doctorDegreeList.isInitialized) {
            doctorDegreeList.any { it.certificationName == certification.certificationName }
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

    //@TargetApi(Build.VERSION_CODES.O)     // To clear the doubt and edit it afterwards. It Can used be used with Date() Object
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
                } else {
                    dateTimeSlotMap[key]?.add(value)
                }
            }
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
                getToast(this, "Doctor Logout Successfully").show()
                startActivity(Intent(this, PatientLoginSignUpActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}