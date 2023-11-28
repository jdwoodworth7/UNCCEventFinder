package com.example.test

import android.os.Parcel
import android.os.Parcelable

data class EventSession(
        val startDate: String,
        val startTime: String,
        val endDate: String,
        val endTime: String
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(startDate)
                parcel.writeString(startTime)
                parcel.writeString(endDate)
                parcel.writeString(endTime)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EventSession> {
                override fun createFromParcel(parcel: Parcel): EventSession {
                        return EventSession(parcel)
                }

                override fun newArray(size: Int): Array<EventSession?> {
                        return arrayOfNulls(size)
                }
        }
}