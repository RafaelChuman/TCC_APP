package br.univesp.tcc.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.databinding.RecyclerviewItemBinding

class ItemRecycleView(
    private val context: Context,
    var itemOnClickEvent: (item: Item) -> Unit = {},
    items: List<Item> = emptyList()
) : RecyclerView.Adapter<ItemRecycleView.ViewHolder>() {

    private val items = items.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewItemBinding,
        private val itemOnClickEvent: (item: Item) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: Item

        init {
            itemView.setOnClickListener {
                if (::item.isInitialized) {
                    itemOnClickEvent(item)
                }
            }
        }

        fun associateItem(item: Item) {
            this.item = item
            val name = item.name
            val type = item.type

            binding.recyclerviewItemTextViewName.text = name
            binding.recyclerviewItemTextViewType.text = type
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewItemBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            itemOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Item>) {
        notifyItemRangeRemoved(0, this.items.size)
        this.items.clear()
        this.items.addAll(newItems)


        notifyItemInserted(this.items.size)
    }

}
