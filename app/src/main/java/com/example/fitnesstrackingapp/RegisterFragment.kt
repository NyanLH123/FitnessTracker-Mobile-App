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
        val api = "http://10.0.2.2/projects/mobileapi/register.php"

        val request = object : StringRequest(Method.POST, api, { response ->
            Log.i("Register Listener", "***Register Successful")

            val obj = JSONObject(response)
            val msg = obj.get("message").toString()

            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

            if (msg == "Login Success") {
                val userObj = obj.getJSONObject("user")
                val username = userObj.getString("username")

                Log.i("Login User", "Welcome, $username")

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }, { error ->
            Log.i("Register Listener","***Error Message: $error")
        }) {
            override fun getParams(): Map<String?, String?>? {
                return mapOf("firstname" to firstName, "lastname" to lastName, "username" to username, "email" to email, "password" to password)
            }
        }
        Volley.newRequestQueue(context).add(request)
    }
}