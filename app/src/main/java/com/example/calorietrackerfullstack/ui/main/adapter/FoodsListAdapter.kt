package com.example.calorietrackerfullstack.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.calorietrackerfullstack.R
import com.example.calorietrackerfullstack.data.model.Food
import com.example.calorietrackerfullstack.utils.Constants.Companion.IMAGE_BASE_URL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.food_item_raw.view.*

class FoodsListAdapter(private val foodList: List<Food>, private val listener: OnClickListener) :
    RecyclerView.Adapter<FoodsListAdapter.FoodsListViewHolder>() {

    interface OnClickListener { fun onClick(foodData: Food) }

    class FoodsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(foodData: Food, listener: OnClickListener) {
            itemView.setOnClickListener {
                listener.onClick(foodData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsListViewHolder =
        FoodsListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.food_item_raw, parent, false)
        )

    override fun onBindViewHolder(holder: FoodsListViewHolder, position: Int) {
        holder.itemView.tv_product_name.text = foodList[position].foodName
        holder.itemView.tv_date.text = foodList[position].date
        holder.itemView.tv_time.text = foodList[position].time
        holder.itemView.tv_calories.text = foodList[position].calorieValue
        holder.itemView.img_food.load(
            "$IMAGE_BASE_URL${foodList[position].foodImage}"
        ) { placeholder(R.drawable.baseline_photo_24) }
        holder.bind(foodList[position], listener)
    }

    override fun getItemCount(): Int = foodList.size

}
//class FoodsListAdapter(private val onClickListener: OnClickListener) :
//    ListAdapter<Food, FoodsListAdapter.FoodsViewHolder>(DiffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder =
//        FoodsViewHolder(FoodItemRawBinding.inflate(LayoutInflater.from(parent.context)))
//
//    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
//        val foodData = getItem(position)
//        holder.itemView.setOnClickListener { onClickListener.onClick(foodData) }
//        holder.bind(foodData)
//    }
//
//    class FoodsViewHolder(private var binding: FoodItemRawBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(foodData: Food) {
//            binding.tvProductName.text = foodData.foodName
//            binding.tvDate.text = foodData.date
//            binding.tvTime.text = foodData.time
//            binding.tvCalories.text = foodData.calorieValue.toString()
////            if (foodData.foodImage.isNotEmpty()) {
////                Picasso.get().load(foodData.foodImage).into(binding.imgFood)
////            }
//        }
//    }
//
//    companion object DiffCallback : DiffUtil.ItemCallback<Food>() {
//        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
//            return oldItem.foodId == newItem.foodId
//        }
//
//        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
//            return oldItem.foodId == newItem.foodId
//        }
//    }
//
//    class OnClickListener(val clickListener: (foodData: Food) -> Unit) {
//        fun onClick(foodData: Food) = clickListener(foodData)
//    }
//}