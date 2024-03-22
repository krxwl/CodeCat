package com.github.krxwl.codecat.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "questions",
    foreignKeys = [
        ForeignKey(parentColumns = arrayOf("id"), entity = Submodule::class, childColumns = arrayOf("submodule"))
    ])
data class Task(@PrimaryKey val id: Int,
                val task: String?,
                var isSolved: Int? = 0,
                val submodule: Int,
                val input: String?,
                val output: String?,
                var answer: String?,
                val taskName: String?)