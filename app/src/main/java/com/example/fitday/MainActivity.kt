package com.example.fitday

import EventRepository
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitday.adapters.EventsAdapter
import com.example.fitday.databinding.ActivityMainBinding
import com.example.fitday.room.EventDatabase
import com.example.fitday.viewmodel.EventViewModel
import com.example.fitday.viewmodel.EventViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val eventViewModel: EventViewModel by viewModels {
        EventViewModelFactory(EventRepository(EventDatabase.create(applicationContext).eventsDao))
    }
    private lateinit var adapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = eventViewModel

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = getString(R.string.name_of_main_activity)

        eventViewModel.events.observe(this) { events ->
            Log.d("MainActivity", "Observed events: $events")
            adapter.setEvents(events)
        }

        init()
    }


    private fun init() {
        adapter = EventsAdapter(mutableListOf(), this)
        binding.rcViewOfContacts.layoutManager = LinearLayoutManager(this)
        binding.rcViewOfContacts.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_event -> {
                val i = Intent(this, AddEvent::class.java)
                startActivity(i)
                true
            }
            R.id.update_event -> {
                val i = Intent(this, ActionsWithEvent::class.java)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}