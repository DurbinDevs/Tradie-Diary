package com.durbindevs.tradiediary.adapter

import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.durbindevs.tradiediary.R
import com.durbindevs.tradiediary.databinding.JobRowBinding
import com.durbindevs.tradiediary.models.Jobs

class JobAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(val binding: JobRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val job = differ.currentList[position]
                        listener.onItemClick(job)
                    }
                }
                ivIsCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val job = differ.currentList[position]
                        listener.onCompleteCircleClick(job, isComplete = true)
                    }
                }
            }
        }

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Jobs>() {
        override fun areItemsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        return JobViewHolder(
            JobRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: JobAdapter.JobViewHolder, position: Int) {
        val newJobList = differ.currentList[position]
        holder.binding.apply {
            tvCreatedOn.text = DateUtils.getRelativeDateTimeString(
                context,
                newJobList.dateCreated,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE.toLong(),
                0
            )
            tvJobLocation.text = newJobList.location
            tvJobTitle.text = newJobList.title

            ivIsCompleted.setOnClickListener {
                listener.onCompleteCircleClick(newJobList, isComplete = true)
            }
            if (newJobList.isCompleted) {
                ivIsCompleted.setColorFilter(context.getColor(R.color.green))
                ivDone.isVisible = true

            }
        }

    }

    override fun getItemCount() = differ.currentList.size


    interface OnItemClickListener {
        fun onItemClick(job: Jobs)
        fun onCompleteCircleClick(job: Jobs, isComplete: Boolean)
    }
}

