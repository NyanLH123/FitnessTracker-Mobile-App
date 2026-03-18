package com.example.fitnesstrackingapp

import java.io.Serializable

data class WorkoutModel(
    val id: Int,
    val type: String,
    val logDate: String,
    val day: Int,
    val month: Int,
    val year: Int,
    val time: String,
    val duration: Int,
    val distance: Double,
    val weight: Double,
    val place: String,
    val remark: String,
    val userId: Int
) : Serializable