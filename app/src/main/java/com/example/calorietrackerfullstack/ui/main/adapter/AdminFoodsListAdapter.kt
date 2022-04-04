package com.example.calorietrackerfullstack.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.utils.Constants
import kotlinx.android.synthetic.main.food_item_raw.view.img_food
import kotlinx.android.synthetic.main.food_item_raw.view.tv_calories
import kotlinx.android.synthetic.main.food_item_raw.view.tv_date
import kotlinx.android.synthetic.main.food_item_raw.view.tv_product_name
import kotlinx.android.synthetic.main.food_item_raw.view.tv_time
import kotlinx.android.synthetic.main.item_admin_food_list.view.*

class AdminFoodsListAdapter(private val foodList: List<Food>, private val listener: FoodAdapterListener) :
    RecyclerView.Adapter<AdminFoodsListAdapter.AdminFoodsListViewHolder>() {

    interface FoodAdapterListener {
        fun onDeleteClick(foodData: String)
        fun onEditClick(foodData: Food)
    }

    class AdminFoodsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(foodData: Food, listener: FoodAdapterListener) {
            itemView.btn_delete.setOnClickListener { listener.onDeleteClick(foodData.foodId.toString()) }
            itemView.btn_edit.setOnClickListener { listener.onEditClick(foodData) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminFoodsListViewHolder =
        AdminFoodsListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_admin_food_list, parent, false)
        )

    override fun onBindViewHolder(holder: AdminFoodsListViewHolder, position: Int) {
        holder.itemView.tv_product_name.text = foodList[position].foodName
        holder.itemView.tv_date.text = foodList[position].date
        holder.itemView.tv_time.text = foodList[position].time
        holder.itemView.tv_calories.text = foodList[position].calorieValue
        holder.itemView.img_food.load(
            "${Constants.IMAGE_BASE_URL}${foodList[position].foodImage}"
        ) { placeholder(R.drawable.baseline_photo_24) }
        holder.bind(foodList[position], listener)
    }

    override fun getItemCount(): Int = foodList.size

}