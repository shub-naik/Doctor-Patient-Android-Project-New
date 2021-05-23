package com.shubham.doctorpatientandroidappnew

import ADMIN_LOGIN_TITLE
import admin.ChooseRoleActivity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import appDarkModeState
import com.shubham.databasemodule.Database
import com.shubham.doctorpatientandroidappnew.databinding.ActivityMainBinding
import com.shubham.doctorpatientandroidappnew.databinding.AdminLoginLayoutBinding
import doctorPatientCommon.DoctorLoginActivity
import helperFunctions.getDarkModeSharedPreferences
import helperFunctions.getSupportActionBarView
import helperFunctions.getToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adminLoginDialogBinding: AdminLoginLayoutBinding
    private lateinit var dialog: Dialog
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mainMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        adminLoginDialogBinding =
            AdminLoginLayoutBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        this.getSupportActionBarView(ADMIN_LOGIN_TITLE)

//        // Checking Shared Preferences for Redirection to Respective Screens
//        // 1) For Patient
//        val patientSharedPreferences = getPatientSharedPreferences(this)
//        val patientCredential = patientSharedPreferences.getString(PATIENT_CREDENTIAL, null)
//        if (patientCredential != null) {
//            startActivity(Intent(this, PatientMainActivity::class.java))
//            finish()
//            return
//        }
//
//        // 2) For Doctor
//        val doctorSharedPreferences = getDoctorSharedPreferences(this)
//        val doctorCredential = doctorSharedPreferences.getString(DOCTOR_CREDENTIAL, null)
//        if (doctorCredential != null) {
//            startActivity(Intent(this, DoctorProfileActivity::class.java))
//            finish()
//            return
//        }

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
                    if (Database.adminCheck(username, password)) {
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

//        // Patient Login Button Click
//        binding.patientLoginBtn.setOnClickListener {
//            startActivityIntent("patient")
//        }
//
//        // Doctor Login Button Click
//        binding.doctorLoginBtn.setOnClickListener {
//            startActivityIntent("doctor")
//        }
    }

    private fun startActivityIntent(loginRoleType: String) {
        val intent = Intent(this, DoctorLoginActivity::class.java)
        intent.putExtra("LoginRoleType", loginRoleType)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        mainMenuItem = menu!!.findItem(R.id.LightNightIconSwitch)

        val darkModeSharedPref = getDarkModeSharedPreferences(this)
        if (darkModeSharedPref.getBoolean(appDarkModeState, false))
            mainMenuItem.setIcon(R.drawable.ic_baseline_wb_sunny_24)
        else
            mainMenuItem.setIcon(R.drawable.ic_baseline_dark_mode_24)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.LightNightIconSwitch -> {
                val darkModeSharedPref = getDarkModeSharedPreferences(this)
                val editor = darkModeSharedPref.edit()
                val isNightMode = darkModeSharedPref.getBoolean(appDarkModeState, false)
                if (!isNightMode) {
                    // changed to night mode , previous light mode
                    mainMenuItem.setIcon(R.drawable.ic_baseline_wb_sunny_24)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putBoolean(appDarkModeState, true)
                } else {
                    // Changed to light mode , previous night mode
                    mainMenuItem.setIcon(R.drawable.ic_baseline_dark_mode_24)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putBoolean(appDarkModeState, false)
                }
                editor.apply()
                editor.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
