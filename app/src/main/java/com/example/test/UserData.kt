package com.example.test

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

data class UserData(
    val id: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val status: String,
    val friendIds: List<String>,
    val privacy: String?,
    val reportCases: Int,
    val isModerator: Boolean
) : Parcelable {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        listOf(),
        "",
        0,
        false
    )

    // constructor for converting Firestore DocumentSnapshot to UserData object
    constructor(documentSnapshot: DocumentSnapshot) : this(
        documentSnapshot.getString("id") ?: "",
        documentSnapshot.getString("email") ?: "",
        documentSnapshot.getString("firstname") ?: "",
        documentSnapshot.getString("lastname") ?: "",
        documentSnapshot.getString("password") ?: "",
        documentSnapshot.getString("status") ?: "",
        documentSnapshot.get("friendIds") as? List<String> ?: listOf(),
        documentSnapshot.getString("privacy") ?: "",
        documentSnapshot.get("reportCases") as? Int ?: 0,
        documentSnapshot.get("isModerator") as? Boolean ?: false
    )

    // constructor for Parcelable UserData object to send between activities as Intent attribute
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList()?.toList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readBoolean() ?: false
    )

    //constructor for menu
    constructor(id:String, email: String, firstname: String, isModerator: Boolean) :  this(
        id,
        email,
        firstname,
        "",
        "",
        "",
        listOf(),
        "",
        0,
        isModerator
    )

    //Parcelable Implementations
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(email)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(password)
        parcel.writeString(status)
        parcel.writeStringList(friendIds)
        parcel.writeString(privacy)
        parcel.writeInt(reportCases)
        parcel.writeBoolean(isModerator)
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
//    fun toMap(): Map<String, Any?> {
//        return mapOf(
//            "id" to id,
//            "email" to email,
//            "firstname" to firstname,
//            "lastname" to lastname,
//            "password" to password,
//            "status" to status,
//            "friendIds" to friendIds,
//            "privacy" to privacy,
//            "reportCases" to reportCases
//        )
//    }
    }
    fun createUserDataForMenu(id:String, email: String, firstname: String, isModerator: Boolean) :UserData {
        return UserData(id,firstname,email,isModerator)
    }
}