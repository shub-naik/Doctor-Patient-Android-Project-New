package models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Certification(
    val certificationName: String,
    val certificationValidity: Date
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(certificationName)
        parcel.writeLong(certificationValidity.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Certification> {
        override fun createFromParcel(parcel: Parcel): Certification {
            return Certification(parcel)
        }

        override fun newArray(size: Int): Array<Certification?> {
            return arrayOfNulls(size)
        }
    }

}