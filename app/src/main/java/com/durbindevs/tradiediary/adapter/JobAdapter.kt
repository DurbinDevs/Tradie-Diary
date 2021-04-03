package com.durbindevs.tradiediary.adapter

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.durbindevs.tradiediary.databinding.JobRowBinding
import com.durbindevs.tradiediary.models.Jobs

class JobAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    inner class JobViewHolder(val binding: JobRowBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        init {
            val itemView = binding.root
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
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

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val newJobList = differ.currentList[position]
        holder.binding.apply {
            tvCreatedOn.text = DateUtils.getRelativeDateTimeString(
                context,
                newJobList.dateCreated.toEpochMilli(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE.toLong(),
                0
            )
            tvJobLocation.text = newJobList.location
            tvJobTitle.text = newJobList.title

        }

    }

    override fun getItemCount() = differ.currentList.size

}

