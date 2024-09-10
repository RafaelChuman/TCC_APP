package br.univesp.tcc.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityItemBinding
import br.univesp.tcc.ui.ItemRecycleView
import kotlinx.coroutines.launch

private const val TAG = "ItemActivity"

class ItemActivity : Fragment() {

    private lateinit var binding: ActivityItemBinding

    private val adapter by lazy {
        ItemRecycleView(requireContext())
    }

    private val itemDao by lazy {
        DataSource.getDatabase(requireContext()).ItemDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityItemBinding.inflate(inflater, container, false)

        configRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllGroupIoTOfUser("")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.activityItemFab.setOnClickListener {
            setFab()
        }
    }


    private suspend fun getAllGroupIoTOfUser(userId: String) {
        itemDao.getAll()?.collect { items ->
                binding.activityItemTextView.visibility =
                    if (items.isEmpty()) {
                        binding.activityItemRecyclerview.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityItemRecyclerview.visibility = VISIBLE
                        adapter.update(items)
                        GONE
                    }
            }
    }

    private fun setFab() {
        val intent = Intent(requireActivity(), ItemMgmtActivity::class.java)
        startActivity(intent)
    }

    private fun configRecyclerView() {
        binding.activityItemRecyclerview.adapter = adapter
        adapter.itemOnClickEvent = { groupIot ->
            val intent = Intent(requireActivity(), ItemMgmtActivity::class.java)

            intent.putExtra(ITEM_ID, groupIot.id)
            startActivity(intent)
        }
    }
}