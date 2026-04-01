package com.example.fitnesstrackingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnesstrackingapp.databinding.FragmentListBinding
import org.json.JSONObject

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var workoutList: ArrayList<WorkoutModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        val userId=arguments?.getInt("id")
        showWorkoutList(userId!!)
        return binding.root
    }

    @SuppressLint("NotifyDataChanged")
    private fun showRecyclerView(workoutList: ArrayList<WorkoutModel>) {
        binding.recyclerViewWorkoutList.layoutManager= LinearLayoutManager(context)
        binding.recyclerViewWorkoutList.adapter= WorkoutAdapter(workoutList,
            showWorkoutDetails = {workoutModel -> showAlertDetails(workoutModel)},
            deleteWorkout = {id->
                deleteWorkoutConfirmation(id)
            },
            editWorkout = {workoutModel ->
                editWorkoutAction(workoutModel)}
        )
        binding.recyclerViewWorkoutList.adapter?.notifyDataSetChanged()
    }

    private fun deleteWorkoutConfirmation (id:Int){
        val alertDialog= AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Warning")
            .setMessage("are you sure you want to delete it?")
            .setCancelable(false)
            .setPositiveButton("yes"){dialog,_ ->
                deleteWorkoutAction(id)
                dialog.dismiss()
            }
            .setNegativeButton("no"){dialog,_ -> dialog.dismiss()}
        val alert = alertDialog.create()
        alert.show()
    }

    private fun editWorkoutAction(workout: WorkoutModel) {
        val action= ListFragmentDirections.actionListFragmentToEntryFragment(workout)
        findNavController().navigate(action)
    }

    private fun deleteWorkoutAction(id: Int) {
        val url="http://10.0.2.2/projects/mobileapi/mobile/deleteWorkOut.php"

        val request=object: StringRequest(Method.POST,url,
            { response->

                Log.d("Delete Workout Listener", "Successfully Deleted!")
                val obj = JSONObject(response)
                val msg= obj.get("message").toString()
                Toast.makeText(context,"Response:$msg", Toast.LENGTH_LONG).show()

            }, { error->
                Log.d("Delete Workout Listener", "***Error:$error")

            }

        ){
            override fun getParams(): Map<String?, String?>? {
                return mapOf(
                    "id" to id.toString()
                )
            }
        }
        Volley.newRequestQueue(context).add(request)

    }

    private fun showWorkoutList(userId: Int) {
        val api = "http://10.0.2.2/projects/mobileapi/mobile/showWorkoutList.php"

        val request = object : StringRequest(
            Request.Method.POST, api,
            { response ->
                Log.i("Workout List", "***Workout Response: $response")

                try {
                    val obj = JSONObject(response)
                    val msg = obj.getString("message")
                    val workouts = obj.getJSONArray("workouts")

                    if (msg == "not_empty") {
                        for  (i in 0 until workouts.length()) {
                            val workoutItem = workouts.getJSONObject(i)
                            val id = workoutItem.getInt("id")
                            val type = workoutItem.getString("type")
                            val logDate = workoutItem.getString("logDate")
                            val day = workoutItem.getInt("day")
                            val month = workoutItem.getInt("month")
                            val year = workoutItem.getInt("year")
                            val time = workoutItem.getString("time")
                            val duration = workoutItem.getInt("duration")
                            val distance = workoutItem.getDouble("distance")
                            val weight = workoutItem.getDouble("weight")
                            val place = workoutItem.getString("place")
                            val remark = workoutItem.getString("remark")

                            val workoutModel = WorkoutModel(id, type, logDate, day, month, year, time, duration, distance, weight, place, remark, userId)
                        }
                        Log.i("Workout List", "***Not empty. Size:" + workouts.length())
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
                    Log.e("Workout List", "JSON Parse Error: ${e.message}")
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

    private fun showAlertDetails(workoutModel: WorkoutModel){
        val stringBuilder= StringBuilder()
        stringBuilder.append("Type: "+workoutModel.type+"\n\n")
            .append("Date: "+workoutModel.logDate+"\n")
            .append("Time: "+workoutModel.time+"\n")
            .append("Duration: "+workoutModel.duration+" minutes\n")
            .append("Distance: "+workoutModel.distance+" km\n")
            .append("Weight: "+workoutModel.weight+" kg\n")
            .append("Place: "+workoutModel.place+"\n")
            .append("Remark: "+workoutModel.remark+"\n")
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Workout Information")
            .setMessage(stringBuilder.toString())
            .setCancelable(false)
            .setPositiveButton("Ok"){
                    dialog,_ -> dialog.dismiss()
            }
        val alert= alertDialog.create()
        alert.show()
    }

}