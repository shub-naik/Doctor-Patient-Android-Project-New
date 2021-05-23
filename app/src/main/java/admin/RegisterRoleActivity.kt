package admin

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import application.ApplicationClass
import com.shubham.databasemodule.Database
import com.shubham.doctorpatientandroidappnew.R
import com.shubham.doctorpatientandroidappnew.databinding.ActivityRegisterRoleBinding
import entities.CertificationEnt
import entities.DoctorEnt
import helperFunctions.getDatePickerDialog
import helperFunctions.getLocalDateObject
import helperFunctions.getToast
import helperFunctions.showSoftKeyboard
import kotlinx.coroutines.launch

class RegisterRoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterRoleBinding
    private val doctorDao by lazy { (this.application as ApplicationClass).doctorDao }
    private val context by lazy { this }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For Doctor Degree DropDown
        val degreeList = Database.doctorDegreeList
        val dropdownAdapter = ArrayAdapter(this, R.layout.doctor_degree_dropdown_item, degreeList)
        binding.AutoCompleteDegreeDropDown.setText(degreeList[0])
        binding.AutoCompleteDegreeDropDown.setAdapter(dropdownAdapter)

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
            val graduatedIn = binding.DoctorDegreeExpiryDateBtn.text.toString()

            // string.xml variables
            val expString = resources.getString(R.string.select_date_of_doctor_degree_expiry)

            if (doctorUsername.isEmpty() || doctorPassword.isEmpty() || doctorPhone.isEmpty() || doctorDegreeName.isEmpty() || graduatedIn.equals(
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
                if (graduatedIn.equals(expString, false))
                    binding.DoctorDegreeExpiryDateBtn.error =
                        "Degree Expiry Date has to be selected"
            } else {
                addDoctorDataToDatabase(
                    doctorUsername,
                    doctorPhone,
                    doctorPassword,
                    doctorDegreeName,
                    graduatedIn
                )
            }
        }

        binding.DoctorDegreeExpiryDateBtn.setOnClickListener {
            this.getDatePickerDialog(
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
        graduatedIn: String
    ) {
        lifecycleScope.launch {
            try {
                val id =
                    doctorDao.insertDoctor(DoctorEnt(doctorUsername, doctorPhone, doctorPassword))
                doctorDao.insertCertifications(
                    listOf(
                        CertificationEnt(
                            id,
                            doctorDegree,
                            getLocalDateObject(graduatedIn)
                        )
                    )
                )
                getToast(
                    context,
                    getString(R.string.signup_data_saved, "Doctor")
                ).show()
                finish()
            } catch (exception: SQLiteConstraintException) {
                val error = getString(
                    R.string.doctor_already_exists,
                    doctorPhone
                )
                getToast(
                    context,
                    error
                ).show()
                binding.RoleSignUpPhoneEt.error = error
            }
        }
    }
}