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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.fitnesstrackingapp.databinding.CustomDialogViewBinding
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

    private fun getLastLocation() {
        val addressString= StringBuilder()
        var city=""
        var country=""
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    try {
                        Log.d("Location***","Location: "+it.latitude+"..."+it.longitude)
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses: List<Address>? = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        val address = addresses?.get(0)
                        addressString.append("Latitude: ${address!!.latitude}\n")
                            .append("Longitude: ${address.longitude}\n")
                            .append("Address: ${address.getAddressLine(0)}\n")
                        //.append("City: ${address.locality}\n")
                        //.append("Country: ${address.countryName}")
                        city= "City: ${address.locality}"
                        country = "Country: ${address.countryName}"

                        val dialogViewBinding = CustomDialogViewBinding.inflate(layoutInflater)

                        val alertDialog = AlertDialog.Builder(requireContext())
                            .setView(dialogViewBinding.root)
                            .create()

                        dialogViewBinding.txtAddress.text = addressString.toString()
                        dialogViewBinding.txtCity.text = city
                        dialogViewBinding.txtCountry.text = country

                        dialogViewBinding.btnLocationOk.setOnClickListener {
                            alertDialog.dismiss()
                        }

                        alertDialog.show()


                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            askPermission()
        }
    }//getLastLocation

    private fun askPermission() {
        if (activity != null) {
            activity?.let {
                ActivityCompat.requestPermissions(it,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults:
    IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Location***","Last Location")
                getLastLocation()
            } else {
                Toast.makeText(requireContext(), "Please provide the required permission", Toast.LENGTH_LONG).show()
            }
        }
    }//Location
}