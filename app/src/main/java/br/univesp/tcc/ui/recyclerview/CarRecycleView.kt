package br.univesp.tcc.ui.recyclerview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.databinding.RecyclerviewCarBinding

private const val TAG = "CarRecycleView"

class CarRecycleView(
    private val context: Context,
    var carOnClickEvent: (car: Car) -> Unit = {},
    car: List<Car> = emptyList()
) : RecyclerView.Adapter<CarRecycleView.ViewHolder>() {

    private val cars = car.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewCarBinding,
        private val carOnClickEvent: (car: Car) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var car: Car

        init {
            itemView.setOnClickListener {
                if (::car.isInitialized) {
                    carOnClickEvent(car)
                }
            }
        }

        fun associateItem(item: Car) {
            Log.i(TAG, "associateItem: $item")

            this.car = item

            val brand = binding.recyclerviewCarTextViewName
            val model = binding.recyclerviewCarTextViewName
            val kind = binding.recyclerviewCarTextViewName
            val type = binding.recyclerviewCarTextViewName
            val plate = binding.recyclerviewCarTextViewName
            val color = binding.recyclerviewCarTextViewName
            val yearOfFabrication = binding.recyclerviewCarTextViewName
            val yearOfModel = binding.recyclerviewCarTextViewName

            brand.text = item.brand
            model.text = item.model
            kind.text = item.kind
            type.text = item.type
            plate.text = item.plate
            color.text = item.color
            yearOfFabrication.text = item.yearOfFabrication.toString()
            yearOfModel.text = item.yearOfModel.toString()

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewCarBinding
                .inflate(
                    LayoutInflater.from(context)
                ),
            carOnClickEvent
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    fun update(newItems: List<Car>) {
        notifyItemRangeRemoved(0, this.cars.size)
        this.cars.clear()
        this.cars.addAll(newItems)
        notifyItemInserted(this.cars.size)
    }

}
