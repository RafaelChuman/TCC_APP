package br.univesp.pji610.ui.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.database.model.IoT_GroupIoT
import br.univesp.pji610.databinding.RecyclerviewIotBinding

class IoTRecycleView(
    private val context: Context,
    var iotOnClickEvent: (ioT: IoT_GroupIoT) -> Unit = {},
    ioTs: List<IoT_GroupIoT> = emptyList()
) : RecyclerView.Adapter<IoTRecycleView.ViewHolder>() {

    private val ioTs = ioTs.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewIotBinding,
        private val iotOnClickEvent: (ioT: IoT_GroupIoT) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var iot: IoT_GroupIoT

        init {
            itemView.setOnClickListener {
                if (::iot.isInitialized) {
                    iotOnClickEvent(iot)
                }
            }
        }

        fun associateItem(item: IoT_GroupIoT) {
            Log.i("getAllIoTOfUser", "associateItem(): $item")
            this.iot = item
            val name = binding.recyclerviewIotItemsTextViewName
            val groupName = binding.recyclerviewIotItemsTextViewGroupName
            val humidity = binding.recyclerviewIotItemsTextViewGroupHumidity
            val noBreak = binding.recyclerviewIotItemsTextViewGroupNoBreak
            val temperature = binding.recyclerviewIotItemsTextViewGroupTemperature

            name.text = item.ioT.name
            groupName.text = item.groupIoT.name
            temperature.text =  item.groupIoT.temperature.toString()
            humidity.text =  item.groupIoT.humidity.toString()
            noBreak.text =  item.groupIoT.noBreak.toString()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewIotBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            iotOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(ioTs[position])
    }

    override fun getItemCount(): Int = ioTs.size

    fun update(newItems: List<IoT_GroupIoT>) {
        notifyItemRangeRemoved(0, this.ioTs.size)
        this.ioTs.clear()
        this.ioTs.addAll(newItems)
        notifyItemInserted(this.ioTs.size)
    }

}
