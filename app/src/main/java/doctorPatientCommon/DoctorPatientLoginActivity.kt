package doctorPatientCommon

import DOCTOR_CREDENTIAL
import PATIENT_CREDENTIAL
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityDoctorPatientLoginBinding
import doctor.DoctorProfileActivity
import doctorPatientCommon.dialogs.LoadingDialog
import helperFunctions.getDoctorSharedPreferences
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getSnackBar
import patient.PatientMainActivity
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class DoctorPatientLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorPatientLoginBinding
    private lateinit var backgroundExecutor: ScheduledExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorPatientLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // show Custom Dialog after clicking Login Button
        val loadingDialog = LoadingDialog(this)

        // Request Focus For Phone Number Field Automatically
        binding.RoleLoginPhoneEt.isFocusableInTouchMode = true
        binding.RoleLoginPhoneEt.requestFocus()

        // BackGround and Main Executor
        val mainExecutor: Executor = ContextCompat.getMainExecutor(this)
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

        // Get Intent Data for Respective Role Login
        val intent = intent
        val intentData = intent.getStringExtra("LoginRoleType")
        if (intentData != null) {
            // 2 possibilities - one for doctor and other for patient login.
            if (intentData.equals("patient", true)) {
                binding.roleLoginHeadingTxtView.text = getString(R.string.patient_login)
                binding.RoleLoginBtn.text = getString(R.string.patient_login)
                binding.RoleLoginBtn.setOnClickListener {
                    val phone = binding.RoleLoginPhoneEt.text.toString()
                    val password = binding.RoleLoginPasswordEt.text.toString()
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                        getSnackBar(
                            binding.SnackBarLinearLayout,
                            getString(R.string.empty_field_error)
                        ).show()
                        showEtError(phone, password)
                    } else {
                        loadingDialog.startLoadingDialog()
                        backgroundExecutor.schedule({
                            // Call Backend Method For Patient Login Check
                            val check = DataBase.checkPatientLogin(phone, password)
                            loadingDialog.stopLoadingDialog()

                            if (!check)
                                mainExecutor.execute {
                                    getSnackBar(
                                        binding.SnackBarLinearLayout,
                                        "Invalid Login Found !!!"
                                    ).show()
                                }
                            else {
                                // Save Data To Patient Shared Preferences
                                val sharedPreferences = getPatientSharedPreferences(this)
                                val editor = sharedPreferences.edit()
                                editor.putString(PATIENT_CREDENTIAL, phone)
                                editor.apply()
                                editor.commit()
                                startActivity(Intent(this, PatientMainActivity::class.java))
                                finish()
                            }
                        }, 3, TimeUnit.SECONDS)
                    }
                }
            } else if (intentData.equals("doctor", true)) {
                binding.roleLoginHeadingTxtView.text = getString(R.string.doctor_login)
                binding.RoleLoginBtn.text = getString(R.string.doctor_login)
                binding.RoleLoginBtn.setOnClickListener {
                    val phone = binding.RoleLoginPhoneEt.text.toString()
                    val password = binding.RoleLoginPasswordEt.text.toString()
                    if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                        getSnackBar(
                            binding.SnackBarLinearLayout,
                            getString(R.string.empty_field_error)
                        ).show()
                        showEtError(phone, password)
                    } else {
                        loadingDialog.startLoadingDialog()
                        backgroundExecutor.schedule({
                            // Call Backend Method For Doctor Login Check
                            val check = DataBase.checkDoctorLogin(phone, password)
                            loadingDialog.stopLoadingDialog()
                            if (!check)
                                mainExecutor.execute {
                                    getSnackBar(
                                        binding.SnackBarLinearLayout,
                                        "Invalid Login Found !!!"
                                    ).show()
                                }
                            else {
                                // Save Data To Doctor Shared Preferences
                                val sharedPreferences = getDoctorSharedPreferences(this)
                                val editor = sharedPreferences.edit()
                                editor.putString(DOCTOR_CREDENTIAL, phone)
                                editor.apply()
                                editor.commit()
                                startActivity(Intent(this, DoctorProfileActivity::class.java))
                                finish()
                            }
                        }, 3, TimeUnit.SECONDS)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
    }

    override fun onStop() {
        super.onStop()
        backgroundExecutor.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundExecutor.shutdown()
    }

    private fun showEtError(phone: String, password: String) {
        if (phone.isEmpty())
            binding.RoleLoginPhoneEt.error = "Phone Number is Mandatory"
        if (password.isEmpty())
            binding.RoleLoginPasswordEt.error =
                "8 Characters Password is Mandatory"
    }
}