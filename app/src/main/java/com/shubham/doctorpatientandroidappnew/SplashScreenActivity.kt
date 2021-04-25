package com.shubham.doctorpatientandroidappnew

import DOCTOR_CREDENTIAL
import PATIENT_CREDENTIAL
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import doctor.DoctorProfileActivity
import helperFunctions.getDoctorSharedPreferences
import helperFunctions.getPatientSharedPreferences
import patient.PatientLoginSignUpActivity
import patient.PatientMainActivity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var backgroundExecutor: ScheduledExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        backgroundExecutor = Executors.newSingleThreadScheduledExecutor()
        backgroundExecutor.schedule({
            // Checking Shared Preferences for Redirection to Respective Screens
            // 1) For Patient
            val patientSharedPreferences = getPatientSharedPreferences(this)
            val patientCredential = patientSharedPreferences.getString(PATIENT_CREDENTIAL, null)
            if (patientCredential != null) {
                startActivity(Intent(this, PatientMainActivity::class.java))
                finish()
            }

            // 2) For Doctor
            val doctorSharedPreferences = getDoctorSharedPreferences(this)
            val doctorCredential = doctorSharedPreferences.getString(DOCTOR_CREDENTIAL, null)
            if (doctorCredential != null) {
                startActivity(Intent(this, DoctorProfileActivity::class.java))
                finish()
            }

//            if (patientCredential == null && doctorCredential == null) {
//                startActivity(Intent(this, PatientLoginSignUpActivity::class.java))
//                finish()
//            }

            when {
                patientCredential != null -> {
                    startActivity(Intent(this, PatientMainActivity::class.java))
                    finish()
                }
                doctorCredential != null -> {
                    startActivity(Intent(this, DoctorProfileActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this, PatientLoginSignUpActivity::class.java))
                    finish()
                }
            }
        }, 3, TimeUnit.SECONDS)
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundExecutor.shutdown()
    }
}