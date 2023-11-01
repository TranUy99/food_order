package com.example.orderfood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodordering.adaptar.CartAdapter

import com.example.orderfood.R
import com.example.orderfood.databinding.FragmentCartBinding


class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater,container,false)
        val cartFoodName = listOf("Burger","sandwich","momo","item","banh mi")
        val cartItemPrice = listOf("$5","$6","$7","$8","$9")
        val cartImage = listOf(
            R.drawable.banhmi1,
            R.drawable.banhmi2,
            R.drawable.banhmi3,
            R.drawable.banhmi4,
            R.drawable.banhmi3,)
        val adapter = CartAdapter(ArrayList(cartFoodName),ArrayList(cartItemPrice),ArrayList(cartImage))
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
        return binding.root

    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {

            }
    }
}