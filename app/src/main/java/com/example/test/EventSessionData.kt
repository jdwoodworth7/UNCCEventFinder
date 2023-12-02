package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot

data class EventSessionData(
    val startDate: String,
    val startTime: String,
    val endDate: String,
    val endTime: String
) : Parcelable {
    // No-argument constructor for Firestore deserialization
    constructor() : this("", "", "", "")

    // constructor for converting Firestore DocumentSnapshot to EventSessionData object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("startDate") ?: "",
        documentSnapshot.getString("startTime") ?: "",
        documentSnapshot.getString("endDate") ?: "",
        documentSnapshot.getString("endTime") ?: ""
    )

    // constructor for Parcelable EventSessionData object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    // Parcelable Implementations
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startDate)
        parcel.writeString(startTime)
        parcel.writeString(endDate)
        parcel.writeString(endTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventSessionData> {
        override fun createFromParcel(parcel: Parcel): EventSessionData {
            return EventSessionData(parcel)
        }

        override fun newArray(size: Int): Array<EventSessionData?> {
            return arrayOfNulls(size)
        }
    }
}