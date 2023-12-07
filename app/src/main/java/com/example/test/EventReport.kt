package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class EventReport(
    val id: String,
    val eventAuthorId: String,
    val reportAuthorId: String,
    val reportedDate: String,
    val category: String,
    val details: String,
    val eventId: String,
    val eventName: String,
) : Parcelable {

    //No-argument constructor for Firestore deserialization

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )

    // constructor for converting Firestore DocumentSnapshot to EventReport object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("id") ?: "",
        documentSnapshot.getString("eventAuthorId") ?: "",
        documentSnapshot.getString("category") ?: "",
        documentSnapshot.getString("details") ?: "",
        documentSnapshot.getString("reportedDate") ?: "",
        documentSnapshot.getString("eventId") ?: "",
        documentSnapshot.getString("eventName") ?: "",
        documentSnapshot.getString("reportAuthorId") ?: ""
    )

    // constructor for Parcelable EventReport object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

        ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(eventAuthorId)
        parcel.writeString(category)
        parcel.writeString(details)
        parcel.writeString(reportedDate)
        parcel.writeString(eventId)
        parcel.writeString(eventName)
        parcel.writeString(reportAuthorId)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventReport> {
        override fun createFromParcel(parcel: Parcel): EventReport {
            return EventReport(parcel)
        }

        override fun newArray(size: Int): Array<EventReport?> {
            return arrayOfNulls(size)
        }
    }

}
