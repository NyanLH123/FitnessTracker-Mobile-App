package com.example.fitnesstrackingapp

import android.os.Bundle
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

        if (email.isEmpty()) {
            binding.
        }
    }

}