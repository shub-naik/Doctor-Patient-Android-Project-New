package com.shubham.databasemodule

import exceptions.Exceptions
import helperFunctions.getUid
import models.Doctor
import models.Patient
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

        // For Getting Random Degree to a Doctor
        private fun getRandomDegreeList(): List<String> {
            val degreeList = mutableListOf<String>()
            for (i in 1..5) {
                val randomInt = Random.nextInt(0, dummyDoctorDegreeList.size)
                degreeList.add(dummyDoctorDegreeList[randomInt])
            }
            return degreeList
        }

        // Admin Login Check
        fun adminCheck(username: String, password: String) =
            username == "admin" && password == "admin"

        // For registering Doctor
        private val registeredDoctorList = mutableListOf<Doctor>()
        fun getRegisteredDoctorList() = registeredDoctorList.toList()
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
                getRandomDegreeList()
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
                l.add(Doctor(id, id, "$counter", "$i", getRandomDegreeList()))
                counter++
            }
            return l
        }
    }
}