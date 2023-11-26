package com.example.test

import android.os.Parcel
import android.os.Parcelable

//Data class representing filter criteria for event search
data class FilterData(
        val category_academic: Boolean,
        val category_social: Boolean,
        val category_sports: Boolean,
        val category_clubs: Boolean,
        val category_workshops: Boolean,
        val category_volunteering: Boolean,
        val category_students_only: Boolean
) : Parcelable {
        //Parcel constructor to create a FilterData instance from a Parcel.
        constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte(),
                parcel.readByte() != 0.toByte()
        )

        //Write object values to a Parcel
        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (category_academic) 1 else 0)
                parcel.writeByte(if (category_social) 1 else 0)
                parcel.writeByte(if (category_sports) 1 else 0)
                parcel.writeByte(if (category_clubs) 1 else 0)
                parcel.writeByte(if (category_workshops) 1 else 0)
                parcel.writeByte(if (category_volunteering) 1 else 0)
                parcel.writeByte(if (category_students_only) 1 else 0)
        }

        //Describe special types of objects contained in this Parcelable instance
        override fun describeContents(): Int {
                return 0
        }

        //Companion object implementing Parcelable.Creator for creating instances from Parcel
        companion object CREATOR : Parcelable.Creator<FilterData> {
                //Create a FilterData instance from a Parcel
                override fun createFromParcel(parcel: Parcel): FilterData {
                        return FilterData(parcel)
                }

                //Create a new array of the Parcelable class
                override fun newArray(size: Int): Array<FilterData?> {
                        return arrayOfNulls(size)
                }
        }
}