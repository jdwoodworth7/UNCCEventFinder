package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot

data class UserEventsData (
    val id: String,
    val userId: String,
    val eventId: String, //unique identifier from EventDat data class
    val eventSessionId: String //unique identifier from EventSessionData data class
) : Parcelable
{
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        "",
        "",
        "",
        "",
    )

    // constructor for converting Firestore DocumentSnapshot to UserEventsData object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("id") ?: "",
        documentSnapshot.getString("userId") ?: "",
        documentSnapshot.getString("eventId") ?: "",
        documentSnapshot.getString("eventSessionId") ?: "",
    )

    // constructor for Parcelable UserData object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(eventId)
        parcel.writeString(eventSessionId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEventsData> {
        override fun createFromParcel(parcel: Parcel): UserEventsData {
            return UserEventsData(parcel)
        }

        override fun newArray(size: Int): Array<UserEventsData?> {
            return arrayOfNulls(size)
        }
    }
}