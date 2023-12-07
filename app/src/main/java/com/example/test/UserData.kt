package com.example.test

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class UserData (
        val email: String,
        val firstname: String,
        val lastname: String,
        val password: String,
        val status: String,
        val friendIds: List<String>,
        val privacy: String?
) : Parcelable    {
        // No-argument constructor for Firestore deserialization
        constructor() : this(
        "",
        "",
        "",
        "",
        "",
        listOf(),
        ""
        )

        // constructor for converting Firestore DocumentSnapshot to UserData object
        constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("email") ?: "",
        documentSnapshot.getString("firstname") ?: "",
        documentSnapshot.getString("lastname") ?: "",
        documentSnapshot.getString("password") ?: "",
        documentSnapshot.getString("status") ?: "",
        documentSnapshot.get("friendIds") as? List<String> ?: listOf(),
        documentSnapshot.getString("privacy") ?: ""
        )

        // constructor for Parcelable UserData object to send between activities as Intent attribute
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createStringArrayList()?.toList() ?: listOf(),
            parcel.readString() ?: ""
            )

        //Parcelable Implementations
        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(email)
            parcel.writeString(firstname)
            parcel.writeString(lastname)
            parcel.writeString(password)
            parcel.writeString(status)
            parcel.writeStringList(friendIds)
            parcel.writeString(privacy)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<UserData> {
            override fun createFromParcel(parcel: Parcel): UserData {
                return UserData(parcel)
            }

            override fun newArray(size: Int): Array<UserData?> {
                return arrayOfNulls(size)
            }
        }
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,
            "password" to password,
            "status" to status,
            "friendIds" to friendIds,
            "privacy" to privacy
        )
    }
}