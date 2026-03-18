package com.example.fitnesstrackingapp

import java.io.Serializable

data class UserModel(val id:Int,
                     val firstName: String,
                     val lastName: String,
                     val username: String,
                     val email: String,
                     val password: String
    ): Serializable
