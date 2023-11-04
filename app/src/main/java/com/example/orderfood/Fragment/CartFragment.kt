package com.example.orderfood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodordering.adaptar.CartAdapter

import com.example.orderfood.databinding.FragmentCartBinding
import com.example.orderfood.model.CartModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CartFragment : Fragment(

) {
    private lateinit var binding: FragmentCartBinding
    private lateinit var cartModel: CartModel
    private val db = Firebase.firestore
    private var userId: String // Để lưu trữ ID của người dùng hiện tại
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val user = auth.currentUser
        userId = user?.uid ?: ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)


        val menuItemsRef = db.collection("Cart")
        menuItemsRef.whereEqualTo("userId", userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (foodSnapshot in task.result!!) {
                        val foodData = foodSnapshot.toObject(CartModel::class.java)
                        cartModel =
                            foodData  // Gán `cartModel` với đối tượng CartModel tìm thấy đầu tiên
                        break  // Dừng sau khi tìm thấy đối tượng
                    }

                    if (cartModel != null) {
                        // Gọi hàm hiển thị dữ liệu hoặc cập nhật giao diện ở đây
                        setAdapter()
                    } else {
                        // Xử lý trường hợp không tìm thấy CartModel nếu cần
                    }
                }
            }
        return binding.root


    }

    private fun setAdapter() {
        val adapter = CartAdapter(cartModel, context)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter
    }

    companion object {

    }
}
