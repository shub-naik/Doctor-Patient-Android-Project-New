package helperFunctions

import DOCTOR_CREDENTIAL
import PATIENT_CREDENTIAL
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.shubham.doctorpatientandroidappnew.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

fun Context.getAlertDialog(
    message: String,
    error_type: Int,
    cancellable: Boolean = true,
    positiveButtonClickCallback: (dialog: DialogInterface) -> Unit = {
        it.cancel()
    }
) {
    val builder = SweetAlertDialog(this, error_type)
    builder.apply {
        setCancelable(cancellable)
        titleText = if (error_type == SweetAlertDialog.ERROR_TYPE)
            getString(R.string.error)
        else
            getString(R.string.success)
        setConfirmButton(getString(R.string.Ok)) { dialog ->
            positiveButtonClickCallback.invoke(dialog)
        }
        contentText = message
        show()
    }
}

fun Context.getLinearLayoutManager() = LinearLayoutManager(this)

fun Context.getDoctorCredentials(): Long {
    val doctorPref = getDoctorSharedPreferences(this)
    return doctorPref.getLong(DOCTOR_CREDENTIAL, 0)
}

fun Context.getPatientCredentials(): Long {
    val doctorPref = getPatientSharedPreferences(this)
    return doctorPref.getLong(PATIENT_CREDENTIAL, 0)
}

fun getToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG): Toast =
    Toast.makeText(context, message, duration)

// 1 means VISIBLE , 2 means INVISIBLE , number other than 1 and 2 means GONE
fun makeVisibilityToGivenState(view: View, state: Int = 1) {
    view.visibility = when (state) {
        1 -> View.VISIBLE
        2 -> View.INVISIBLE
        else -> View.GONE
    }
}

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

fun getLocalDateObject(dateStr: String): LocalDate = LocalDate.parse(dateStr)

inline fun <reified T> Context.getDatePickerDialog(
    DateOfAppointmentView: T, viewTitle: String,
    crossinline callBackFun: (LocalDate) -> Unit = { }
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar[Calendar.YEAR]
    val currentMonth = calendar[Calendar.MONTH]
    val currentDate = calendar[Calendar.DATE]

    val datePickerDialog =
        DatePickerDialog(
            this,
            R.style.DialogTheme,
            { _, year, month, dayOfMonth ->
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

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun getPatientSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("PatientSharedPref", Context.MODE_PRIVATE)

fun getDoctorSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DoctorSharedPref", Context.MODE_PRIVATE)

fun getDarkModeSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("DarkModeSharedPref", Context.MODE_PRIVATE)
