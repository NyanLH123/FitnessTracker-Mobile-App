package com.example.fitnesstrackingapp

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnesstrackingapp.databinding.FragmentRegisterBinding
import org.json.JSONObject

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.btnRegister.setOnClickListener {
            val firstName = binding.editFirstName.text.toString().trim()
            val lastName = binding.editLastName.text.toString().trim()
            val username = binding.editUsername.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()
            val password = binding.editPassword.text.toString()
            val confirmPass = binding.editConfirmPass.text.toString()

            if (password == confirmPass) {
                val user = UserModel(
                    0,
                    firstName,
                    lastName,
                    username,
                    email,
                    password
                )

                if (validateInput(user, confirmPass)) {
                    registerUser(user)
                }
            } else {
                binding.editConfirmPass.error = "Passwords do not match"
                binding.editConfirmPass.requestFocus()
                Toast.makeText(requireContext(), "Confirm Password must match!", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun validateInput(user: UserModel, confirmPass: String): Boolean {
        if (user.firstName.isBlank()) {
            binding.editFirstName.error = "First name is required"
            binding.editFirstName.requestFocus()
            return false
        }

        if (user.lastName.isBlank()) {
            binding.editLastName.error = "Last name is required"
            binding.editLastName.requestFocus()
            return false
        }

        if (user.username.isBlank()) {
            binding.editUsername.error = "Username is required"
            binding.editUsername.requestFocus()
            return false
        }

        if (user.username.length < 3) {
            binding.editUsername.error = "Username must be at least 3 characters"
            binding.editUsername.requestFocus()
            return false
        }

        if (user.email.isBlank()) {
            binding.editEmail.error = "Email is required"
            binding.editEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(user.email).matches()) {
            binding.editEmail.error = "Please enter a valid email address"
            binding.editEmail.requestFocus()
            return false
        }

        if (user.password.isBlank()) {
            binding.editPassword.error = "Password is required"
            binding.editPassword.requestFocus()
            return false
        }

        if (user.password.length < 6) {
            binding.editPassword.error = "Password must be at least 6 characters"
            binding.editPassword.requestFocus()
            return false
        }

        if (confirmPass.isBlank()) {
            binding.editConfirmPass.error = "Please confirm your password"
            binding.editConfirmPass.requestFocus()
            return false
        }

        if (user.password != confirmPass) {
            binding.editConfirmPass.error = "Passwords do not match"
            binding.editConfirmPass.requestFocus()
            return false
        }

        return true
    }

    private fun registerUser(user: UserModel) {
        val api = "http://10.0.2.2/projects/mobileapi/mobile/register.php"

        val request = object : StringRequest(Method.POST, api, { response ->
            Log.i("Register Listener", "***Register Successful, $response", )

            val obj = JSONObject(response)
            val msg = obj.getString("message")

            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()

            if (msg == "Register Successful") {
                val userObj = obj.optJSONObject("user")
                val username = userObj?.optString("username", user.username) ?: user.username

                Log.i("Register User", "Welcome, $username")

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Log.i("Register Listener", "$msg")
            }
        }, { error ->
            Log.i("Register Listener", "***Error Message: $error")
            Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_LONG).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "firstname" to user.firstName,
                    "lastname" to user.lastName,
                    "username" to user.username,
                    "email" to user.email,
                    "password" to user.password
                )
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
    }
}