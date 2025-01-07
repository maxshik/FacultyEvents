package com.example.fitday.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitday.R
import com.example.fitday.databinding.EventCardBinding
import com.example.fitday.room.Event
import com.squareup.picasso.Picasso;

class EventsAdapter(
    private var events: MutableList<Event>,
    private val context: Context
) : RecyclerView.Adapter<EventsAdapter.EventHolder>() {

    class EventHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = EventCardBinding.bind(item)

        fun bind(event: Event) = with(binding) {
            type.text = event.name
            time.text = event.time
            prizes.text = "Призы: ${event.prizes}"
            rules.text = "Правила: ${event.rules}"
            Picasso.get().load(event.photoUrl).into(eventImage)
        }
    }

    fun setEvents(newEvents: List<Event>) {
        Log.d("EventsAdapter", "Setting events: $newEvents")
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return EventHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }
}