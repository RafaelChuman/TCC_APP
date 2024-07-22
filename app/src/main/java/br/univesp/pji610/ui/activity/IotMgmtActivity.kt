package br.univesp.pji610.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import br.univesp.pji610.R
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.databinding.ActivityIotMgmtBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class IotMgmtActivity : AuthBaseActivity() {

    private var iotID: String? = null

    private val iotDAO by lazy {
        DataSource.instance(this).iotTDAO()
    }

    private val groupIoTDAO by lazy {
        DataSource.instance(this).groupIoTDAO()
    }

    private var selectedIdOnSpinner = ""

    private val binding by lazy {
        ActivityIotMgmtBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.activityIotMgmtToolbar)

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
        iotID = intent.getStringExtra(IOT_ID)
    }


    private suspend fun getIotFromDataSource() {
        iotID?.let { id ->
            iotDAO.getById(id)
                ?.filterNotNull()
                ?.collect { iot ->
                    iotID = iot.id
                    binding.activityIotEditTextName.setText(iot.name)
                }
        }
    }

    private suspend fun setGroupOnSpinner() {

        val spinner = binding.activityIotMgmtSpinnerGroup

        groupIoTDAO.getName().filterNotNull()
            .collect { group ->

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
                    Log.i("IoTMgmt", "createNewIot setGroupOnSpinner: $selectedIdOnSpinner")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedIdOnSpinner = group[0].id
                }
            }
        }
    }


    private fun remove() {
        lifecycleScope.launch {
            iotID?.let { id ->
                iotDAO.remove(id)
            }
            finish()
        }
    }

    private fun save() {

        val iot = createNewIot()

        lifecycleScope.launch {

            Log.i("IoT Mgmt", "Save IoT: $iot")
            iotDAO.save(iot)

            finish()
        }
    }


    private fun createNewIot(): IoT {
        val name = binding.activityIotEditTextName.text.toString()

        return iotID?.let { id ->
            IoT(
                id = id,
                name = name,
                groupId = selectedIdOnSpinner,
                createdAt = LocalDateTime.now().toString()
            )
        } ?: IoT(
            name = name,
            groupId = selectedIdOnSpinner,
            createdAt = LocalDateTime.now().toString()
        )
    }
}