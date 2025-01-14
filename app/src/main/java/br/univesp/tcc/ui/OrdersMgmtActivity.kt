package br.univesp.tcc.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.database.model.OrderAndItems
import br.univesp.tcc.database.model.Orders
import br.univesp.tcc.databinding.ActivityOrdersMgmtBinding
import br.univesp.tcc.repository.OrderAndItemsRepository
import br.univesp.tcc.repository.OrdersRepository
import br.univesp.tcc.ui.activity.ORDERS_ID
import br.univesp.tcc.webclient.OrderAndItemsWebClient
import br.univesp.tcc.webclient.OrdersWebClient
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Currency

private const val TAG = "OrdersMgmtActivity"

class OrdersMgmtActivity : AuthBaseActivity() {

    private var selectedIdOnSpinner = ""
    private var selectedIdOnSpinnerClient = ""
    private var ordersID: String = ""
    private var ordersSearched = Orders()
    private var orderAndItemsCreated = mutableListOf<OrderAndItems>()
    private val format = NumberFormat.getCurrencyInstance()

    private val recycleViewAdapter by lazy {
        OrdersItemRecycleView(this)
    }

    private val binding by lazy {
        ActivityOrdersMgmtBinding.inflate(layoutInflater)
    }
    private val ordersDAO by lazy {
        DataSource.getDatabase(this).OrdersDAO()
    }
    private val orderAndItemsDAO by lazy {
        DataSource.getDatabase(this).OrderAndItemsDAO()
    }
    private val ordersWebClient by lazy {
        OrdersWebClient()
    }
    private val orderAndItemsWebClient by lazy {
        OrderAndItemsWebClient()
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

    private val orderAndItemsRepository by lazy {
        OrderAndItemsRepository(
            orderAndItemsDAO,
            orderAndItemsWebClient,
            this.baseContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate - savedInstanceState: $savedInstanceState")

        super.onCreate(savedInstanceState)

        binding.buttonAddItem.setOnClickListener {
            addItem()
        }

        format.maximumFractionDigits = 2
        format.currency = Currency.getInstance("BRL")

        configRecyclerView()

        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        lifecycleScope.launch {
            getOrdersID()
        }

        lifecycleScope.launch {
            setCarOnSpinner()
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

    private fun getTotal(){

        var total = 0.0
        val totalText = binding.textInputEditTextTotal

        if(orderAndItemsCreated.isNotEmpty()) {
            orderAndItemsCreated.map { item ->
                total += ((item.quantity * item.price) - item.discount)
            }
        }

        totalText.setText(format.format(total))
    }


    private suspend fun getOrdersID() {
        ordersID = intent.getStringExtra(ORDERS_ID) ?: ""
        Log.i(TAG, "getOrdersID - ordersID: $ordersID")

        if (ordersID.isEmpty()) return

        val ordersList = ordersRepository.getById(ordersID)

        if (ordersList.isNullOrEmpty())
        {
            binding.recyclerView.visibility = View.GONE
            binding.textInputEditTextMessage.visibility = View.VISIBLE
            return
        }

        ordersSearched = ordersList.first()

        binding.textInputEditTextStatusExecution.visibility = View.VISIBLE
        binding.textInputEditTextStatusOrder.visibility = View.VISIBLE
        binding.textInputEditTextMessage.visibility = View.GONE

        binding.textInputEditTextKm.setText(ordersSearched.km.toString())
        binding.textInputEditTextFuel.setText(ordersSearched.fuel.toString())
        binding.textInputEditTextStatusExecution.setText(ordersSearched.statusExecution.toString())
        binding.textInputEditTextStatusOrder.setText(ordersSearched.statusOrder.toString())

        selectedIdOnSpinner = ordersSearched.carId
        selectedIdOnSpinnerClient = ordersSearched.userId

        //Procedure to Get All Items from Order and Sync With the mais Val OrderList
        //The main val is used to sync the recycleView of Items
        val orderAndItemsList = orderAndItemsRepository.getOrderItems(ordersID)

        if (orderAndItemsList.isNullOrEmpty()) return

        orderAndItemsCreated.addAll(orderAndItemsList)
        Log.i(TAG, "addItem - orderAndItemsCreated: $orderAndItemsCreated")

        binding.recyclerView.visibility = View.VISIBLE
        recycleViewAdapter.update(orderAndItemsCreated)

        getTotal()
    }

    private suspend fun setCarOnSpinner() {

        val spinner = binding.spinnerCar

        val car = carDao.getAll().firstOrNull()

        if(car.isNullOrEmpty()) return

        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, car.map { it.plate })

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                selectedIdOnSpinner = car[position].carId
                selectedIdOnSpinnerClient = car[position].userId

                Log.i(TAG, "setGroupOnSpinner: $selectedIdOnSpinner")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedIdOnSpinner = car[0].carId
                selectedIdOnSpinnerClient = car[0].userId
            }
        }

        val selectedPlate = car.find { it -> it.carId ==  selectedIdOnSpinner }
        if(selectedPlate == null) return
        val position = spinnerAdapter.getPosition(selectedPlate.plate)
        spinner.setSelection(position)
    }




    private fun remove() {
        Log.i(TAG, "remove")

        lifecycleScope.launch {
            Log.i(TAG, "remove - ordersID: $ordersID")

            orderAndItemsRepository.purgeByOrderId(listOf(ordersID))
            ordersRepository.delete(listOf(ordersID))

            finish()
        }
    }

    private fun save() {

        Log.i(TAG, "save - ordersID: $ordersID")

        val ordersCreated = createNewOrders()

        if (ordersID.isEmpty()) {
            lifecycleScope.launch {
                ordersRepository.insert(ordersCreated)
                orderAndItemsRepository.insert(orderAndItemsCreated)
                Log.i(TAG, "save - ordersCreated: $ordersCreated")
            }
        } else {
            lifecycleScope.launch {
                ordersRepository.update(ordersCreated)
                orderAndItemsRepository.update(orderAndItemsCreated)
                Log.i(TAG, "save - ordersUpdated: $ordersCreated")
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
        newOrders.userId = selectedIdOnSpinnerClient

        if (ordersID.isNotEmpty()) {
            newOrders.orderId = ordersSearched.orderId
            newOrders.createdAt = ordersSearched.createdAt
            newOrders.statusExecution = "Aberta"
        }

        if(orderAndItemsCreated.isNotEmpty())
        {
            orderAndItemsCreated.map { item -> item.orderId = newOrders.orderId }
        }

        return newOrders
    }

    private fun createNewOrderAndItems(): OrderAndItems {

        Log.i(TAG, "createNewOrderAndItems")

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

        if (ordersID.isNotEmpty())
        {
            newOrderAndItems.orderId = ordersID
        }

        return newOrderAndItems
    }


    private fun addItem() {

        Log.i(TAG, "addItem")

        val newOrderAndItems = createNewOrderAndItems()

        orderAndItemsCreated.add(newOrderAndItems)

        binding.textInputEditTextDescription.setText("")
        binding.textInputEditTextQuantity.setText("")
        binding.textInputEditTextPrice.setText("")

        Log.i(TAG, "addItem - orderAndItemsCreated: $orderAndItemsCreated")
        binding.recyclerView.visibility = View.VISIBLE
        recycleViewAdapter.update(orderAndItemsCreated)

        getTotal()
    }


    private fun configRecyclerView() {
        binding.recyclerView.adapter = recycleViewAdapter
        recycleViewAdapter.ordersItemOnClickEvent= { orderAndItems ->

            Log.i(TAG, "configRecyclerView - orderAndItems: $orderAndItems")
            orderAndItemsCreated.indexOf(orderAndItems)
            orderAndItemsCreated.remove(orderAndItems)
            recycleViewAdapter.update(orderAndItemsCreated)
            Log.i(TAG, "configRecyclerView - orderAndItemsCreated: $orderAndItemsCreated")
        }
    }
}