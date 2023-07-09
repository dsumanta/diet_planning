package com.example.chatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val mList: List<Item>
) : RecyclerView.Adapter<ItemAdapter.LanguageViewHolder>() {
    private lateinit var myListner: ItemClickListner
    interface ItemClickListner{
        fun OnItemClick(position: Int)
    }
    fun setItemClickListner(listner: ItemClickListner){
       myListner=listner
    }
    inner class LanguageViewHolder(itemView: View,listner:ItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.search_food_name)
        val calorie: TextView = itemView.findViewById(R.id.search_food_acalorie)

        init {
            itemView.setOnClickListener {
               listner.OnItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_food_view, parent, false)
        return LanguageViewHolder(view,myListner)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val foodItem = mList[position]
        holder.calorie.text = foodItem.calories.toString()
        holder.name.text = foodItem.name.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}
