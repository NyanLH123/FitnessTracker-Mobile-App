package com.example.fitnesstrackingapp

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnesstrackingapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.btnRegister.setOnClickListener {
            val firstName = binding.editFirstName.text.toString()
            val lastName = binding.editLastName.text.toString()
            val username = binding.editUsername.text.toString()
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val confirmPass = binding.editConfirmPass.text.toString()


        }

        return binding.root
    }

    private fun validateInput(firstName: String, lastName: String, username: String, email: String, password: String, confirmPass: String): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            binding.editFirstName.error = "First name is required"
            binding.editFirstName.requestFocus()
            isValid = false
        }

        if (lastName.isEmpty()) {
            binding.editLastName.error = "Last name is required"
            binding.editLastName.requestFocus()
            isValid = false
        }

        if (username.isEmpty()) {
            binding.editUsername.error = "Username is required"
            binding.editUsername.requestFocus()
            isValid = false
        } else if (username.length < 3) {
            binding.editUsername.error = "Username must be at least 3 characters"
            binding.editUsername.requestFocus()
            isValid = false
        }

        if (email.isEmpty()) {
            binding.editEmail.error = "Email is required"
            binding.editEmail.requestFocus()
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editEmail.error = "Please enter a valid email address"
            binding.editEmail.requestFocus()
            isValid = false
        }

        if (password.isEmpty()) {
            binding.editPassword.error = "Password is required"
            binding.editPassword.requestFocus()
            isValid = false
        } else if (password.length < 6) {
            binding.editPassword.error = "Password must be at least 6 characters"
            binding.editPassword.requestFocus()
            isValid = false
        }

        if (confirmPass.isEmpty()) {
            binding.editConfirmPass.error = "Please confirm your password"
            binding.editConfirmPass.requestFocus()
            isValid = false
        } else if (password != confirmPass) {
            binding.editConfirmPass.error = "Passwords do not match"
            binding.editConfirmPass.requestFocus()
            isValid = false
        }

        return isValid
    }

    private fun registerUser(firstName: String, lastName: String, username: String, email: String, password: String) {

    }

}