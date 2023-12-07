package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class EventData(
        var id: String,
        val title: String,
        val description: String,
        val date: String,
        val time: String,
        val eventSessionIds: List<String>,
        val buildingName: String?,
        val address: String?,
        val imageUri: String?,
        val categories: List<String>,
        val audience: List<String>,
        val authorId: String?,
        val reportCount : Int
) : Parcelable {

        // No-argument constructor for Firestore deserialization
        constructor() : this(
                UUID.randomUUID().toString(),
                "",
                "",
                "",
                "",
                listOf(),
                null,
                null,
                null,
                listOf(),
                listOf(),
                "",
                0
        )

        // constructor for converting Firestore DocumentSnapshot to EventData object
        constructor(documentSnapshot: DocumentSnapshot) : this(
                documentSnapshot.get("id") as? String ?: UUID.randomUUID().toString(),
                documentSnapshot.getString("title") ?: "",
                documentSnapshot.getString("description") ?: "",
                documentSnapshot.getString("date") ?: "",
                documentSnapshot.getString("time") ?: "",
                documentSnapshot.get("eventSessionIds") as? List<String> ?: listOf(), // Change the type
                documentSnapshot.getString("buildingName"),
                documentSnapshot.getString("address"),
                documentSnapshot.getString("userUploadedImageUrl"),
                documentSnapshot.get("categories") as? List<String> ?: listOf(),
                documentSnapshot.get("audience") as? List<String> ?: listOf(),
                documentSnapshot.getString("authorId") ?: "",
                documentSnapshot.get("reportCount") as? Int ?: 0
        )

        // constructor for Parcelable EventData object to send between activities as Intent attribute
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.createStringArrayList()?.toList() ?: listOf(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createStringArrayList()?.toList() ?: listOf(),
                parcel.createStringArrayList()?.toList() ?: listOf(),
                parcel.readString() ?: "",
                parcel.readInt()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeString(date)
                parcel.writeString(time)
                parcel.writeStringList(eventSessionIds)
                parcel.writeString(buildingName)
                parcel.writeString(address)
                parcel.writeString(imageUri)
                parcel.writeStringList(categories)
                parcel.writeStringList(audience)
                parcel.writeString(authorId)
                parcel.writeInt(reportCount)
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

        data class User(
                val name: String = "",
                val privacySettings: PrivacySettings = PrivacySettings()
        )

        data class PrivacySettings(
                val showEvents: Boolean = true
        )
}