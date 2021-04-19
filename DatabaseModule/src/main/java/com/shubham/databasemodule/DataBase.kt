package com.shubham.databasemodule

import android.os.Build
import androidx.annotation.RequiresApi
import exceptions.Exceptions
import helperFunctions.getUid
import models.*
import java.time.LocalDate
import kotlin.random.Random

class DataBase {
    companion object {
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

        private var counter = 1
        fun createDummyDocData() {
            for (i in 1..20) {
                registeredDoctorList.add(
                    Doctor(
                        "$i", "$counter", "$i", "$i",
                        ArrayList<String>(),
                        getRandomDegreeList
                            ()
                    )
                )
                counter++
            }
        }

        // For Getting Random Degree to a Doctor
        private fun getRandomDegreeList(): ArrayList<String> {
            val degreeList = ArrayList<String>()
            for (i in 1..5) {
                val randomInt = Random.nextInt(0, dummyDoctorDegreeList.size)
                degreeList.add(dummyDoctorDegreeList[randomInt])
            }
            return degreeList
        }

        // Admin Login Check
        fun adminCheck(username: String, password: String) =
            username == "a" && password == "a"

        // For registering Doctor
        private val registeredDoctorList = mutableListOf<Doctor>()
        fun getRegisteredDoctorList() = registeredDoctorList.toList()

        private var initialIndex = 0
        private const val partsSize = 10
        fun getPartsOfRegisteredDoctorList(): List<Doctor> {
            val l: List<Doctor>
            if (initialIndex == registeredDoctorList.size)
                return listOf()
            if (initialIndex + partsSize > registeredDoctorList.size) {
                // Partial BreakDown of the original list
                l = registeredDoctorList.subList(initialIndex, registeredDoctorList.size)
                initialIndex = registeredDoctorList.size
            } else {
                l = registeredDoctorList.subList(initialIndex, partsSize + initialIndex)
                initialIndex += partsSize
            }
            return l
        }

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
                ArrayList()
            )
            registeredDoctorList.add(doctor)
        }

        fun getDoctorWithCredentials(doctorCredentials: String) =
            getRegisteredDoctorList().filter { doctorCredentials == it.personPhone }[0]

        fun updateDoctorTimingDetails(doctorCredentials: String, timingList: ArrayList<String>) {
            val doctor = getDoctorWithCredentials(doctorCredentials)
            doctor.doctorAvailableTimingList.clear()
            doctor.doctorAvailableTimingList.addAll(timingList)
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

        private val appointmentMap = mutableMapOf<String, ArrayList<Appointment>>()
        fun getAppointmentDetails(patientCredential: String): Pair<String, List<Appointment>> {
            if (appointmentMap[patientCredential] != null)
                return Pair("Result", appointmentMap[patientCredential]!!.toList())
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
                        AppointmentDetails(
                            mapData["patientCredentials"] as String,
                            mapData["selectedDoctor"] as Doctor,
                            mapData["selectedDate"] as LocalDate,
                            mapData["selectedTime"] as String
                        ),
                        AppointmentDate(mapData["selectedDate"].toString())
                    )
                )
            }
        }
    }
}