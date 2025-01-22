package com.busra.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "is_completed")
    var isCompleted: Boolean = false,

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}