package com.example.fitnesstrackingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnesstrackingapp.databinding.FragmentLoginBinding
import org.json.JSONObject

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        // Navigate to RegisterFragment when user clicks register link
        binding.txtRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        // Handle login button click
        binding.btnLogin.setOnClickListener {
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()

            if (validateInput(username, password)) {
                loginUser(username, password)
            }
        }

        return binding.root
    }

    private fun validateInput(username: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.editUsername.error = "Username or email is required"
            binding.editUsername.requestFocus()
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

        return isValid
    }


    private fun loginUser(username: String, password: String) {
        val api = "http://10.0.2.2/projects/mobileapi/mobile/login.php"

        val request = object : StringRequest(
            Request.Method.POST, api,
            { response ->
                Log.i("Login Listener", "***Login Response: $response")

                try {
                    val obj = JSONObject(response)
                    val msg = obj.getString("message")

                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

                    if (msg == "Login Success") {
                        val userObj = obj.getJSONObject("user")
                        val id = userObj.getString("id")
                        val loggedInUsername = userObj.getString("username")

                        Log.i("Login User", "Welcome, $loggedInUsername")
                        val intent = Intent(context, UserActivity::class.java)
                        intent.putExtra("id", id)
                        intent.putExtra("username", loggedInUsername)
                        startActivity(intent)
                        activity?.finish()
                    }
                } catch (e: Exception) {
                    Log.e("Login Listener", "JSON Parse Error: ${e.message}")
                    Toast.makeText(context, "Invalid response from server", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("Login Listener", "***Error Message: ${error.message}")
                Toast.makeText(context, "Login failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "username" to username,
                    "password" to password
                )
            }
        }

        Volley.newRequestQueue(requireContext()).add(request)
    }
}