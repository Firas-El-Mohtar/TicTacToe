package com.example.testingthings.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testingthings.R
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    val firestore = Firebase.firestore
    val query = firestore.collection("matches").orderBy("duration", Query.Direction.DESCENDING).limit(50)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        historyAdapter = HistoryAdapter(query)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter.apply {
                notifyDataSetChanged()
            }

        }
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HistoryFragment().apply {
                }

    }
    public override fun onStart() {
        super.onStart()

        historyAdapter.startListening()
    }

    public override fun onStop() {
        super.onStop()
        historyAdapter.stopListening()
    }
}