package admin

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityRegisterRoleBinding
import exceptions.Exceptions
import helperFunctions.getDateObject
import helperFunctions.getDatePickerDialog
import helperFunctions.getToast
import helperFunctions.showSoftKeyboard
import models.Certification

class RegisterRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterRoleBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For Doctor Degree DropDown
        val degreeList = DataBase.doctorDegreeList
        val dropdownAdapter = ArrayAdapter(this, R.layout.doctor_degree_dropdown_item, degreeList)
        binding.AutoCompleteDegreeDropDown.setText(degreeList[0])
        binding.AutoCompleteDegreeDropDown.setAdapter(dropdownAdapter)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait ...")
        progressDialog.setMessage("Please Wait, Validating Data ...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.roleHeadingTxtView.text = getString(R.string.doctor_signUp)
        binding.RoleSignUpBtn.text = getString(R.string.doctor_signUp)

        handleClickListeners()

        showSoftKeyboard(binding.RoleSignUpUsernameEt)
    }

    private fun handleClickListeners() {
        binding.RoleSignUpBtn.setOnClickListener {
            val doctorUsername = binding.RoleSignUpUsernameEt.text.toString()
            val doctorPhone = binding.RoleSignUpPhoneEt.text.toString()
            val doctorPassword = binding.RoleSignUpPasswordEt.text.toString()
            val doctorDegreeName = binding.AutoCompleteDegreeDropDown.text.toString()
            val degreeExpiryDate = binding.DoctorDegreeExpiryDateBtn.text.toString()

            // string.xml variables
            val expString = resources.getString(R.string.select_date_of_doctor_degree_expiry)

            if (doctorUsername.isEmpty() || doctorPassword.isEmpty() || doctorPhone.isEmpty() || doctorDegreeName.isEmpty() || degreeExpiryDate.equals(
                    expString, true
                )
            ) {
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
                if (degreeExpiryDate.equals(expString, false))
                    binding.DoctorDegreeExpiryDateBtn.error =
                        "Degree Expiry Date has to be selected"
            } else {
                progressDialog.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    addDoctorDataToDatabase(
                        doctorUsername,
                        doctorPhone,
                        doctorPassword,
                        doctorDegreeName,
                        degreeExpiryDate
                    )
                    progressDialog.hide()
                }, 3000)
            }
        }

        binding.DoctorDegreeExpiryDateBtn.setOnClickListener {
            this.getDatePickerDialog<MaterialButton>(
                binding.DoctorDegreeExpiryDateBtn,
                getString(R.string.select_date_of_doctor_degree_expiry)
            )
        }
    }

    private fun addDoctorDataToDatabase(
        doctorUsername: String,
        doctorPhone: String,
        doctorPassword: String,
        doctorDegree: String,
        degreeExpiresIn: String
    ) {
        progressDialog.hide()
        try {
            val degreeList = arrayListOf(
                Certification(
                    doctorDegree,
                    getDateObject(degreeExpiresIn)
                )
            )

            DataBase.addDoctorToRegisteredDoctorList(
                mapOf(
                    "DoctorUsername" to doctorUsername,
                    "DoctorPhone" to doctorPhone,
                    "DoctorPassword" to doctorPassword,
                    "DoctorDegreeList" to degreeList
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
}