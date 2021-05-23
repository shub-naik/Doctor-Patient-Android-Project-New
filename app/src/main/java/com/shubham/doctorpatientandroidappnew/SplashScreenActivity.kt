package com.shubham.doctorpatientandroidappnew

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import doctor.DoctorProfileActivity
import helperFunctions.getDoctorCredentials
import helperFunctions.getPatientCredentials
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
            // 1) For Patient
            val patientCredential = this.getPatientCredentials()

            // 2) For Doctor
            val doctorCredential = this.getDoctorCredentials()

            when {
                patientCredential != 0.toLong() -> {
                    startActivity(Intent(this, PatientMainActivity::class.java))
                    finish()
                }
                doctorCredential != 0.toLong() -> {
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