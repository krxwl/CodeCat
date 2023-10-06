package com.github.krxwl.codecat

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "modules")
data class Course(@PrimaryKey val id: Int,
    var name: String = "",
    var description: String = "",
    var progress: Int = 0,
    var main: Boolean,
    var image: ByteArray)