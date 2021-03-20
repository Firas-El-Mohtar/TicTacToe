package com.example.testingthings.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testingthings.R
import com.example.testingthings.ingame.data.model.MatchHistory
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject


class HistoryAdapter(query: Query) : FirestoreAdapter<HistoryAdapter.ViewHolder>(query) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.player_name)
        private val isWinner: TextView = itemView.findViewById(R.id.is_winner)
        private val matchDuration: TextView = itemView.findViewById(R.id.match_duration)

        fun bind(snapshot: DocumentSnapshot) {

            val match = snapshot.toObject<MatchHistory>()

            name.text = "Player Name: " + match?.playerName
            isWinner.text = "Winner: " + match?.isWinner
            matchDuration.text = "Match Duration: " + match?.duration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getSnapshot(position))
    }
}
