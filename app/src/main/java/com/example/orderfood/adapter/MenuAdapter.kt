package com.example.foodordering.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.orderfood.databinding.MenuItemBinding
import com.example.orderfood.model.FoodModel

class MenuAdapter(
    private val menuItems: List<FoodModel>, private val context: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
//                    openDetailsActivity(position)
                }
            }
        }

        //set data in to recyclerview  item menu
        fun bind(position: Int) {
            val menuItem = menuItems[position]

            binding.menuFoodName.text = menuItem.foodName
            binding.menuPrice.text = menuItem.foodPrice
            // Load your image into the ImageView, assuming `menuItem.foodImage` is a drawable resource
            val uri = Uri.parse(menuItem.foodImage)
            Glide.with(context).load(uri).into(binding.menuImage)
        }
    }
}
//        private fun openDetailsActivity(position: Int) {
//            val menuItem = menuItems[position]
//
//            val intent = Intent(context, DetaisActivity::class.java).apply {
//                putExtra("MenuItemName", menuItem.foodName)
//                putExtra("MenuItemImage", menuItem.foodImage)
//                putExtra("MenuItemDescription", menuItem.foodDescription)
//                putExtra("MenuItemPrice", menuItem.foodPrice)
//            }
//            context.startActivity(intent)
//        }
