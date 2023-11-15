package com.example.test

import android.os.Parcel
import android.os.Parcelable

data class FilterData(
        val academic: Boolean,
        val social: Boolean,
        val clubsOrg: Boolean,
        val workshops: Boolean,
        val volunteering: Boolean,
        val studentsOnly: Boolean
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (academic) 1 else 0)
                parcel.writeByte(if (social) 1 else 0)
                parcel.writeByte(if (clubsOrg) 1 else 0)
                parcel.writeByte(if (workshops) 1 else 0)
                parcel.writeByte(if (volunteering) 1 else 0)
                parcel.writeByte(if (studentsOnly) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FilterData> {
                override fun createFromParcel(parcel: Parcel): FilterData {
                        return FilterData(parcel)
                }

                override fun newArray(size: Int): Array<FilterData?> {
                        return arrayOfNulls(size)
                }
        }
}