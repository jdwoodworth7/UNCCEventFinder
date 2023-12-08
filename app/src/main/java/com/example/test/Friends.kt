package com.example.test

import android.os.Parcel
import android.os.Parcelable

data class FriendData(
    val userId: String,
    val username: String,
    val status: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(username)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendData> {
        override fun createFromParcel(parcel: Parcel): FriendData {
            return FriendData(parcel)
        }

        override fun newArray(size: Int): Array<FriendData?> {
            return arrayOfNulls(size)
        }
    }
}
