package com.example.inov_task.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inov_task.databinding.ColorsItemBinding
import com.example.inov_task.databinding.ViewpagerItemBinding

class ViewPagerAdapter(private val imageList: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.viewPagerHolder>() {

    inner class viewPagerHolder(val binding: ViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Image set to viewpager Item
        fun setData(url: String) {
            Glide.with(binding.root.context)
                .load(url)
                .into(binding.viewpagerImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewPagerHolder {
        val binding = ViewpagerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return viewPagerHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: viewPagerHolder, position: Int) {
        holder.setData(imageList[position])
    }
}