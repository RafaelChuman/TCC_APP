package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.databinding.ActivityCarMgmtBinding
import br.univesp.tcc.databinding.ActivityOrdersMgmtBinding
import br.univesp.tcc.repository.CarRepository
import br.univesp.tcc.repository.OrdersRepository
import br.univesp.tcc.ui.activity.CAR_ID
import br.univesp.tcc.ui.activity.ORDERS_ID
import br.univesp.tcc.webclient.CarWebClient
import br.univesp.tcc.webclient.OrdersWebClient
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "OrdersMgmtActivity"

class OrdersMgmtActivity : AuthBaseActivity() {

    private var selectedIdOnSpinner = ""
    private var ordersID: String = ""
    private var ordersSearched = Orders()

    private val binding by lazy {
        ActivityOrdersMgmtBinding.inflate(layoutInflater)
    }
    private val ordersDAO by lazy {
        DataSource.getDatabase(this).OrdersDAO()
    }
    private val ordersWebClient by lazy {
        OrdersWebClient()
    }
    private val carDao by lazy {
        DataSource.getDatabase(this).CarDAO()
    }
    private val ordersRepository by lazy {
        OrdersRepository(
            ordersDAO,
            ordersWebClient,
            this.baseContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate - savedInstanceState: $savedInstanceState")

        super.onCreate(savedInstanceState)

        binding.buttonAddItem.setOnClickListener {
            addItem()
        }

        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        lifecycleScope.launch() {
            launch {
                getOrdersID()
            }
            launch {
                setCarOnSpinner()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i(TAG, "onCreateOptionsMenu - menu: $menu")

        menuInflater.inflate(R.menu.mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(menu: MenuItem): Boolean {
        Log.i(TAG, "onOptionsItemSelected - menu: $menu")

        when (menu.itemId) {
            R.id.mgmt_menu_save -> {
                save()
            }

            R.id.mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(menu)
    }


    private suspend fun getOrdersID() {
        ordersID = intent.getStringExtra(ORDERS_ID) ?: ""
        Log.i(TAG, "getOrdersID - ordersID: $ordersID")

        if (ordersID.isEmpty()) return

        val ordersList = ordersRepository.getById(ordersID)

        if (ordersList.isNullOrEmpty()) return

        ordersSearched = ordersList.first()

        binding.textInputEditTextKm.setText(ordersSearched.km.toString())
        binding.textInputEditTextFuel.setText(ordersSearched.fuel.toString())
        binding.textInputEditTextStatusExecution.setText(ordersSearched.statusExecution.toString())
        binding.textInputEditTextStatusOrder.setText(ordersSearched.statusOrder.toString())

    }

    private suspend fun setCarOnSpinner() {

        val spinner = binding.spinnerCar

        carDao.getAll().filterNotNull().collect { group ->

            val spinnerAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, group.map { it.plate })

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = spinnerAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    selectedIdOnSpinner = group[position].id
                    Log.i(TAG, "setGroupOnSpinner: $selectedIdOnSpinner")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedIdOnSpinner = group[0].id
                }
            }
        }
    }




    private fun remove() {
        Log.i(TAG, "remove - ordersID: $ordersID")

        lifecycleScope.launch {

            if (ordersID.isNotEmpty()) {
                ordersRepository.delete(listOf(ordersID))
            }

            finish()
        }
    }

    private fun save() {

        Log.i(TAG, "save - ordersID: $ordersID")

        val ordersCreated = createNewOrders()

        if (ordersID.isEmpty()) {
            lifecycleScope.launch {
                ordersRepository.insert(ordersCreated)
                Log.i(TAG, "save - ordersCreated: $ordersCreated")
            }
        } else {
            lifecycleScope.launch {
                ordersRepository.update(ordersCreated)
                Log.i(TAG, "save - ordersCreated: $ordersCreated")
            }
        }
        finish()
    }

    private fun createNewOrders(): Orders {

        Log.i(TAG, "createNewOrders")

        val updateAt = LocalDateTime.now()

        val km = binding.textInputEditTextKm.text.toString()
        val fuel = binding.textInputEditTextFuel.text.toString()
        val statusOrder = binding.textInputEditTextStatusOrder.text.toString()
        val statusExecution = binding.textInputEditTextStatusExecution.text.toString()

        val newOrders = Orders()

        newOrders.km = km.toInt()
        newOrders.fuel = fuel.toDouble()
        newOrders.statusOrder = statusOrder.toBoolean()
        newOrders.statusExecution = statusExecution
        newOrders.deleted = false
        newOrders.updated = updateAt

        newOrders.carId = selectedIdOnSpinner

        if (ordersID.isNotEmpty()) {
            newOrders.id = ordersSearched.id
            newOrders.createdAt = ordersSearched.createdAt
        }

        return newOrders
    }

    private fun createNewOrderAndItems(): OrderAndItems {

        Log.i(TAG, "createNewOrderAndItems")

        val updateAt = LocalDateTime.now()

        val description = binding.textInputEditTextDescription.text.toString()
        val quantity = binding.textInputEditTextQuantity.text.toString()
        val price = binding.textInputEditTextPrice.text.toString()

        val newOrderAndItems = OrderAndItems()

        newOrderAndItems.name = description
        newOrderAndItems.price = price.toDouble()
        newOrderAndItems.type = "PÇ"
        newOrderAndItems.discount = 0.0
        newOrderAndItems.quantity = quantity.toInt()
        newOrderAndItems.unitMeasurement = ""

        return newOrderAndItems
    }


    private fun addItem() {

    }
}