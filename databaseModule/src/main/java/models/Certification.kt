package models

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class Certification(
    val certificationName: String,
    val graduatedIn: LocalDate
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        LocalDate.ofEpochDay(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(certificationName)
        parcel.writeLong(graduatedIn.toEpochDay())
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