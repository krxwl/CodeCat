package com.github.krxwl.codecat.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.UUID

@Entity(tableName = "modules")
data class Course(
    @PrimaryKey val id: Int?,
    var name: String? = "",
    var description: String? = "",
    var progress: Int? = 0,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var image: Bitmap?)