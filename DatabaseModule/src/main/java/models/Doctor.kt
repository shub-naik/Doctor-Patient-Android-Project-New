package models

import android.os.Parcel
import android.os.Parcelable

class Doctor(
    val doctorId: String,
    doctorUsername: String,
    doctorPhone: String,
    val doctorPassword: String,
    val doctorDegreeList: ArrayList<String>,
    val doctorAvailableTimingList: ArrayList<String>
) :
    Person(doctorUsername, doctorPhone), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!
    )

    override fun equals(other: Any?): Boolean {
        if (other is Doctor) {
            if (doctorId == other.doctorId && personName == other.personName && personPhone == other.personPhone && doctorPassword == other.doctorPassword
                && doctorDegreeList.containsAll(other.doctorDegreeList) && other.doctorDegreeList.containsAll(
                    doctorDegreeList
                )
                && doctorAvailableTimingList.containsAll(other.doctorAvailableTimingList) && other.doctorAvailableTimingList.containsAll(
                    doctorAvailableTimingList
                )
            )
                return true
        }
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(doctorId)
        parcel.writeString(super.personName)
        parcel.writeString(super.personPhone)
        parcel.writeString(doctorPassword)
        parcel.writeStringList(doctorDegreeList)
        parcel.writeStringList(doctorAvailableTimingList)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun hashCode(): Int {
        var result = doctorId.hashCode()
        result = 31 * result + doctorPassword.hashCode()
        result = 31 * result + doctorDegreeList.hashCode()
        result = 31 * result + doctorAvailableTimingList.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Doctor> {
        override fun createFromParcel(parcel: Parcel): Doctor {
            return Doctor(parcel)
        }

        override fun newArray(size: Int): Array<Doctor?> {
            return arrayOfNulls(size)
        }
    }
}