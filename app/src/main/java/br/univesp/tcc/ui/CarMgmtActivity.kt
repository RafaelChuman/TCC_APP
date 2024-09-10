package br.univesp.tcc.ui

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
import br.univesp.tcc.databinding.ActivityCarMgmtBinding
import br.univesp.tcc.ui.activity.CAR_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "CarMgmtActivity"

class CarMgmtActivity : AuthBaseActivity() {

    private var carID: String = ""

    private val carDAO by lazy {
        DataSource.getDatabase(this).CarDAO()
    }

    private val userDao by lazy {
        DataSource.getDatabase(this).UserDao()
    }

    private var selectedIdOnSpinner = ""

    private val binding by lazy {
        ActivityCarMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.activityCarMgmtToolbar)

        setContentView(binding.root)

        getIotID()

        lifecycleScope.launch(Dispatchers.IO) {
            launch {
                getIotFromDataSource()
            }
            launch {
                setGroupOnSpinner()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mgmt_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mgmt_menu_save -> {
                save()
            }

            R.id.mgmt_menu_remove -> {
                remove()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getIotID() {
        carID = intent.getStringExtra(CAR_ID).toString()
    }


    private suspend fun getIotFromDataSource() {
        carID.let { id ->
            carDAO.getById(listOf(id)).map { car ->
                carID = car.id
                binding.activityCarEditTextName.setText(car.model)
            }
        }
    }

    private suspend fun setGroupOnSpinner() {

        val spinner = binding.activityCarMgmtSpinnerGroup

        userDao.getAll().filterNotNull().collect { group ->

            val spinnerAdapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, group.map { it.name })

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
        lifecycleScope.launch {
            if(carID.isNotEmpty()) {
                carDAO.remove(listOf(carID))


            }

            finish()
        }
    }

    private fun save() {

        val car = createNewCar()

        lifecycleScope.launch {

            Log.i(TAG, "save: $car")
            carDAO.save(listOf(car))

            finish()
        }
    }


    private fun createNewCar(): Car {
        val brand = binding.activityCarEditTextName.text.toString()

        val newCar = Car()

        newCar.yearOfFabrication = 2007
        newCar.kind = "Passeio"
        newCar.brand = brand
        newCar.updated = LocalDateTime.now()
        newCar.deleted = false
        newCar.type = "automóvel"
        newCar.model = "Astra Sedan"
        newCar.plate = "FFB6162"
        newCar.yearOfModel = 2007
        newCar.color = "Prata"
        newCar.userId = selectedIdOnSpinner
        newCar.createdAt = LocalDateTime.now()

        if (carID.isNotEmpty()) {
            newCar.createdAt = LocalDateTime.now()
            newCar.id = carID
        }

        return newCar
    }
}