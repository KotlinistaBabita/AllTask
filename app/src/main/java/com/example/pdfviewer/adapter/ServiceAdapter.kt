package com.example.pdfviewer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdfviewer.R
import com.example.pdfviewer.local.ServiceItemEntity
import com.example.pdfviewer.utils.ServiceItemDiffCallback

class ServiceAdapter(
    private val onDeleteClick: (ServiceItemEntity) -> Unit,
    private var onEditClick: (ServiceItemEntity) -> Unit
) : ListAdapter<ServiceItemEntity, ServiceAdapter.ViewHolder>(ServiceItemDiffCallback){


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvGeneration = view.findViewById<TextView>(R.id.tvGeneration)
        private val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        private val tvCapacity = view.findViewById<TextView>(R.id.tvCapacity)
        private val tvScreenSize = view.findViewById<TextView>(R.id.tvScreenSize)
        private val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        private val tvColor = view.findViewById<TextView>(R.id.tvColor)
        private val tvStrapColour = view.findViewById<TextView>(R.id.tvStrapColour)
        private val tvCaseSize = view.findViewById<TextView>(R.id.tvCaseSize)
        private val tvYear = view.findViewById<TextView>(R.id.tvYear)
        private val tvCpuModel = view.findViewById<TextView>(R.id.tvCpuModel)
        private val tvHardDiskSize = view.findViewById<TextView>(R.id.tvHardDiskSize)
        private val tvCapacityGB = view.findViewById<TextView>(R.id.tvCapacityGB)
        private val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        private val btnDelete = view.findViewById<Button>(R.id.btnDelete)
        private val tvName = view.findViewById<TextView>(R.id.tvNAme)

        fun bind(item: ServiceItemEntity) {
            val data = item.data

            @SuppressLint("SetTextI18n")
            fun setTextOrHide(textView: TextView, label: String, value: String?) {
                if (!value.isNullOrBlank()) {
                    textView.visibility = View.VISIBLE
                    textView.text = "$label: $value"
                } else {
                    textView.visibility = View.GONE
                }
            }
            fun setTextOrHideInt(textView: TextView, label: String, value: Int?) {
                if (value != null) {
                    textView.visibility = View.VISIBLE
                    textView.text = "$label: $value"
                } else {
                    textView.visibility = View.GONE
                }
            }

            setTextOrHide(tvGeneration, "Generation", data?.generation)
            setTextOrHide(tvName, "Name", item.name)
            setTextOrHide(tvPrice, "Price", data?.price)
            setTextOrHide(tvCapacity, "Capacity", data?.capacity)
            setTextOrHide(tvScreenSize, "Screen Size", data?.screenSize)
            setTextOrHide(tvDescription, "Description", data?.description)
            setTextOrHide(tvColor, "Color", data?.color)
            setTextOrHide(tvStrapColour, "Strap Colour", data?.strapColour)
            setTextOrHide(tvCaseSize, "Case Size", data?.caseSize)
            setTextOrHideInt(tvYear, "Year", data?.year)
            setTextOrHide(tvCpuModel, "CPU Model", data?.cpuModel)
            setTextOrHide(tvHardDiskSize, "Hard Disk Size", data?.hardDiskSize)
            setTextOrHideInt(tvCapacityGB, "Capacity GB", data?.capacityGB)

            btnUpdate.setOnClickListener {
                onEditClick(item)


            }



            btnDelete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}