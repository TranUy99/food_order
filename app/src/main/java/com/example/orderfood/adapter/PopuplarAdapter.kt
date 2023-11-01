package com.example.foodordering.adaptar

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.orderfood.databinding.PopularItemBinding
import com.example.orderfood.model.FoodModel


class PopuplarAdapter(
    private val popularItems: List<FoodModel>,
    private val context: Context
) : RecyclerView.Adapter<PopuplarAdapter.PopuplarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopuplarViewHolder {
        val binding =
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PopuplarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopuplarViewHolder, position: Int) {
        val foodName = popularItems[position].foodName
        val foodImage = popularItems[position].foodImage
        val foodPrice = popularItems[position].foodPrice
        val uri = Uri.parse(foodImage)
        holder.bind(foodName, foodPrice, uri, context)
    }

    override fun getItemCount(): Int {
        return popularItems.size
    }

    class PopuplarViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodName: String?, foodPrice: String?, uri: Uri, context: Context) {
            binding.foodNamePopular.text = foodName
            binding.PricePopular.text = foodPrice
            Glide.with(context).load(uri).into(binding.popuplarImage)
        }
    }
}