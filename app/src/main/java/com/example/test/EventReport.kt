package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class EventReport(
    val authorId: String,
    val category: List<String>,
    val details: String,
    val eventId: String,
    val eventName: String,
    val imageUri: String?,
    val reportAuthorId: String
    ) : Parcelable {

    //No-argument constructor for Firestore deserialization

    constructor() : this(
        "",
        listOf(),
        "",
        "",
        "",
        "",
        ""
        )

    // constructor for converting Firestore DocumentSnapshot to EventReport object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("authorId") ?: "",
        documentSnapshot.get("category") as? List<String> ?: listOf(),
        documentSnapshot.getString("details") ?: "",
        documentSnapshot.getString("eventId") ?: "",
        documentSnapshot.getString("eventName") ?: "",
        documentSnapshot.getString("imageUri") ?: "",
        documentSnapshot.getString("reportAuthorId") ?: ""
        )

    // constructor for Parcelable EventReport object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.createStringArrayList()?.toList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(authorId)
        parcel.writeStringList(category)
        parcel.writeString(details)
        parcel.writeString(eventId)
        parcel.writeString(eventName)
        parcel.writeString(imageUri)
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
