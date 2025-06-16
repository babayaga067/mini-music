package com.example.sangeet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView

data class Song(val title: String, val artist: String, val imageRes: Int, var liked: Boolean)

class SongAdapter(private val songs: List<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songImage: ImageView = view.findViewById(R.id.song_image)
        val songTitle: TextView = view.findViewById(R.id.song_title)
        val songArtist: TextView = view.findViewById(R.id.song_artist)
        val likeButton: ImageView = view.findViewById(R.id.like_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_item, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.songTitle.text = song.title
        holder.songArtist.text = song.artist
        holder.songImage.setImageResource(song.imageRes)
        holder.likeButton.setImageResource(
            if (song.liked) R.drawable.heartfilled else R.drawable.heartoutline
        )
        holder.likeButton.setOnClickListener {
            song.liked = !song.liked
            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = songs.size
}

@Preview
@Composable
fun SongItemPreview() {
    val song = Song("Song Title", "Artist Name", R.drawable.placeholder, false)
    SongItem(song)
}
