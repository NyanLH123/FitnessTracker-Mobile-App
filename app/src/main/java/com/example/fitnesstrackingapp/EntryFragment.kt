package com.example.fitnesstrackingapp

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.fitnesstrackingapp.databinding.FragmentEntryBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EntryFragment : Fragment() {
    private lateinit var binding: FragmentEntryBinding
    private val activityType = listOf(
        "Select one activity type:",
        "Running",
        "Cycling",
        "Weight Lifting",
        "Swimming",
        "Boxing"
    )

    private var yy = 0
    private var mm = 0
    private var dd = 0
    private var selectedType = ""
    private var userId = 0

    @RequiresApi(
        Build.VERSION_CODES.TIRAMISU
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEntryBinding.inflate(inflater, container, false)
        val id = arguments?.getInt("id")
        Log.d("Entry","Entry***$id")
        if(id!=null)    userId = id

        //>33
        //val workoutModel = arguments?.getSerializable("WorkoutModel", WorkoutModel::class.java)

        //<33
        val workoutModel = arguments?.getSerializable("WorkoutModel") as WorkoutModel?

        if (workoutModel != null) {
            binding.apply {
                editDate.text = workoutModel.logDate
                editTime.text = workoutModel.time
                editDuration.setText(workoutModel.duration)
                editDistance.setText(workoutModel.distance.toString())
                editWeight.setText(workoutModel.weight.toString())
                editPlace.setText(workoutModel.place)
                editRemark.setText(workoutModel.remark)
                yy = workoutModel.year
                mm = workoutModel.month
                dd = workoutModel.day

                btnActivityAdd.text = "Edit"
            }
        }


        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item_view, activityType)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerActivityType.adapter = spinnerAdapter
        binding.spinnerActivityType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                )
                {
                    selectedType = activityType[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //do nothing
                }
            }


        val calendar = Calendar.getInstance()
        binding.editDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, y, m, d ->
                    val cal = Calendar.getInstance()
                    cal.set(y, m, d)

                    yy = y
                    mm = m + 1
                    dd = d

                    val dateFormat = SimpleDateFormat("dd/MM//yy", Locale.getDefault())
                    val selectedDate = dateFormat.format(cal.time)
                    binding.editDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }


        binding.editTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(requireContext(),
                { _, hr, min ->
                    val cal = Calendar.getInstance()
                    cal.set(Calendar.HOUR_OF_DAY, hr)
                    cal.set(Calendar.MINUTE, min)

                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val selectedTime = timeFormat.format(cal.time)
                    binding.editTime.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        }


        binding.btnActivityAdd.setOnClickListener {
            val type = selectedType
            val logDate = binding.editDate.text.toString()
            val day = dd
            val month = mm
            val year = yy
            val time = binding.editTime.text.toString()
            val duration = binding.editDuration.text.toString()
            val distance = binding.editDistance.text.toString()
            val weight = binding.editWeight.text.toString()
            val place = binding.editPlace.text.toString()
            val remark = binding.editRemark.text.toString()

            if (workoutModel == null) {
                val workout = WorkoutModel(
                    0,
                    type,
                    logDate,
                    day,
                    month,
                    year,
                    time,
                    duration.toInt(),
                    distance.toDouble(),
                    weight.toDouble(),
                    place,
                    remark,
                    userId
                )
                saveWorkout(workout)
            } else {
                val workout = WorkoutModel(
                    workoutModel.id,
                    type,
                    logDate,
                    day,
                    month,
                    year,
                    time,
                    duration.toInt(),
                    distance.toDouble(),
                    weight.toDouble(),
                    place,
                    remark,
                    userId
                )
                editWorkout(workout)
            }

        }//entry


        binding.btnClear.setOnClickListener {
            binding.editDuration.setText("")
            binding.editDistance.setText("")
            binding.editWeight.setText("")
            binding.editPlace.setText("")
            binding.editRemark.setText("")
        }

        return binding.root
    }

    private fun editWorkout(workout: WorkoutModel) {
        val url = "http://10.0.2.2/projects/mobileapi/mobile/editWorkout.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->

                Log.d("Edit Workout Listener", "Successfully Saved!")
                val obj = JSONObject(response)
                val msg = obj.get("message").toString()
                Toast.makeText(context, "Response:$msg", Toast.LENGTH_LONG).show()
                val action = EntryFragmentDirections.actionEntryFragmentToListFragment(workout)
                findNavController().navigate(action)

            }, { error ->
                Log.d("Edit Workout Listener", "***Error:$error")

            }

        ) {
            override fun getParams(): Map<String?, String?>? {
                return mapOf(
                    "id" to workout.id.toString(),
                    "type" to workout.type,
                    "logDate" to workout.logDate,
                    "day" to workout.day.toString(),
                    "month" to workout.month.toString(),
                    "year" to workout.year.toString(),
                    "time" to workout.time,
                    "duration" to workout.duration.toString(),
                    "distance" to workout.distance.toString(),
                    "weight" to workout.weight.toString(),
                    "place" to workout.place,
                    "remark" to workout.remark,
                    "userId" to workout.userId.toString()
                )
            }
        }
        Volley.newRequestQueue(context).add(request)


    }//editWorkout


    private fun saveWorkout(workout: WorkoutModel) {

        val url = "http://10.0.2.2/projects/mobileapi/mobile/saveWorkout.php"

        val request = object : StringRequest(
            Method.POST, url,
            { response ->

                Log.d("Save Workout Listener", "Successfully Saved!")
                val obj = JSONObject(response)
                val msg = obj.get("message").toString()
                Toast.makeText(context, "Response:$msg", Toast.LENGTH_LONG).show()
                val action = EntryFragmentDirections.actionEntryFragmentToListFragment(workout)
                findNavController().navigate(action)

            }, { error ->
                Log.d("Save Workout Listener", "***Error:$error")

            }

        ) {
            override fun getParams(): Map<String?, String?>? {
                return mapOf(
                    "type" to workout.type,
                    "logDate" to workout.logDate,
                    "day" to workout.day.toString(),
                    "month" to workout.month.toString(),
                    "year" to workout.year.toString(),
                    "time" to workout.time,
                    "duration" to workout.duration.toString(),
                    "distance" to workout.distance.toString(),
                    "weight" to workout.weight.toString(),
                    "place" to workout.place,
                    "remark" to workout.remark,
                    "userId" to workout.userId.toString()
                )
            }
        }
        Volley.newRequestQueue(context).add(request)

    }//saveWorkout
}
