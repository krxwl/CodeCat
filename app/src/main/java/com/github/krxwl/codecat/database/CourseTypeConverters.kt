package com.github.krxwl.codecat.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.sql.Blob

class CourseTypeConverters {
    @TypeConverter
    fun fromBlobToBitmap(blob: ByteArray?): Bitmap? {
        if (blob != null) {
            return BitmapFactory.decodeByteArray(blob, 0, blob.size)
        }
        return null
    }

    @TypeConverter
    fun fromBitmapToBlob(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}