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
    val startKm: Int = 0,
    val finishKm: Int = 0,
    var isCompleted: Boolean = false
) : Parcelable {
    val dateCreatedFormat: String
        get() = DateFormat.getDateTimeInstance().format(dateCreated)
}
