package models

import android.os.Parcel
import android.os.Parcelable

class Doctor(
    val doctorId: Long,
    doctorUsername: String,
    doctorPhone: String,
    val doctorDegreeList: List<Certification>
) :
    Person(doctorUsername, doctorPhone), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.createTypedArrayList(Certification.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(doctorId)
        parcel.writeString(super.personName)
        parcel.writeString(super.personPhone)
        parcel.writeTypedList(doctorDegreeList)
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
        result = 31 * result + doctorDegreeList.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Doctor

        if (doctorId != other.doctorId) return false
        if (doctorDegreeList != other.doctorDegreeList) return false

        return true
    }
}
