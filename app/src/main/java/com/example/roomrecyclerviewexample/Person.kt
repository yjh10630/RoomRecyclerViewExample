package com.example.roomrecyclerviewexample

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Person(
    @PrimaryKey
    @SerializedName("name") val name: String,
    @SerializedName("age") val age: Int,
    @SerializedName("address") val address: String
)