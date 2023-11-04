package com.example.orderfood.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodordering.adaptar.PopuplarAdapter
import com.example.orderfood.MenuBottomFragment
import com.example.orderfood.R
import com.example.orderfood.databinding.FragmentHomeBinding
import com.example.orderfood.model.FoodModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var menuItems: MutableList<FoodModel>
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewAllMenu.setOnClickListener {
            val bottomSheetDialogFragment = MenuBottomFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, "Test")
        }

        retrieveMenuItems()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.food_slap_screen, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.start_activity, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.images, ScaleTypes.FIT))

        var imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun retrieveMenuItems() {
        menuItems = mutableListOf()

        val menuItemsRef = db.collection("foods")
        menuItemsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (foodSnapshot in task.result!!) {
                    val foodData = foodSnapshot.toObject(FoodModel::class.java)
                    val menuItem = FoodModel(
                        foodSnapshot.id,
                        foodData.foodName,
                        foodData.foodPrice,
                        foodData.foodDescription,
                        foodData.foodImage
                    )

                    menuItems.add(menuItem)
                }


                setAdapter()
            }
        }

    }

    private fun setAdapter() {

        val adapter = PopuplarAdapter(menuItems, requireContext())
        binding.PopularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter = adapter
    }

    companion object {


    }
}