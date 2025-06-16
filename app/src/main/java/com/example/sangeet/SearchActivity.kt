package com.example.sangeet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity :
    AppCompatActivity() {
    private lateinit var songsRecyclerView: RecyclerView
    private lateinit var adapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        val songs = listOf(
            Song("Easy On Me", "Adele", R.drawable.adele, false),
            Song("Blue", "Yung Kai", R.drawable.blue, false),
            Song("Dandelions", "Ruth B", R.drawable.dandelions, false),
            Song("Upahaar", "Swoopna Suman", R.drawable.upahaar, true),
            Song("Jhim Jhimaune Aankha", "Ekdev Limbu", R.drawable.jhim, false),
            Song("Apna Bana Le", "Arijit Singh", R.drawable.apna, false)
        )

        songsRecyclerView = findViewById(R.id.songsRecyclerView)
        adapter = SongAdapter(songs)
        songsRecyclerView.layoutManager = LinearLayoutManager(this)
        songsRecyclerView.adapter = adapter
    }
}