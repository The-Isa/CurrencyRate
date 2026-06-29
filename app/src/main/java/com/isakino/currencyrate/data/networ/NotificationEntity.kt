package com.isakino.currencyrate.data.networ

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class NotificationEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val currencyCode: String,

    val message: String,

    val triggerTime: Long
)