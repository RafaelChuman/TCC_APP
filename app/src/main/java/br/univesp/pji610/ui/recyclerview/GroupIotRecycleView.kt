package br.univesp.pji610.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.databinding.RecyclerviewGroupIotBinding

class GroupIotRecycleView(
    private val context: Context,
    var groupIotOnClickEvent: (group: GroupIoT) -> Unit = {},
    groupIots: List<GroupIoT> = emptyList()
) : RecyclerView.Adapter<GroupIotRecycleView.ViewHolder>() {

    private val groupIots = groupIots.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewGroupIotBinding,
        private val groupIotOnClickEvent: (group: GroupIoT) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var groupIoT: GroupIoT

        init {
            itemView.setOnClickListener {
                if (::groupIoT.isInitialized) {
                    groupIotOnClickEvent(groupIoT)
                }
            }
        }

        fun associateItem(item: GroupIoT) {
            this.groupIoT = item
            val name = item.name
            val humidity = "${item.humidity} %"
            val temperature = "${item.temperature} ºC"
            val noBreak = "${item.noBreak} min"

            binding.recyclerviewGroupIotTextViewName.text = name
            binding.recyclerviewGroupIotTextViewHumidity.text = humidity
            binding.recyclerviewGroupIotTextViewTemperature.text = temperature
            binding.recyclerviewGroupIotTextViewNoBreak.text = noBreak


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewGroupIotBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            groupIotOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(groupIots[position])
    }

    override fun getItemCount(): Int = groupIots.size

    fun update(newItems: List<GroupIoT>) {
        notifyItemRangeRemoved(0, this.groupIots.size)
        this.groupIots.clear()
        this.groupIots.addAll(newItems)
        notifyItemInserted(this.groupIots.size)
    }

}
