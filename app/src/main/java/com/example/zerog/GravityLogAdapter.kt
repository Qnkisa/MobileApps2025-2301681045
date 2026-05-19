package com.example.zerog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.zerog.data.local.GravityLog
import com.example.zerog.databinding.ItemGravityLogBinding

class GravityLogAdapter(
    private val onItemClick: (GravityLog) -> Unit,
    private val onDeleteClick: (GravityLog) -> Unit
) : ListAdapter<GravityLog, GravityLogAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemGravityLogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGravityLogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = getItem(position)
        with(holder.binding) {
            tvItemName.text = log.itemName
            tvEarthWeight.text = "Earth: ${"%.2f".format(log.earthWeight)} kg"
            tvCalculatedWeight.text = "Zero-G: ${"%.2f".format(log.calculatedWeight)} kg"
            tvReduction.text = "${log.gravityReduction}% reduction"
            cardRoot.setOnClickListener { onItemClick(log) }
            cardRoot.setOnLongClickListener { onDeleteClick(log); true }
            btnDelete.setOnClickListener { onDeleteClick(log) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GravityLog>() {
        override fun areItemsTheSame(old: GravityLog, new: GravityLog) = old.id == new.id
        override fun areContentsTheSame(old: GravityLog, new: GravityLog) = old == new
    }
}
