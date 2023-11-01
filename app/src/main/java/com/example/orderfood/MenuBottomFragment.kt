package com.example.orderfood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodordering.adaptar.MenuAdapter
import com.example.orderfood.databinding.FragmentMenuBottomBinding
import com.example.orderfood.model.FoodModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MenuBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBottomBinding

    private lateinit var menuItems: MutableList<FoodModel>
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomBinding.inflate(inflater, container, false)
        retrieveMenuItems()
        return binding.root
    }

    private fun retrieveMenuItems() {
        menuItems = mutableListOf()

        val menuItemsRef = db.collection("foods")
        menuItemsRef .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (foodSnapshot in task.result!!) {
                    val menuItem = foodSnapshot.toObject(FoodModel::class.java)
                    menuItems.add(menuItem)
                }
                setAdapter()
            }
        }
    }

    private fun setAdapter() {
        Log.d("menuItems", menuItems.toString())
        val adapter = MenuAdapter(menuItems, requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }

    companion object {
    }

}