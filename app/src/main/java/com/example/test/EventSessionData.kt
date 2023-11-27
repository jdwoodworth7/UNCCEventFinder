package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class EventSessionData (
    val eventId: String,
    val sessionDate : String,
    val startTime: String,
    val endTime: String,
    // val location: String
) : Parcelable
{
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        "",
        "",
        "",
        ""
    )

    // constructor for converting Firestore DocumentSnapshot to EventSessionData object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("eventId") ?: "",
        documentSnapshot.getString("sessionDate") ?: "",
        documentSnapshot.getString("startTime") ?: "",
        documentSnapshot.getString("endTime") ?: "",

    )

    // constructor for Parcelable EventSessionData object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    //Parcelable Implementations
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventId)
        parcel.writeString(sessionDate)
        parcel.writeString(startTime)
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