package com.example.test

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

data class EventData(
        val id: UUID,
        val title: String,
        val description: String,
        val date: String, // converted from LocalDate
        val time: String, // converted from LocalTime
        val buildingName: String?,
        val address: String?,
        val userUploadedImageUrl: String?,
        val categories: List<String>
) : Parcelable {

        // Add a no-argument constructor
        constructor() : this(
                UUID.randomUUID(),
                "",
                "",
                "",
                "",
                null,
                null,
                null,
                listOf()
        )

        constructor(parcel: Parcel) : this(
                UUID.fromString(parcel.readString() ?: ""),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.createStringArrayList()?.toList() ?: listOf()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id.toString())
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeString(date)
                parcel.writeString(time)
                parcel.writeString(buildingName)
                parcel.writeString(address)
                parcel.writeString(userUploadedImageUrl)
                parcel.writeStringList(categories)
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