package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class EventData(
        val id: String, //converted from UUID
        val title: String,
        val description: String,
        val date: String, // converted from LocalDate
        val time: String, // converted from LocalTime
        val eventSessionIds: List<List<String>>, // holds session Ids of corresponding event
        // val startDate : String, // start date of the event for event duration
        // val endDate: String, // end dat eof the event for event duration
        val buildingName: String?,
        val address: String?,
        val imageUri: String?,
        val categories: List<String>,
        val audience: List<String>
) : Parcelable {
        // No-argument constructor for Firestore deserialization - REQUIRED
        constructor() : this(
                UUID.randomUUID().toString(),
                "",
                "",
                "",
                "",
                listOf(listOf()),
                null,
                null,
                null,
                listOf(),
                listOf()
        )

        // constructor for converting Firestore DocumentSnapshot to EventData object
        constructor(documentSnapshot: DocumentSnapshot) : this(
                documentSnapshot.get("id") as? String ?: UUID.randomUUID().toString(),
                documentSnapshot.getString("title") ?: "",
                documentSnapshot.getString("description") ?: "",
                documentSnapshot.getString("date") ?: "",
                documentSnapshot.getString("time") ?: "",
                documentSnapshot.get("eventSessionIds") as? List<List<String>> ?: listOf(),
                documentSnapshot.getString("buildingName"),
                documentSnapshot.getString("address"),
                documentSnapshot.getString("userUploadedImageUrl"),
                documentSnapshot.get("categories") as? List<String> ?: listOf(),
                documentSnapshot.get("audience") as? List<String> ?: listOf()
        )

        // constructor for Parcelable EventData object to send between activities as Intent attribute
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                (parcel.createStringArrayList() ?: listOf()).chunked(4),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createStringArrayList()?.toList() ?: listOf(),
                parcel.createStringArrayList()?.toList() ?: listOf()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id.toString())
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeString(date)
                parcel.writeString(time)
                parcel.writeStringList(eventSessionIds.flatten())
                parcel.writeString(buildingName)
                parcel.writeString(address)
                parcel.writeString(imageUri)
                parcel.writeStringList(categories)
                parcel.writeStringList(audience)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<EventData> {
                override fun createFromParcel(parcel: Parcel): EventData {
                        return EventData(parcel)
                }

                override fun newArray(size: Int): Array<EventData?> {
                        return arrayOfNulls(size)
                }
        }
}