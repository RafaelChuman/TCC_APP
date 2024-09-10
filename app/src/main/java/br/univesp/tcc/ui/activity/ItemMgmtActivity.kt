package br.univesp.tcc.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.univesp.tcc.R
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.Item
import br.univesp.tcc.databinding.ActivityItemMgmtBinding
import br.univesp.tcc.ui.AuthBaseActivity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

private const val TAG = "ItemMgmtActivity"

class ItemMgmtActivity : AuthBaseActivity() {

    private val binding by lazy {
        ActivityItemMgmtBinding.inflate(layoutInflater)
    }

    private var itemId: String? = null

    private val itemDAO by lazy {
        DataSource.getDatabase(this).ItemDAO()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.activityItemMgmtToolbar)

        setContentView(binding.root)

        getGroupIotId()

        lifecycleScope.launch {
            launch {
                getGroupIotFromDataSource()
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


    private fun getGroupIotId() {
        itemId = intent.getStringExtra(ITEM_ID)
    }


    private suspend fun getGroupIotFromDataSource() {
        itemId?.let {
            itemDAO.getById(it)?.filterNotNull()?.collect { item ->
                    itemId = item.id
                    val name = item.name
                    val type = item.type

                    binding.activityItemMgmtEditTextName.setText(name)
                    binding.activityItemMgmtEditTextType.setText(type)

                }
        }
    }


    private fun remove() {
        lifecycleScope.launch {
            itemId?.let { id ->
                itemDAO.remove(id)
            }
            finish()
        }
    }

    private fun save() {

        val itemCreated = createNewItem()

        lifecycleScope.launch {
            itemDAO.save(itemCreated)
            Log.i(TAG, "save: $itemCreated")

            finish()
        }
    }


    private fun createNewItem(): Item {
        val name = binding.activityItemMgmtEditTextName.text.toString()
        val type = binding.activityItemMgmtEditTextType.text.toString()

        return itemId?.let { itemId ->
            Item(
                id = itemId,
                name = name,
                type = type,
                deleted = false,
                updated = "",
                createdAt = LocalDateTime.now().toString()
            )
        } ?: Item(
            name = name,
            type = type,
            deleted = false,
            updated = "",
            createdAt = LocalDateTime.now().toString()
        )
    }



}