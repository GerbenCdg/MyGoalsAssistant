package com.gmail.gerbencdg.mygoalsassistant.application

import android.app.Application
import com.gmail.gerbencdg.mygoalsassistant.data.room.AppDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class GoalsAssistantApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.initialize(this)
        FirebaseApp.initializeApp(this)

        val firestoreSettings = FirebaseFirestoreSettings
            .Builder()
            .setPersistenceEnabled(false).build()

        FirebaseFirestore.getInstance().firestoreSettings = firestoreSettings
    }

}