package models

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

class Doctor(
    val doctorId: String,
    doctorUsername: String,
    doctorPhone: String,
    val doctorPassword: String,
    val doctorDegreeList: ArrayList<Certification>, // Edited from String To Certification Class
    val doctorAvailableDateTimeMap: HashMap<LocalDate, ArrayList<AvailableTimingSlot>> // Edited from String to HashMap<LocalDate,ArrayList<AvailableTimingSlot>>()
) :
    Person(doctorUsername, doctorPhone), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Certification.CREATOR)!!,
        parcel.readSerializable() as HashMap<LocalDate, ArrayList<AvailableTimingSlot>>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(doctorId)
        parcel.writeString(super.personName)
        parcel.writeString(super.personPhone)
        parcel.writeString(doctorPassword)
        parcel.writeTypedList(doctorDegreeList)
        parcel.writeSerializable(doctorAvailableDateTimeMap)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doctor> {
        override fun createFromParcel(parcel: Parcel): Doctor {
            return Doctor(parcel)
        }

        override fun newArray(size: Int): Array<Doctor?> {
            return arrayOfNulls(size)
        }
    }

    override fun hashCode(): Int {
        var result = doctorId.hashCode()
        result = 31 * result + doctorPassword.hashCode()
        result = 31 * result + doctorDegreeList.hashCode()
        result = 31 * result + doctorAvailableDateTimeMap.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Doctor

        if (doctorId != other.doctorId) return false
        if (doctorPassword != other.doctorPassword) return false
        if (doctorDegreeList != other.doctorDegreeList) return false
        if (doctorAvailableDateTimeMap != other.doctorAvailableDateTimeMap) return false

        return true
    }
}
