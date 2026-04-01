package com.example.fitnesstrackingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstrackingapp.databinding.WorkoutItemViewBinding

class WorkoutAdapter(val workoutList: ArrayList<WorkoutModel>,val showWorkoutDetails:(WorkoutModel)->Unit,
                     val deleteWorkout:(id:Int)->Unit,
                     val editWorkout:(WorkoutModel)->Unit):
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutViewHolder {
        val itemBinding= WorkoutItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WorkoutViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: WorkoutViewHolder,
        position: Int
    ) {
        val workoutItem = workoutList[position]
        holder.itemBinding.txtWorkoutType.text = workoutItem.type
        holder.itemBinding.txtWorkoutDate.text = "Date: "+workoutItem.logDate
        holder.itemBinding.txtWorkoutDuration.text = "Duration: "+workoutItem.duration.toString()+" minutes"

        holder.itemBinding.btnShowWorkoutDetail.setOnClickListener {
            showWorkoutDetails(workoutItem)
        }
        holder.itemBinding.btnDeleteWorkout.setOnClickListener {
            deleteWorkout(workoutItem.id)
        }
        holder.itemBinding.btnEditWorkout.setOnClickListener {
            editWorkout(workoutItem)
        }

    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(val itemBinding: WorkoutItemViewBinding):
        RecyclerView.ViewHolder(itemBinding.root){}

}