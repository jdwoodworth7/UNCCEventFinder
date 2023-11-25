package com.example.test

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseStorageUtil {
    private var firebaseStorage: FirebaseStorage? = null

    //create reference for Cloud Store - Images
    private var storageReference: StorageReference? = null

    //create reference for FireStore - SQL queries and structured data
    private var firebaseFireStore: FirebaseFirestore? = null

    //function to get the FirebaseStorage instance
    fun getFirebaseStorageInstance(): FirebaseStorage {
        //check if the FirebaseStorage instance is null and initialize
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance()
        }
        return firebaseStorage!!
    }

    //function to get the FirebaseFireStore instance
    fun getFirebaseFireStoreInstance(): FirebaseFirestore {
        //check if the FirebaseStorage instance is null and initialize
        if (firebaseFireStore == null) {
            firebaseFireStore = FirebaseFirestore.getInstance()
        }
        return firebaseFireStore!!
    }

    //function to get the StorageReference
    fun getStorageReference(): StorageReference {
        //check if the StorageReference is null and get reference
        if (storageReference == null) {
            storageReference = getFirebaseStorageInstance().reference
        }
        return storageReference!!
    }
}
