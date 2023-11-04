package com.example.orderfood.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodordering.adaptar.MenuAdapter

import com.example.orderfood.databinding.FragmentSearchBinding
import com.example.orderfood.model.FoodModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private val originalItems: MutableList<FoodModel> = ArrayList()
    private val filteredItems: MutableList<FoodModel> = ArrayList()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        retrieveMenuItems()
        adapter = MenuAdapter(filteredItems, requireContext())
        binding.searchRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.searchRecycleView.adapter = adapter
        setupSearchView()
        return binding.root
    }

    private fun retrieveMenuItems() {
        val menuItemsRef = db.collection("foods")
        menuItemsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                originalItems.clear()
                filteredItems.clear()
                for (foodSnapshot in task.result!!) {
                    val menuItem = foodSnapshot.toObject(FoodModel::class.java)
                    originalItems.add(menuItem)
                    filteredItems.add(menuItem)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenuItems(newText.orEmpty())
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredItems.clear()
        for (item in originalItems) {
            if (item.foodName?.contains(query, ignoreCase = true) == true) {
                filteredItems.add(item)
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {
    }
}