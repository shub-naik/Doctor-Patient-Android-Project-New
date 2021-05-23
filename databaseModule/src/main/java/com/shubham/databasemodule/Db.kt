package com.shubham.databasemodule

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import converters.LocalDateConverter
import converters.LocalTimeConverter
import daos.DoctorDao
import daos.PatientDao
import entities.*

@Database(
    entities = [
        DoctorEnt::class,
        CertificationEnt::class,
        PatientEnt::class,
        AppointmentEnt::class,
        AvailabilityEnt::class
    ],
    version =3
)
@TypeConverters(value = [LocalDateConverter::class, LocalTimeConverter::class])
abstract class Db : RoomDatabase() {
    abstract val doctorDao: DoctorDao
    abstract val patientDao: PatientDao

    companion object {
        @Volatile
        private var INSTANCE: Db? = null

        fun getInstance(context: Context): Db {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    Db::class.java,
                    "doctor_patient_db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}