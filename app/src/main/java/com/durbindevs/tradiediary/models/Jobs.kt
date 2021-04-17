package com.durbindevs.tradiediary.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat


@Entity(
    tableName = "jobs"
)
@Parcelize
data class Jobs(
    val location: String,
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateCreated: Long = System.currentTimeMillis(),
    val dateFinished: Long,
    val startKm: String,
    val finishKm: String,
    val totalKm: String = "0",
    var isCompleted: Boolean = false,
    val totalTime: String = ""
) : Parcelable {
    val dateCreatedFormat: String
        get() = DateFormat.getDateTimeInstance().format(dateCreated)
}
