package com.example.chatapp

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso


class FoodAdapter(val context : Context,private val productArrayList: List<FoodItemItem>) :
    RecyclerView.Adapter<FoodAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.foof_details_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productArrayList[position]
        holder.title.text = currentItem.title
        Picasso.get().load(currentItem.image).into(holder.image)
        holder.calories.text = currentItem.calories.toString()
        holder.fat.text = currentItem.fat
        holder.protein.text = currentItem.protein
        holder.carb.text = currentItem.carbs
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.name_of_food)
        val image: ShapeableImageView = itemView.findViewById(R.id.productImage)
        val calories: TextView = itemView.findViewById(R.id.Calories)
        val fat: TextView = itemView.findViewById(R.id.fat)
        val carb: TextView = itemView.findViewById(R.id.carb)
        val protein: TextView = itemView.findViewById(R.id.Protein)
    }
}
