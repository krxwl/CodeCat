package com.github.krxwl.codecat.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "questions",
    foreignKeys = [
        ForeignKey(parentColumns = arrayOf("id"), entity = Submodule::class, childColumns = arrayOf("submodule"))
    ])
data class Task(@PrimaryKey val id: Int?,
                val task: String?,
                var isSolved: String?,
                val submodule: Int,
                val input: String?,
                val output: String?,
                var answer: String?,
                val taskName: String?) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeInt(id!!)
        p0.writeString(task!!)
        p0.writeString(isSolved)
        p0.writeInt(submodule!!)
        p0.writeString(input)
        p0.writeString(output)
        p0.writeString(answer)
        p0.writeString(taskName)
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}