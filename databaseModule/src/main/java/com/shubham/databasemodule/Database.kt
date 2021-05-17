package com.shubham.databasemodule

import exceptions.Exceptions
import helperFunctions.getUid
import models.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class Database {
    companion object {
        val doctorDegreeList =
            listOf(
                "Dentist",
                "Surgeon",
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

        fun getRandomDegreeList(): ArrayList<Certification> {
            val l = arrayListOf<Certification>()
            for (i in 1..5) {
                l.add(
                    Certification(
                        doctorDegreeList[Random.nextInt(doctorDegreeList.size)],
                        Date()
                    )
                )
            }
            return l
        }

        // Admin Login Check
        fun adminCheck(username: String, password: String) =
            username == "a" && password == "a"

        // For registering Doctor
        private val registeredDoctorList = mutableListOf<Doctor>()
        fun getRegisteredDoctorList() = registeredDoctorList.toList()

        fun addDoctorToRegisteredDoctorList(dataMap: Map<String, Any>) {
            val docList =
                registeredDoctorList.filter { it.personPhone == dataMap["DoctorPhone"] }.toList()
            if (docList.isNotEmpty())
                throw Exceptions("Doctor Already Exists With This Phone Number - ${dataMap["DoctorPhone"]}")

            val doctor = Doctor(
                getUid(),
                dataMap["DoctorUsername"].toString(),
                dataMap["DoctorPhone"].toString(),
                dataMap["DoctorPassword"].toString(),
                ArrayList((dataMap["DoctorDegreeList"] as ArrayList<*>).filterIsInstance<Certification>()),
                HashMap()
            )
            registeredDoctorList.add(doctor)
        }

        fun getDoctorWithCredentials(doctorCredentials: String): Doctor? =
            try {
                registeredDoctorList.filter { doctorCredentials == it.personPhone }[0]
            } catch (exception: Exception) {
                null
            }

        fun getPatientWithCredentials(patientCredential: String): Patient? {
            val patientList = registeredPatientList.filter { patientCredential == it.personPhone }
            if (patientList.isNotEmpty() && patientList.size == 1)
                return patientList[0]
            return null
        }

        fun updateDegreeDetails(doctor: Doctor, certificationList: ArrayList<Certification>) {
            doctor.doctorDegreeList.addAll(certificationList)
        }

        fun updateDoctorTimingDetails(
            doctorCredentials: String,
            availableDateTimeMap: HashMap<LocalDate, ArrayList<AvailableTimingSlot>>
        ) {
            val doctor = getDoctorWithCredentials(doctorCredentials)
            doctor?.doctorAvailableDateTimeMap?.putAll(availableDateTimeMap)
        }

        // For registering Patient
        private val registeredPatientList = mutableListOf<Patient>()
        fun addPatientToRegisteredList(dataMap: Map<String, String>): Boolean {
            val patList =
                registeredPatientList.filter { it.personPhone == dataMap["PatientPhone"] }.toList()
            if (patList.isNotEmpty())
                throw Exceptions("Patient Already Exists With This Phone Number - ${dataMap["PatientPhone"]}")
            val patient = Patient(
                getUid(),
                dataMap["PatientUsername"] ?: error("Username Required"),
                dataMap["PatientPhone"] ?: error("Phone Number Required"),
                dataMap["PatientPassword"] ?: error("Password Required")
            )
            return registeredPatientList.add(patient)
        }

        // Doctor - Patient Login Check
        fun checkDoctorLogin(phone: String, password: String) =
            registeredDoctorList.filter { it.personPhone == phone && it.doctorPassword == password }.size == 1

        fun checkPatientLogin(phone: String, password: String) =
            registeredPatientList.filter { it.personPhone == phone && it.patientPassword == password }.size == 1

        private val appointmentList = ArrayList<Appointment>()
        fun getAllAppointmentOnADate(doctor: Doctor, date: LocalDate) =
            appointmentList.filter {
                it.appointmentDetails.doctor.doctorId == doctor.doctorId && it.appointmentDetails.dateOfAppointment == date
            }

        private fun getCurrentDateTimePair(): Pair<LocalDate, LocalTime> {
//            val dateFormat1 = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//            val dateFormat2 = SimpleDateFormat("HH:mm", Locale.US)
//            val date = Date()
            val todayDateStr = LocalDate.now()
            val todayTimeStr = LocalTime.now()

            return Pair(todayDateStr, todayTimeStr)
        }

        fun getAllRespectiveAppointmentForPatient(
            patientCredential: String,
            pastOrUpcoming: Int // Past means 1 and Upcoming means any number other than 1
        ): List<Appointment> {
            val patient = getPatientWithCredentials(patientCredential)

            if (patient != null) {
                val value = getCurrentDateTimePair()
                val todayDate = value.first
                val currentTime = value.second
                return if (pastOrUpcoming == 1) {
                    appointmentList.filter {
                        it.appointmentDetails.patient.patientId == patient.patientId
                                &&
                                (it.appointmentDetails.dateOfAppointment.isBefore(todayDate) ||
                                        (it.appointmentDetails.dateOfAppointment.isEqual(todayDate) && it.appointmentDetails.timeOfAppointment.isBefore(
                                            currentTime
                                        )))
                    }
                } else {
                    appointmentList.filter {
                        it.appointmentDetails.patient.patientId == patient.patientId &&
                                (it.appointmentDetails.dateOfAppointment.isAfter(todayDate) ||
                                        (it.appointmentDetails.dateOfAppointment.isEqual(todayDate) && it.appointmentDetails.timeOfAppointment.isAfter(
                                            currentTime
                                        )))
                    }
                }
            } else
                return listOf()
        }

        private fun alreadyAppointmentBooked(appointment: Appointment): Boolean {
            for (currentApp in appointmentList) {
                val currentAppointmentDetails = currentApp.appointmentDetails
                val checkerAppointmentDetails = appointment.appointmentDetails
                if (currentAppointmentDetails.doctor.doctorId == checkerAppointmentDetails.doctor.doctorId &&
                    currentAppointmentDetails.dateOfAppointment.toString() == checkerAppointmentDetails.dateOfAppointment.toString()
                    && currentAppointmentDetails.timeOfAppointment.toString() == checkerAppointmentDetails.timeOfAppointment.toString()
                )
                    return true
            }
            return false
        }

        fun saveAppointmentDataForPatient(appointment: Appointment) =
            if (!alreadyAppointmentBooked(appointment))
                appointmentList.add(appointment)
            else
                false
    }
}

// Unused Code
//private var initialIndex = 0
//private const val partsSize = 10
//fun getPartsOfRegisteredDoctorList(): List<Doctor> {
//    val l: List<Doctor>
//    if (initialIndex == DataBase.registeredDoctorList.size)
//        return listOf()
//    if (initialIndex + partsSize > DataBase.registeredDoctorList.size) {
//        // Partial BreakDown of the original list
//        l = DataBase.registeredDoctorList.subList(initialIndex, DataBase.registeredDoctorList.size)
//        initialIndex = DataBase.registeredDoctorList.size
//    } else {
//        l = DataBase.registeredDoctorList.subList(initialIndex, partsSize + initialIndex)
//        initialIndex += partsSize
//    }
//    return l
//}