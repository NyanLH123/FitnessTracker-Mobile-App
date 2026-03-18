package com.example.fitnesstrackingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnesstrackingapp.databinding.FragmentUserHomeBinding
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder

import android.widget.Toast

import androidx.appcompat.app.AlertDialog

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import java.io.IOException
import java.util.Locale

class UserHomeFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_CODE = 100
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.floatingActionButton.setOnClickListener {

        }

        return binding.root
    }
}