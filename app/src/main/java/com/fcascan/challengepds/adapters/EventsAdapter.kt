package com.fcascan.challengepds.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fcascan.challengepds.R

class EventsAdapter(
    private val eventsList: MutableList<EventObject>,
) : RecyclerView.Adapter<EventsAdapter.EventHolder>() {
    private lateinit var context: Context

    class EventHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }

        fun setTitle(title: String) {
            val txtTitle: TextView = view.findViewById(R.id.tv)
            txtTitle.text = title
        }
    }

    class EventObject(var name: String, var timeStamp: String) {
        override fun toString(): String {
            return "EventObject(name='$name', timeStamp='$timeStamp')"
        }
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        context = parent.context
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.setTitle("${eventsList[position].name}: ${eventsList[position].timeStamp}")
    }
}