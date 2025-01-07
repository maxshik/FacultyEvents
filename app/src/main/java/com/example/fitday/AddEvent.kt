package com.example.fitday

import EventRepository
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.fitday.databinding.ActivityAddEventBinding
import com.example.fitday.room.Event
import com.example.fitday.room.EventDatabase
import com.example.fitday.viewmodel.EventViewModel
import com.example.fitday.viewmodel.EventViewModelFactory

class AddEvent : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding

    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory(EventRepository(EventDatabase.create(applicationContext).eventsDao))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val eventDao = EventDatabase.create(applicationContext).eventsDao
        val repository = EventRepository(eventDao)
        val factory = EventViewModelFactory(repository)

        binding.saveEventButton.setOnClickListener {
            saveEvent()
        }
    }

    private fun saveEvent() {
        val name = binding.eventNameEditText.text.toString().trim()
        val time = binding.eventTimeEditText.text.toString().trim()
        val prizes = binding.eventPrizesEditText.text.toString().trim()
        val rules = binding.eventRulesEditText.text.toString().trim()
        val imageUrl = binding.imageLink.text.toString().trim()

        if (name.isBlank() || time.isBlank() || prizes.isBlank() || rules.isBlank() || imageUrl.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля!", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            Toast.makeText(this, "Мероприятие добавлено!", Toast.LENGTH_SHORT).show()
        }

        val event = Event(0, time, name, prizes, imageUrl, rules)
        Thread{
            eventViewModel.addEvent(event)
        }.start()
        finish()
    }
}