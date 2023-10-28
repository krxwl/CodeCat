package com.github.krxwl.codecat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "submodules")
data class Submodule(@PrimaryKey val id: Int,
    var progress: Int,
    val name: String,
    val module: Int)