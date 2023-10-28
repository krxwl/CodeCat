package com.github.krxwl.codecat

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "types")
data class Type(@PrimaryKey val type: String)