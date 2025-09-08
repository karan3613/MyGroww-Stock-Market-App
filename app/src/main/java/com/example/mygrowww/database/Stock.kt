package com.example.mygrowww.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "stocks",
    foreignKeys = [
        ForeignKey(
            entity = WatchList::class,
            parentColumns = ["id"],
            childColumns = ["watchlistId"],
            onDelete = ForeignKey.CASCADE
        )
                  ],
    indices = [Index(value = ["watchlistId"])]
)
data class Stock(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val watchlistId: Long,
    val symbol: String,
    val companyName: String?,
    val price: Double?,
    val week52High: Double?,
    val week52Low: Double?,
    val marketCap: Double?,
    val volume: Long?
)
