package com.github.krxwl.codecat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "submodules",
    foreignKeys = [
        ForeignKey(parentColumns = arrayOf("type"), entity = Type::class, childColumns = arrayOf("type")),
        ForeignKey(parentColumns = arrayOf("id"), entity = Course::class, childColumns = arrayOf("module")),])
data class Submodule(@PrimaryKey val id: Int?,
                     @ColumnInfo(defaultValue = "0") var progress: Int? = 0,
                     val name: String?,
                     val module: Int?,
                    val type: String?,
                    var savedTaskId: Int?)