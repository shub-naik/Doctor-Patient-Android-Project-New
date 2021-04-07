package com.shubham.databasemodule

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import exceptions.Exceptions
import helperFunctions.getUid
import models.Appointment
import models.Doctor
import models.Patient
import java.time.LocalDate
import kotlin.random.Random

class DataBase {
    companion object {
        // Dummy Data for Doctor Degree
        private val dummyDoctorDegreeList =
            listOf(
                "MBBS",
                "MD(Res)",
                "MD",
                "PhD",
                "DPhil",
                "MCM",
                "MMSc",
                "MMedSc",
                "MM",
                "MMed",
                "MPhil",
                "MS", "MSc", "MSurg", "DSurg", "DS"
            )

//        private val dummyDoctorsTimingList = listOf<Timing>(
//            Timing("12:00pm"),
//            Timing("12:30pm"),
//            Timing("01:00pm"),
//            Timing("01:30pm"),
//            Timing("02:00pm"),
//            Timing("02:30pm"),
//            Timing("03:00pm"),
//            Timing("03:30pm"),
//            Timing("04:00pm"),
//            Timing("04:30pm"),
//            Timing("05:00pm"),
//            Timing("05:30pm"),
//            Timing("06:00pm"),
//            Timing("06:30pm"),
//            Timing("07:00pm"),
//            Timing("08:00pm"),
//            Timing("08:30pm"),
//            Timing("09:00pm"),
//            Timing("09:30pm")
//        )

        private val dummyDoctorsTimingList = listOf(
            "12:00pm",
            "12:30pm",
            "01:00pm",
            "01:30pm",
            "02:00pm",
            "02:30pm",
            "03:00pm",
            "03:30pm",
            "04:00pm",
            "04:30pm",
            "05:00pm",
            "05:30pm",
            "06:00pm",
            "06:30pm",
            "07:00pm",
            "08:00pm",
            "08:30pm",
            "09:00pm",
            "09:30pm"
        )

        // For Getting Random Degree to a Doctor
        private fun getRandomDegreeList(): List<String> {
            val degreeList = mutableListOf<String>()
            for (i in 1..5) {
                val randomInt = Random.nextInt(0, dummyDoctorDegreeList.size)
                degreeList.add(dummyDoctorDegreeList[randomInt])
            }
            return degreeList
        }

        private fun getRandomAvailableTimings(): List<String> {
            val timingList = mutableListOf<String>()
            for (i in 1..10) {
                val randomInt = Random.nextInt(0, dummyDoctorsTimingList.size)
                timingList.add(dummyDoctorsTimingList[randomInt])
            }
            Log.e("DatabaseLog", "getRandomAvailableTimings: $timingList")
            return timingList
        }

        // Admin Login Check
        fun adminCheck(username: String, password: String) =
            username == "admin" && password == "admin"

//        private val nextIndex = 0

        // For registering Doctor
        private val registeredDoctorList = mutableListOf<Doctor>()
        fun getRegisteredDoctorList() = registeredDoctorList.toList()

//        fun getPartsOfRegisteredDoctorList()

        fun addDoctorToRegisteredDoctorList(dataMap: Map<String, String>) {
            val docList =
                registeredDoctorList.filter { it.personPhone == dataMap["DoctorPhone"] }.toList()
            if (docList.isNotEmpty())
                throw Exceptions("Doctor Already Exists With This Phone Number - ${dataMap["DoctorPhone"]}")
            val doctor = Doctor(
                getUid(),
                dataMap["DoctorUsername"] ?: error("Username Required"),
                dataMap["DoctorPhone"] ?: error("Phone Number Required"),
                dataMap["DoctorPassword"] ?: error("Password Required"),
                getRandomDegreeList(),
                getRandomAvailableTimings()
            )
            registeredDoctorList.add(doctor)
        }

        // For registering Patient
        private val registeredPatientList = mutableListOf<Patient>()
        fun addPatientToRegisteredDoctorList(dataMap: Map<String, String>) {
            val docList =
                registeredPatientList.filter { it.personPhone == dataMap["PatientPhone"] }.toList()
            if (docList.isNotEmpty())
                throw Exceptions("Patient Already Exists With This Phone Number - ${dataMap["PatientPhone"]}")
            val patient = Patient(
                getUid(),
                dataMap["PatientUsername"] ?: error("Username Required"),
                dataMap["PatientPhone"] ?: error("Phone Number Required"),
                dataMap["PatientPassword"] ?: error("Password Required")
            )
            registeredPatientList.add(patient)
        }

        // Doctor - Patient Login Check
        fun checkDoctorLogin(phone: String, password: String) =
            registeredDoctorList.filter { it.personPhone == phone && it.doctorPassword == password }.size == 1

        fun checkPatientLogin(phone: String, password: String) =
            registeredPatientList.filter { it.personPhone == phone && it.patientPassword == password }.size == 1

        // Fake , Dummy Data for Doctor
        private var counter = 1
        fun getDummyAvailableDoctorData(): ArrayList<Doctor> {
            val l = ArrayList<Doctor>()
            for (i in 1..8) {
                val id = getUid()
                l.add(
                    Doctor(
                        id,
                        id,
                        "$counter",
                        "$i",
                        getRandomDegreeList(),
                        getRandomAvailableTimings()
                    )
                )
                counter++
            }
            return l
        }

        private val appointmentMap = mutableMapOf<String, ArrayList<Appointment>>()
        fun getAppointmentDetails(patientCredential: String): Pair<String, ArrayList<Appointment>> {
            if (appointmentMap[patientCredential] != null)
                return Pair("Result", appointmentMap[patientCredential]!!)
            return Pair("EmptyList", arrayListOf())
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun saveAppointmentDetails(mapData: Map<String, Any>) {
            val patientAppointmentList =
                appointmentMap.getOrDefault(mapData["patientCredentials"] as String, ArrayList())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (patientAppointmentList.isEmpty()) {
                    appointmentMap[mapData["patientCredentials"] as String] = patientAppointmentList
                }
                patientAppointmentList.add(
                    Appointment(
                        mapData["patientCredentials"] as String,
                        mapData["selectedDoctor"] as Doctor,
                        mapData["selectedDate"] as LocalDate,
                        mapData["selectedTime"] as String
                    )
                )
            }
        }
    }
}