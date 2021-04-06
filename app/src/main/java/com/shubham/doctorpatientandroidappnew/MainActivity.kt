package com.shubham.doctorpatientandroidappnew

import admin.ChooseRoleActivity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shubham.databasemodule.DataBase
import com.shubham.doctorpatientandroidappnew.databinding.ActivityMainBinding
import com.shubham.doctorpatientandroidappnew.databinding.AdminLoginLayoutBinding
import doctorPatientCommon.DoctorPatientLoginActivity
import helperFunctions.getPatientSharedPreferences
import helperFunctions.getToast
import patient.PatientMainActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adminLoginDialogBinding: AdminLoginLayoutBinding
    private lateinit var dialog: Dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        adminLoginDialogBinding =
            AdminLoginLayoutBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Checking Shared Preferences for Redirection to Respective Screens
        val sharedPreferences = getPatientSharedPreferences(this)
        val patientCredential =
            sharedPreferences.getString(getString(R.string.PatientCredential), null)
        if (patientCredential != null) {
            startActivity(Intent(this, PatientMainActivity::class.java))
            finish()
            return
        }

        // Progress Dialog For login purposes
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait ...")
        progressDialog.setMessage("Please Wait, Validating Data ...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Admin Login Dialog Starts Here
        dialog = Dialog(this)
        dialog.setContentView(adminLoginDialogBinding.root)
        dialog.window?.apply {
            attributes.windowAnimations = R.style.dialog_animation

            setBackgroundDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.dialog_background
                )
            )
            setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        dialog.setCancelable(false)
        adminLoginDialogBinding.CancelAdminLoginBtn.setOnClickListener {
            dialog.cancel()
        }
        adminLoginDialogBinding.AdminLoginBtn.setOnClickListener {
            val username = adminLoginDialogBinding.UsernameEt.editText?.text.toString()
            val password = adminLoginDialogBinding.PasswordEt.editText?.text.toString()
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                getToast(this, "All Fields are mandatory").show()
                if (TextUtils.isEmpty(username))
                    adminLoginDialogBinding.UsernameEt.error = "Username Required"
                if (TextUtils.isEmpty(password))
                    adminLoginDialogBinding.PasswordEt.error = "Password Required"
            } else {
                progressDialog.show()
                Handler().postDelayed({
                    if (DataBase.adminCheck(username, password)) {
                        dialog.cancel()
                        progressDialog.cancel()
                        val intent = Intent(this, ChooseRoleActivity::class.java)
                        startActivity(intent)
                    } else {
                        progressDialog.cancel()
                        getToast(this, "Invalid Admin Login").show()
                    }
                }, 3000)
            }
        }
        // Admin Login Ends Here

        // Show Admin Login Dialog on Admin Login
        binding.adminLoginBtn.setOnClickListener {
            dialog.show()
        }

        // Patient Login Button Click
        binding.patientLoginBtn.setOnClickListener {
            startActivityIntent("patient")
        }

        // Doctor Login Button Click
        binding.doctorLoginBtn.setOnClickListener {
            startActivityIntent("doctor")
        }
    }

    private fun startActivityIntent(loginRoleType: String) {
        val intent = Intent(this, DoctorPatientLoginActivity::class.java)
        intent.putExtra("LoginRoleType", loginRoleType)
        startActivity(intent)
    }

}
