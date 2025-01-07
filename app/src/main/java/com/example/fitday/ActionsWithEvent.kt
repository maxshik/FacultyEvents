package com.example.fitday

import EventRepository
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.fitday.databinding.ActivityActionsWithEventBinding
import com.example.fitday.room.Event
import com.example.fitday.room.EventDatabase
import com.example.fitday.viewmodel.EventViewModel
import com.example.fitday.viewmodel.EventViewModelFactory

class ActionsWithEvent : AppCompatActivity() {
    private lateinit var binding: ActivityActionsWithEventBinding
    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityActionsWithEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventDao = EventDatabase.create(applicationContext).eventsDao
        val repository = EventRepository(eventDao)
        val factory = EventViewModelFactory(repository)
        eventViewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        binding.updateEventButton.setOnClickListener {
            updateEvent()
        }

        binding.deleteEventButton.setOnClickListener {
            deleteEvent()
        }
    }

    private fun updateEvent() {
        val eventId = binding.eventIdEditText.text.toString().trim()
        val newName = binding.eventNameEditText.text.toString().trim()
        val newTime = binding.eventTimeEditText.text.toString().trim()
        val newImageUrl = binding.imageLinkEditText.text.toString().trim()

        if (eventId.isBlank()) {
            Toast.makeText(this, "Пожалуйста, введите ID события!", Toast.LENGTH_SHORT).show()
            return
        }

        eventViewModel.events.observe(this) { events ->
            val eventToUpdate = events.find { it.id.toString() == eventId }
            if (eventToUpdate != null) {
                val updatedEvent = eventToUpdate.copy(
                    name = if (newName.isNotBlank()) newName else eventToUpdate.name,
                    time = if (newTime.isNotBlank()) newTime else eventToUpdate.time,
                    photoUrl = if (newImageUrl.isNotBlank()) newImageUrl else eventToUpdate.photoUrl
                )
                eventViewModel.updateEvent(updatedEvent)
            } else {
                Toast.makeText(this, "Событие с таким ID не найдено!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteEvent() {
        val eventId = binding.eventIdEditText.text.toString().trim()

        if (eventId.isBlank()) {
            Toast.makeText(this, "Пожалуйста, введите ID события!", Toast.LENGTH_SHORT).show()
            return
        }

        eventViewModel.events.observe(this) { events ->
            val eventToDelete = events.find { it.id.toString() == eventId }
            if (eventToDelete != null) {
                eventViewModel.deleteEvent(eventToDelete)
                Toast.makeText(this, "Событие удалено!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Событие с таким ID не найдено!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}