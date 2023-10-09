package com.example.inov_task.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inov_task.R
import com.example.inov_task.callback.colorListener
import com.example.inov_task.databinding.ColorsItemBinding
import com.example.inov_task.model.Attribute


class colorAdapter(
    val colorList: List<Attribute>,
    val context: Context,
    val listener: colorListener
) :
    RecyclerView.Adapter<colorAdapter.MyHolder>() {

    //This position used because in colorRecyclerview we need border when item Click
    //and needed only which item clicked and others need normal item.
    var selectedPosition = -1
    var lastSelectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): colorAdapter.MyHolder {
        val binding: ColorsItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.colors_item,
            parent,
            false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val data = colorList[position]
        holder.bind(data)

        if (position == lastSelectedPosition) {
            holder.binding.colorItemImageViewBg.visibility = View.VISIBLE

            //Interface colorListener notifying activity via Interface
            listener.colorChange(colorList[position].option_id)
        } else {
            holder.binding.colorItemImageViewBg.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    inner class MyHolder(val binding: ColorsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(attribute: Attribute) {

            //Image Set via Glide.
            Glide.with(context).load(attribute.swatch_url).into(binding.colorItemImageView)
        }

        //Item Click
        init {
            itemView.setOnClickListener {
                selectedPosition = adapterPosition
                if (lastSelectedPosition == -1)
                    lastSelectedPosition = selectedPosition
                else {
                    notifyItemChanged(lastSelectedPosition)
                    lastSelectedPosition = selectedPosition
                }
                notifyItemChanged(selectedPosition)

            }
        }


    }
}