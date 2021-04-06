package doctor

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorProfileBinding
import java.time.LocalDate
import java.util.*

class DoctorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorProfileBinding

    // Date and Time Objects.
    private lateinit var fromDateObject: LocalDate
    private lateinit var toDateObject: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // from Date Picker Dialog
        binding.FromDateBtn.setOnClickListener {
            handleDatePickerDialog("Select From Date")
        }

        // to Date Picker Dialog
        binding.ToDateBtn.setOnClickListener {
            handleDatePickerDialog("Select To Date")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleDatePickerDialog(message: String) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH]
        val currentDate = calendar[Calendar.DATE]

        val datePickerDialog =
            DatePickerDialog(
                this,
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    if (message.equals("Select From Date", true)) {
                        fromDateObject = LocalDate.of(year, month + 1, dayOfMonth)
                        binding.FromDateBtn.text = fromDateObject.toString()
                    } else {
                        toDateObject = LocalDate.of(year, month + 1, dayOfMonth)
                        binding.ToDateBtn.text = toDateObject.toString()
                    }
                }, currentYear, currentMonth, currentDate
            )
        datePickerDialog.setCancelable(false)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis + 1000 * 60 * 60 * 24 * 14
        datePickerDialog.setTitle(message)
        datePickerDialog.show()
    }

}