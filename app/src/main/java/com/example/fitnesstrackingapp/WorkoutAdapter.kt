package com.example.fitnesstrackingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesstrackingapp.databinding.WorkoutItemViewBinding

class WorkoutAdapter(val workoutList: ArrayList<WorkoutModel>):
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>()
{
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): WorkoutViewHolder {
        val itemBinding = WorkoutItemViewBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return WorkoutViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: WorkoutViewHolder,
        position: Int
    ) {
        val workoutItem = workoutList[position];
        holder.itemBinding.txtWorkoutType.text = workoutItem.type
        holder.itemBinding.txtWorkoutDate.text = workoutItem.logDate
        holder.itemBinding.txtWorkoutDuration.text = workoutItem.duration.toString() + " minutes"
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(val itemBinding: WorkoutItemViewBinding) : RecyclerView.ViewHolder(itemBinding.root)
}