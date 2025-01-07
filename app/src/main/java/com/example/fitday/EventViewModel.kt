package com.example.fitday.viewmodel

import EventRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitday.room.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = eventRepository.getAllEvents().asLiveData(Dispatchers.IO)
    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val eventList = eventRepository.getAllEvents().first()
                _events.value = eventList
                Log.d("EventViewModel", "Fetched events: $eventList")
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error fetching events", e)
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepository.insertNote(event)
                Log.d("EventViewModel", "Event added successfully: $event")

                val updatedEvents = eventRepository.getAllEvents().first()
                _events.postValue(updatedEvents)
                Log.d("EventViewModel", "Updated event list: $updatedEvents")
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error adding event", e)
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepository.updateNote(event)
                // Обновите список событий после обновления
                _events.postValue(eventRepository.getAllEvents().first())
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error updating event", e)
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventRepository.deleteNote(event)
                // Обновите список событий после удаления
                _events.postValue(eventRepository.getAllEvents().first())
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error deleting event", e)
            }
        }
    }
}