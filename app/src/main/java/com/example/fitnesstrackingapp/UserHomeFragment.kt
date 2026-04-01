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
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnesstrackingapp.databinding.CustomDialogViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class UserHomeFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val REQUEST_CODE = 100
    private val monthList = arrayOf(0,0,0,0,0,0,0,0,0,0,0,0)
    private lateinit var binding: FragmentUserHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHomeBinding.inflate(layoutInflater, container, false)
        val id = arguments?.getInt("id")
        val firstName = arguments?.getString("firstname")

        Log.i("Welcome Message","Welcome back!, $firstName")
        binding.txtWelcome.text ="Welcome Back! $firstName"
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val barSet = listOf(
            "JAN" to monthList[0].toFloat(),
            "FEB" to monthList[1].toFloat(),
            "MAR" to monthList[2].toFloat(),
            "APR" to monthList[3].toFloat(),
            "MAY" to monthList[4].toFloat(),
            "JUNE" to monthList[5].toFloat(),
            "JUL" to monthList[6].toFloat(),
            "AUG" to monthList[1].toFloat(),
            "SEP" to monthList[8].toFloat(),
            "OCT" to monthList[9].toFloat(),
            "NOV" to monthList[10].toFloat(),
            "DEC" to monthList[11].toFloat()
        )

        binding.barChartView.animate(barSet)
        binding.barChartView.invalidate()
        binding.floatingActionButton.setOnClickListener {

        }

        return binding.root
    }

    private fun showWorkoutList(userId: Int) {
        val api = "http://10.0.2.2/projects/mobileapi/mobile/showTotalDuration.php"

        val request = object : StringRequest(
            Request.Method.POST, api,
            { response ->
                Log.i("Workout Duration Report", "***Duration Report Response: $response")

                try {
                    val obj = JSONObject(response)
                    val msg = obj.getString("message")
                    val duration = obj.getJSONArray("duration")

                    if (msg == "not_empty") {
                        for  (i in 0 until duration.length()) {
                            val durationItem = duration.getJSONObject(i)
                            val totalDuration = durationItem.getInt("total_duration")
                            val month = durationItem.getInt("month")

                            monthList[month - 1] = totalDuration
                        }
                        Log.i("Duration Report", "***Not empty. Size:" + duration.length())
                    } else {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setTitle("Empty Workout!")
                            .setMessage("No record in your workout list")
                            .setCancelable(false)
                            .setPositiveButton("Okay"){
                                    dialog, _-> dialog.dismiss()
                            }
                        val alert = alertDialog.create()
                        alert.show()
                    }
                } catch (e: Exception) {
                    Log.e("Duration Report", "JSON Parse Error: ${e.message}")
                    Toast.makeText(context, "Invalid response from server", Toast.LENGTH_LONG).show()
                }
            },
            { error ->
                Log.e("Workout List", "***Error Message: ${error.message}")
                Toast.makeText(context, "fetch failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): Map<String, String> {
                return mapOf(
                    "userId" to userId.toString()
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
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