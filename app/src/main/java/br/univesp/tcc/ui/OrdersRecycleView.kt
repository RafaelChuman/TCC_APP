package br.univesp.tcc.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.databinding.RecyclerviewOrderBinding

private const val TAG = "OrderRecycleView"

class OrdersRecycleView(
    private val context: Context,
    var ordersOnClickEvent: (orders: Orders) -> Unit = {},
    ordersList: List<Orders> = emptyList()
) : RecyclerView.Adapter<OrdersRecycleView.ViewHolder>() {

    private val orders = ordersList.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewOrderBinding,
        private val ordersOnClickEvent: (orders: Orders) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var orders: Orders

        init {
            itemView.setOnClickListener {
                if (::orders.isInitialized) {
                    ordersOnClickEvent(orders)
                }
            }
        }

        fun associateOrder(ord: Orders) {
            Log.i(TAG, "associateUser - ord: $ord")

            this.orders = ord

            val id = ord.id
            val createdAt = ord.createdAt
            val carId = ord.carId
            val userId = ord.userId


            binding.textViewId.text = id
            binding.textViewCreatedAt.text = createdAt.toString()
            binding.textViewCarColor.text = carId
            binding.textViewCarModel.text = carId
            binding.textViewCarPlate.text = carId
            binding.textViewUserName.text = userId
            binding.textViewUserCellphone.text = userId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewOrderBinding.inflate(
                LayoutInflater.from(context)
            ),
            ordersOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder - orders[position]: ${orders[position]}")
        holder.associateOrder(orders[position])
    }

    override fun getItemCount(): Int
    {
        Log.i(TAG, "getItemCount - orders.size: ${orders.size}")
        return orders.size
    }

    fun update(newOrders: List<Orders>) {
        Log.i(TAG, "update - this.user.size: ${this.orders.size}")
        notifyItemRangeRemoved(0, this.orders.size)
        this.orders.clear()
        this.orders.addAll(newOrders)

        Log.i(TAG, "update - this.user.size: ${this.orders.size}")
        notifyItemInserted(this.orders.size)
    }

}