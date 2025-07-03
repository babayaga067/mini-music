package com.example.sangeet.repository

import com.example.sangeet.model.MusicModel
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class MusicRepositoryImpl : MusicRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.reference.child("musics")

    override fun addMusic(music: MusicModel, callback: (Boolean, String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("musics")
            .document(music.musicId)
            .set(music)
            .addOnSuccessListener {
                callback(true, "Music added successfully!")
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Error adding music")
            6}
    }

    override fun updateMusic(
        musicId: String,
        updatedData: Map<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(musicId).updateChildren(updatedData).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Music updated successfully!")
            } else {
                callback(false, it.exception?.message ?: "Failed to update music.")
            }
        }
    }

    override fun deleteMusic(
        musicId: String,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(musicId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Music deleted successfully!")
            } else {
                callback(false, it.exception?.message ?: "Failed to delete music.")
            }
        }
    }

    override fun getAllMusics(callback: (Boolean, String, List<MusicModel>?) -> Unit) {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var allMusics = mutableListOf<MusicModel>()
                    for (eachMusic in snapshot.children) {
                        var musics = eachMusic.getValue(MusicModel:: class.java)
                        if (musics != null) {
                            allMusics. add (musics)
                        }
                    }
                    callback(true, "music fetched successfully", allMusics)
                }
            }


            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, emptyList())
            }
        })
    }

    override fun getMusicById(
        musicId: String,
        callback: (Boolean, String, MusicModel?) -> Unit
    ) {
        ref.child(musicId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val music = snapshot.getValue(MusicModel::class.java)
                if (music != null) {
                    callback(true, "Music fetched successfully!", music)
                } else {
                    callback(false, "Music not found", null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(false, error.message, null)
            }
        })
    }
}
