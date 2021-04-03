package com.durbindevs.tradiediary.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant


@Entity(
    tableName = "jobs"
)
data class Jobs(
    val location: String,
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateCreated: Instant = Instant.now(),
    val startKm: Int = 0,
    val finishKm: Int = 0,
    val isCompleted: Boolean = false
    )
