package com.example.test

import android.os.Parcel
import android.os.Parcelable
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