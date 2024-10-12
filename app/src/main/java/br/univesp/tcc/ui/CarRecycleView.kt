package br.univesp.tcc.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.CarUser
import br.univesp.tcc.databinding.RecyclerviewCarBinding

private const val TAG = "CarRecycleView"

class CarRecycleView(
    private val context: Context,
    var carOnClickEvent: (car: CarUser) -> Unit = {},
    carList: List<CarUser> = emptyList()
) : RecyclerView.Adapter<CarRecycleView.ViewHolder>() {

    private val cars = carList.toMutableList()

    inner class ViewHolder(
        private val binding: RecyclerviewCarBinding,
        private val carOnClickEvent: (car: CarUser) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var car: CarUser

        init {
            itemView.setOnClickListener {
                if (::car.isInitialized) {
                    carOnClickEvent(car)
                }
            }
        }

        fun associateItem(item: CarUser) {
            Log.i(TAG, "associateItem: $item")

            this.car = item

            val brand = binding.textViewBrand
            val model = binding.textViewModel
            val kind = binding.textViewKind
            val type = binding.textViewFuel
            val plate = binding.textViewPlate
            val color = binding.textViewColor
            val yearOfFabrication = binding.textViewYearOfFabrication
            val yearOfModel = binding.textViewYearOfModel
            val userName = binding.textViewUserName

            brand.text = item.brand
            model.text = item.model
            kind.text = item.kind
            type.text = item.type
            plate.text = item.plate
            color.text = item.color
            yearOfFabrication.text = item.yearOfFabrication.toString()
            yearOfModel.text = item.yearOfModel.toString()
            userName.text = item.name

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            RecyclerviewCarBinding.inflate(
                LayoutInflater.from(context)
            ),
            carOnClickEvent
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.associateItem(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    fun update(newItems: List<CarUser>) {
        notifyItemRangeRemoved(0, this.cars.size)
        this.cars.clear()
        this.cars.addAll(newItems)
        notifyItemInserted(this.cars.size)
    }

}