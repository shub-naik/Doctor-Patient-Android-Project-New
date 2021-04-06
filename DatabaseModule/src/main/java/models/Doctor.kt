package models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull

class Doctor(
    @NonNull val doctorId: String,
    @NonNull doctorUsername: String,
    @NonNull doctorPhone: String,
    @NonNull val doctorPassword: String,
    @NonNull val doctorDegreeList: List<String>
) :
    Person(doctorUsername, doctorPhone), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!
    )

    override fun equals(other: Any?): Boolean {
        if (other is Doctor) {
            if (doctorId == other.doctorId && personName == other.personName && personPhone == other.personPhone && doctorPassword == other.doctorPassword
                && doctorDegreeList.containsAll(other.doctorDegreeList) && other.doctorDegreeList.containsAll(
                    doctorDegreeList
                )
            )
                return true
        }
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(doctorId)
        parcel.writeString(doctorPassword)
        parcel.writeStringList(doctorDegreeList)
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
}