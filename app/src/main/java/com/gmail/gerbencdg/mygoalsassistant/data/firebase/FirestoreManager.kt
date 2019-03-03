package com.gmail.gerbencdg.mygoalsassistant.data.firebase

import com.gmail.gerbencdg.mygoalsassistant.model.entities.Quote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class FirestoreManager(private val firestore: FirebaseFirestore) {

    fun fetchQuote(onSuccess: (Quote) -> Unit, onFailure: () -> Unit) {

        val firestoreQuoteQuery = firestore
            .collection("quotes")
            .limit(1)

        firestore.collection("config").get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    onFailure()
                    return@addOnSuccessListener
                }
                val quotesCount = it.documents.first()["quotes_count"] as Long
                val randomQuoteIndex = Math.random().times(quotesCount).toInt()
                fetchAndShowQuote(firestoreQuoteQuery, randomQuoteIndex, onSuccess)
            }
            .addOnFailureListener {
                onFailure()
            }
    }

    private fun fetchAndShowQuote(
        firestoreQuery: Query, randomIndex: Int, onSuccess: (Quote) -> Unit, reversed: Boolean = false) {

        firestoreQuery
            .orderBy("id")
            .run {
                if (reversed) firestoreQuery.whereLessThanOrEqualTo("id", randomIndex)
                else firestoreQuery.whereGreaterThan("id", randomIndex)
            }
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    fetchAndShowQuote(firestoreQuery, randomIndex, onSuccess,true)
                    return@addOnSuccessListener
                }

                val quote = it.first().toObject(Quote::class.java)
                onSuccess(quote)
            }
    }
}