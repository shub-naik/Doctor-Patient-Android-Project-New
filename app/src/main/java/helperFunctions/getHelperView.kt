package helperFunctions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.shubham.doctorpatientandroidappnew.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun Context.getLinearLayoutManager() = LinearLayoutManager(this)

fun getToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(context, message, duration)

fun getSnackBar(layout: View, message: String, duration: Int = Snackbar.LENGTH_LONG): Snackbar =
    Snackbar.make(
        layout,
        message,
        duration
    )

fun Context.getSupportActionBarView(
    title: String,
    setHomeAsEnabled: Boolean = true
): androidx.appcompat.app.ActionBar {
    val actionBarView = (this as AppCompatActivity).supportActionBar
    return actionBarView?.apply {
        if (setHomeAsEnabled) {
            setDisplayHomeAsUpEnabled(true)
        }
        this.title = title
    }!!
}

fun showSoftKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.getTimePickerDialog(timePickerView: MaterialButton, viewTitle: String) {
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePickerDialog =
        TimePickerDialog(
            this,
            R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfDay ->
                val timeObject = LocalTime.of(hourOfDay, minuteOfDay, 0)
                timePickerView.text = timeObject.toString()
            }, hour, minute, true
        )
    timePickerDialog.setCancelable(false)
    timePickerDialog.setTitle(viewTitle)
    timePickerDialog.show()
}

fun getDateObject(dateStr: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.parse(dateStr)!!
}

fun Context.closeActivityWithToast(message: String) {
    (this as AppCompatActivity).finish()
    getToast(this, message).show()
}

inline fun <reified T> Context.getDatePickerDialog(
    DateOfAppointmentView: T, viewTitle: String,
    crossinline callBackFun: ((LocalDate) -> Unit) = {}
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar[Calendar.YEAR]
    val currentMonth = calendar[Calendar.MONTH]
    val currentDate = calendar[Calendar.DATE]

    val datePickerDialog =
        DatePickerDialog(
            this,
            R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val dateStr = LocalDate.of(year, month + 1, dayOfMonth).toString()
                when (T::class) {
                    Button::class -> (DateOfAppointmentView as Button).text = dateStr
                    MaterialButton::class -> (DateOfAppointmentView as MaterialButton).text =
                        dateStr
                    else -> (DateOfAppointmentView as Button).text = dateStr
                }
                // Call CallBack after the date is selected for fetching the availability of the doctor on the selected date
                val dateObj = LocalDate.parse(dateStr)
                callBackFun.invoke(dateObj)
            }, currentYear, currentMonth, currentDate
        )
    datePickerDialog.setCancelable(false)
    datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis + 1000 * 60 * 60 * 24 * 14
    datePickerDialog.setTitle(viewTitle)
    datePickerDialog.show()
}

fun getPatientSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("PatientSharedPref", Context.MODE_PRIVATE)

fun getDoctorSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DoctorSharedPref", Context.MODE_PRIVATE)

fun getDarkModeSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DarkModeSharedPref", Context.MODE_PRIVATE)
