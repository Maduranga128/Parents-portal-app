package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parents")
data class ParentEntity(
    @PrimaryKey
    val id: String,
    val studentName: String,
    val studentClass: String,
    val parentName: String,
    val job: String,
    val support: String,
    val phone: String,
    val address: String,
    val timestamp: String,
    val isSynced: Boolean = true
)
