package com.example.fitnesstrackingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnesstrackingapp.databinding.FragmentUserHomeBinding

class UserHomeFragment : Fragment() {
    private lateinit var binding: FragmentUserHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(layoutInflater, container, false)
        val id = arguments?.getString("id")
        val firstName = arguments?.getString("firstname")
        val lastName = arguments?.getString("lastnname")

        Log.i("Welcome Message","Welcome back!, $firstName $lastName")
        binding.txtWelcome.text ="Welcome Back!, $firstName $lastName"

        return binding.root
    }
}