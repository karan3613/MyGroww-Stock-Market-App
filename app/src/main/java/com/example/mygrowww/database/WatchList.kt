package com.example.mygrowww.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlists")
data class WatchList(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)