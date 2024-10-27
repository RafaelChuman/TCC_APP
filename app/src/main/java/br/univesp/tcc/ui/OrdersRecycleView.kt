package br.univesp.tcc.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.database.model.OrdersCarUser
import br.univesp.tcc.databinding.RecyclerviewOrderBinding
import java.text.NumberFormat
import java.time.format.DateTimeFormatterBuilder

private const val TAG = "OrderRecycleView"

class OrdersRecycleView(
    private val context: Context,
    var ordersOnClickEvent: (orders: OrdersCarUser) -> Unit = {},
    ordersList: List<OrdersCarUser> = emptyList()
) : RecyclerView.Adapter<OrdersRecycleView.ViewHolder>() {

    private val orders = ordersList.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewOrderBinding,
        private val ordersOnClickEvent: (orders: OrdersCarUser) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var orders: OrdersCarUser

        private val fromDateToString = DateTimeFormatterBuilder().appendPattern("dd.MM.yy").toFormatter()

        init {
            itemView.setOnClickListener {
                if (::orders.isInitialized) {
                    ordersOnClickEvent(orders)
                }
            }
        }

        fun associateOrder(ord: OrdersCarUser) {
            Log.i(TAG, "associateUser - ord: $ord")

            this.orders = ord

            val createdAt = ord.createdAt

            binding.textViewCreatedAt.text = fromDateToString.format(createdAt)
            binding.textViewCarColor.text = ord.color
            binding.textViewCarModel.text = ord.model
            binding.textViewCarPlate.text = ord.plate
            binding.textViewUserName.text = ord.name
            binding.textViewUserCellphone.text = ord.cellphone
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

    fun update(newOrders: List<OrdersCarUser>) {
        Log.i(TAG, "update - this.user.size: ${this.orders.size}")
        notifyItemRangeRemoved(0, this.orders.size)
        this.orders.clear()
        this.orders.addAll(newOrders)

        Log.i(TAG, "update - this.user.size: ${this.orders.size}")
        notifyItemInserted(this.orders.size)
    }

}