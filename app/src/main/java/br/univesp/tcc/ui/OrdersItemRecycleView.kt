package br.univesp.tcc.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.databinding.RecyclerviewOrdersItemBinding

private const val TAG = "OrdersItemRecycleView"

class OrdersItemRecycleView(
    private val context: Context,
    var ordersItemOnClickEvent: (orderAndItems: OrderAndItems) -> Unit = {},
    orderAndItemsList: List<OrderAndItems> = emptyList()
) : RecyclerView.Adapter<OrdersItemRecycleView.ViewHolder>() {

    private val orderAndItems = orderAndItemsList.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewOrdersItemBinding,
        private val ordersItemOnClickEvent: (item: OrderAndItems) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var orderAndItems: OrderAndItems

        init {
            itemView.setOnClickListener {
                if (::orderAndItems.isInitialized) {
                    ordersItemOnClickEvent(orderAndItems)
                }
            }
        }

        fun associateItem(itm: OrderAndItems) {
            Log.i(TAG, "associateItem - usr: $itm")

            this.orderAndItems = itm

            val name = itm.name
            val price = itm.price
            val quantity = itm.quantity


            binding.textViewName.text = name
            binding.textViewPrice.text = price.toString()
            binding.textViewQuantity.text = quantity.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewOrdersItemBinding.inflate(
                LayoutInflater.from(context)
            ),
            ordersItemOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder - users[position]: ${orderAndItems[position]}")
        holder.associateItem(orderAndItems[position])
    }

    override fun getItemCount(): Int
    {
        Log.i(TAG, "getItemCount - users.size: ${orderAndItems.size}")
        return orderAndItems.size
    }

    fun update(newUser: List<OrderAndItems>) {
        Log.i(TAG, "update - this.user.size: ${this.orderAndItems.size}")
        notifyItemRangeRemoved(0, this.orderAndItems.size)
        this.orderAndItems.clear()
        this.orderAndItems.addAll(newUser)

        Log.i(TAG, "update - this.user.size: ${this.orderAndItems.size}")
        notifyItemInserted(this.orderAndItems.size)
    }

}