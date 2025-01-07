package com.example.fitday.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EVENTS")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: String,
    val name: String,
    val prizes: String,
    val photoUrl: String,
    val rules: String
)