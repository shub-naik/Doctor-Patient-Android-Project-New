package doctor

import DOCTOR_CREDENTIAL
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.MainActivity
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorProfileBinding
import helperFunctions.getDoctorSharedPreferences
import helperFunctions.getToast
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorProfileBinding
    private val timingList = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getDoctorSharedPreferences(this)
        val doctorCredential = sharedPref.getString(DOCTOR_CREDENTIAL, null)

        if (doctorCredential == null) {
            getToast(this, "Some Error Occurred In the App").show()
            finish()
            return
        }
        try {
            val doctor = DataBase.getDoctorWithCredentials(doctorCredential)
            binding.DoctorProfileUsername.text = doctor.personName
            binding.DoctorProfilePhoneNumber.text = doctor.personPhone
        } catch (exception: IndexOutOfBoundsException) {
            getToast(this, "Some DataBase File Error Occurred In the App").show()
        }

        binding.AddNewTimingBtn.setOnClickListener {
            addDynamicView()
        }

        binding.DoctorSaveProfileBtn.setOnClickListener {
            if (checkIfValidAndStoreData()) {
                DataBase.updateDoctorTimingDetails(doctorCredential, timingList)
                getToast(this, "Timing Details Updated Successfully").show()
                finish()
            } else
                getToast(this, "All Time Picker Fields are Mandatory").show()
        }
    }

    private fun checkIfValidAndStoreData(): Boolean {
        var result = true
        timingList.clear()
        val dynamicLinearLayout = binding.DynamicDoctorProfileLinearLayout
        val itemCount = binding.DynamicDoctorProfileLinearLayout.childCount
        for (i in 0 until itemCount) {
            val view = dynamicLinearLayout.getChildAt(i)
            val openTimePickerBtn = view.findViewById<Button>(R.id.OpenTimePickerItemBtn)
            val timeStringValue = openTimePickerBtn.text.toString()
            if (timeStringValue.equals(getString(R.string.select_time), true)) {
                view.findViewById<TextView>(R.id.ValidationErrorItemTxtView).visibility =
                    View.VISIBLE
                result = false
            } else {
                if (timeStringValue !in timingList)
                    timingList.add(timeStringValue)
                view.findViewById<TextView>(R.id.ValidationErrorItemTxtView).visibility =
                    View.GONE
            }
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDynamicView() {
        val view = layoutInflater.inflate(
            R.layout.add_new_timing_layout,
            binding.DynamicDoctorProfileLinearLayout,
            false
        )
        val openTimePickerBtn = view.findViewById<Button>(R.id.OpenTimePickerItemBtn)
        val deleteNewTimeItemBtn = view.findViewById<ImageButton>(R.id.DeleteTimeItemImgBtn)

        openTimePickerBtn.setOnClickListener {
            // Open TimePicker Dialog Here
            val calendar = Calendar.getInstance()
            val hour = calendar[Calendar.HOUR_OF_DAY]
            val minute = calendar[Calendar.MINUTE]

            val timePickerDialog =
                TimePickerDialog(
                    this,
                    R.style.DialogTheme,
                    OnTimeSetListener { _, hourOfDay, minuteOfDay ->
                        val timeObject = LocalTime.of(hourOfDay, minuteOfDay, 0)
                        openTimePickerBtn.text = timeObject.toString()
                        view.findViewById<TextView>(R.id.ValidationErrorItemTxtView).visibility =
                            View.GONE
                    }, hour, minute, true
                )
            timePickerDialog.show()
        }

        deleteNewTimeItemBtn.setOnClickListener {
            removeDynamicView(view)
        }

        binding.DynamicDoctorProfileLinearLayout.addView(view)
    }

    private fun removeDynamicView(view: View) {
        binding.DynamicDoctorProfileLinearLayout.removeView(view)
    }

    // Options Menu
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
                getToast(this, "Doctor Logout Selected").show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}