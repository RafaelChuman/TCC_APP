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
import br.univesp.tcc.repository.CarRepository
import br.univesp.tcc.repository.UserRepository
import br.univesp.tcc.ui.activity.CAR_ID
import br.univesp.tcc.webclient.CarWebClient
import br.univesp.tcc.webclient.UserWebClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "CarMgmtActivity"

class CarMgmtActivity : AuthBaseActivity() {

    private var selectedIdOnSpinner = ""
    private var carID: String = ""
    private var carSearched = Car()

    private val binding by lazy {
        ActivityCarMgmtBinding.inflate(layoutInflater)
    }
    private val carDAO by lazy {
        DataSource.getDatabase(this).CarDAO()
    }
    private val carWebClient by lazy {
        CarWebClient()
    }
    private val userDao by lazy {
        DataSource.getDatabase(this).UserDao()
    }
    private val carRepository by lazy {
        CarRepository(
            carDAO,
            carWebClient,
            this.baseContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate - savedInstanceState: $savedInstanceState")

        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        setContentView(binding.root)

        lifecycleScope.launch() {
            launch {
                getCarID()
            }
            launch {
                setGroupOnSpinner()
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


    private suspend fun getCarID() {
        carID = intent.getStringExtra(CAR_ID) ?: ""
        Log.i(TAG, "getCarID - carID: $carID")

        if (carID.isEmpty()) return

        val carList = carRepository.getCarById(carID)

        if (carList.isNullOrEmpty()) return

        carSearched = carList.first()

        binding.textInputEditTextBrand.setText(carSearched.brand.toString())
        binding.textInputEditTextModel.setText(carSearched.model.toString())
        binding.textInputEditTextKind.setText(carSearched.kind.toString())
        binding.textInputEditTextFuel.setText(carSearched.type.toString())
        binding.textInputEditTextPlate.setText(carSearched.plate.toString())
        binding.textInputEditTextYearOfFabrication.setText(carSearched.yearOfFabrication.toString())
        binding.textInputEditTextYearOfModel.setText(carSearched.yearOfModel.toString())
        binding.textInputEditTextColor.setText(carSearched.color.toString())

    }

    private suspend fun setGroupOnSpinner() {

        val spinner = binding.spinnerGroup

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

                    selectedIdOnSpinner = group[position].userId
                    Log.i(TAG, "setGroupOnSpinner: $selectedIdOnSpinner")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedIdOnSpinner = group[0].userId
                }
            }
        }
    }


    private fun remove() {
        Log.i(TAG, "remove - carID: $carID")

        lifecycleScope.launch {

            if (carID.isNotEmpty()) {
                carRepository.delete(listOf(carID))
            }

            finish()
        }
    }

    private fun save() {

        Log.i(TAG, "save - carID: $carID")

        val carCreated = createNewCar()

        if (carID.isEmpty()) {
            lifecycleScope.launch {
                carRepository.insert(carCreated)
                Log.i(TAG, "save - carCreated: $carCreated")
            }
        } else {
            lifecycleScope.launch {
                carRepository.update(carCreated)
                Log.i(TAG, "save - carCreated: $carCreated")
            }
        }
        finish()
    }

    private fun createNewCar(): Car {

        Log.i(TAG, "createNewCar")

        val updateAt = LocalDateTime.now()

        val brand = binding.textInputEditTextBrand.text.toString()
        val kind = binding.textInputEditTextKind.text.toString()
        val type = binding.textInputEditTextFuel.text.toString()
        val model = binding.textInputEditTextModel.text.toString()
        val plate = binding.textInputEditTextPlate.text.toString()
        val yearOfFabrication = binding.textInputEditTextYearOfFabrication.text.toString()
        val yearOfModel = binding.textInputEditTextYearOfModel.text.toString()
        val color = binding.textInputEditTextColor.text.toString()

        val newCar = Car()

        newCar.brand = brand
        newCar.kind = kind
        newCar.deleted = false
        newCar.type = type
        newCar.model = model
        newCar.plate = plate
        newCar.yearOfFabrication = yearOfFabrication.toInt()
        newCar.yearOfModel = yearOfModel.toInt()
        newCar.color = color
        newCar.userId = selectedIdOnSpinner
        newCar.updated = updateAt

        if (carID.isNotEmpty()) {
            newCar.carId = carSearched.carId
            newCar.createdAt = carSearched.createdAt
        }

        return newCar
    }
}