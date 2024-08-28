package br.univesp.tcc.ui.activity

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
<<<<<<<< HEAD:app/src/main/java/br/univesp/tcc/ui/activity/CarMgmtActivity.kt
import br.univesp.tcc.database.model.Car
import br.univesp.tcc.databinding.ActivityCarMgmtBinding
========
import br.univesp.tcc.database.model.GroupIoT
import br.univesp.tcc.database.model.IoT
import br.univesp.tcc.databinding.ActivityIotMgmtBinding
>>>>>>>> origin/master:app/src/main/java/br/univesp/tcc/ui/activity/IotMgmtActivity.kt
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "CarMgmtActivity"

class CarMgmtActivity : AuthBaseActivity() {

    private var carID: String? = null

    private val carDAO by lazy {
        DataSource.instance(this).CarDAO()
    }

    private val userDao by lazy {
        DataSource.instance(this).UserDao()
    }

    private var selectedIdOnSpinner = ""

    private val binding by lazy {
        ActivityCarMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.activityCarMgmtToolbar)

        setContentView(
            binding.root
        )

        getIotID()

        lifecycleScope.launch {
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
        carID = intent.getStringExtra(CAR_ID)
    }


    private suspend fun getIotFromDataSource() {
        carID?.let { id ->
            carDAO.getById(id)
                ?.filterNotNull()
                ?.collect { car ->
                    carID = car.id
                    binding.activityCarEditTextName.setText(car.model)
                }
        }
    }

    private suspend fun setGroupOnSpinner() {

        val spinner = binding.activityCarMgmtSpinnerGroup

        userDao.getAll()?.filterNotNull()?.collect { group ->

            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, group.map { it.name })

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
            carID?.let { id ->
                carDAO.remove(id)
            }
            finish()
        }
    }

    private fun save() {

        val car = createNewCar()

        lifecycleScope.launch {

            Log.i(TAG, "save: $car")
            carDAO.save(car)

            finish()
        }
    }


    private fun createNewCar(): Car {
        val brand = binding.activityCarEditTextName.text.toString()

        return carID?.let { id ->
            Car(
                id = id,
                yearOfFabrication = 2007,
                kind = "Passeio",
                brand = brand,
                updated = "",
                deleted = false,
                type = "automóvel",
                model = "Astra Sedan",
                plate = "FFB6162",
                yearOfModel = 2007,
                color = "Prata",
                userId = selectedIdOnSpinner,
                createdAt = LocalDateTime.now().toString()
            )
        } ?:
            Car(
                yearOfFabrication = 2007,
                kind = "Passeio",
                brand = brand,
                updated = "",
                deleted = false,
                type = "automóvel",
                model = "Astra Sedan",
                plate = "FFB6162",
                yearOfModel = 2007,
                color = "Prata",
                userId = selectedIdOnSpinner,
                createdAt = LocalDateTime.now().toString()
            )
    }
}