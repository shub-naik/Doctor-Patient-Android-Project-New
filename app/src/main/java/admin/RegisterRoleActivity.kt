package admin

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityRegisterRoleBinding
import exceptions.Exceptions
import helperFunctions.getToast

class RegisterRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterRoleBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val intentData = intent.getStringExtra("register_type")

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait ...")
        progressDialog.setMessage("Please Wait, Validating Data ...")
        progressDialog.setCanceledOnTouchOutside(false)

        if (intentData != null) {
            // 2 possibilities - one for doctor and other for patient.
            when {
                intentData.equals("doctor", true) -> {
                    binding.roleHeadingTxtView.text = getString(R.string.doctor_signUp)
                    binding.RoleSignUpBtn.text = getString(R.string.doctor_signUp)

                    binding.RoleSignUpBtn.setOnClickListener {
                        val doctorUsername = binding.RoleSignUpUsernameEt.text.toString()
                        val doctorPhone = binding.RoleSignUpPhoneEt.text.toString()
                        val doctorPassword = binding.RoleSignUpPasswordEt.text.toString()
                        if (doctorUsername.isEmpty() || doctorPassword.isEmpty() || doctorPhone.isEmpty()) {
                            getToast(
                                this,
                                "All Fields Are Mandatory",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (doctorPhone.isEmpty())
                                binding.RoleSignUpPhoneEt.error = "Phone Number is Mandatory"
                            if (doctorPassword.isEmpty())
                                binding.RoleSignUpPasswordEt.error =
                                    "8 Characters Password is Mandatory"
                            if (doctorUsername.isEmpty())
                                binding.RoleSignUpUsernameEt.error = "Username is Mandatory"
                        } else {
                            progressDialog.show()
                            Handler().postDelayed({
                                addDoctorDataToDatabase(doctorUsername, doctorPhone, doctorPassword)
                            }, 3000)
                        }
                    }
                }
                intentData.equals("patient", true) -> {
                    binding.roleHeadingTxtView.text = getString(R.string.patient_signUp)
                    binding.RoleSignUpBtn.text = getString(R.string.patient_signUp)
                    binding.RoleSignUpBtn.setOnClickListener {
                        val doctorUsername = binding.RoleSignUpUsernameEt.text.toString()
                        val doctorPhone = binding.RoleSignUpPhoneEt.text.toString()
                        val doctorPassword = binding.RoleSignUpPasswordEt.text.toString()
                        if (doctorUsername.isEmpty() || doctorPassword.isEmpty() || doctorPhone.isEmpty()) {
                            getToast(
                                this,
                                "All Fields Are Mandatory",
                                Toast.LENGTH_SHORT
                            ).show()
                            if (doctorPhone.isEmpty())
                                binding.RoleSignUpPhoneEt.error = "Phone Number is Mandatory"
                            if (doctorPassword.isEmpty())
                                binding.RoleSignUpPasswordEt.error =
                                    "8 Characters Password is Mandatory"
                            if (doctorUsername.isEmpty())
                                binding.RoleSignUpUsernameEt.error = "Username is Mandatory"
                        } else {
                            progressDialog.show()
                            Handler().postDelayed({
                                addPatientDataToDatabase(
                                    doctorUsername,
                                    doctorPhone,
                                    doctorPassword
                                )
                            }, 3000)
                        }
                    }
                }
            }
        }
    }

    private fun addDoctorDataToDatabase(
        doctorUsername: String,
        doctorPhone: String,
        doctorPassword: String
    ) {
        progressDialog.hide()
        try {
            DataBase.addDoctorToRegisteredDoctorList(
                mapOf(
                    "DoctorUsername" to "Dr. $doctorUsername",
                    "DoctorPhone" to doctorPhone,
                    "DoctorPassword" to doctorPassword
                )
            )
            getToast(
                this,
                "Doctor Data Saved Successfully"
            ).show()
            finish()
        } catch (exception: IllegalStateException) {
            getToast(
                this,
                exception.message.toString()
            ).show()
        } catch (exception: Exceptions) {
            getToast(
                this,
                exception.message.toString()
            ).show()
            binding.RoleSignUpPhoneEt.error =
                "User Already Exists With This Phone Number - $doctorPhone"
        }
    }

    private fun addPatientDataToDatabase(
        patientUsername: String,
        patientPhone: String,
        patientPassword: String
    ) {
        try {
            DataBase.addPatientToRegisteredDoctorList(
                mapOf(
                    "PatientUsername" to patientUsername,
                    "PatientPhone" to patientPhone,
                    "PatientPassword" to patientPassword
                )
            )
            getToast(
                this,
                "Patient Data Saved Successfully"
            ).show()
            finish()
        } catch (exception: IllegalStateException) {
            getToast(
                this,
                exception.message.toString()
            ).show()
        } catch (exception: Exceptions) {
            getToast(
                this,
                exception.message.toString()
            ).show()
            binding.RoleSignUpPhoneEt.error =
                "User Already Exists With This Phone Number - $patientPhone"
        }
        progressDialog.hide()
    }
}