package br.univesp.pji610.ui.activity

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
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityGroupIotBinding
import br.univesp.pji610.ui.recyclerview.GroupIotRecycleView
import kotlinx.coroutines.launch

class GroupIotActivity : Fragment() {

    private lateinit var binding: ActivityGroupIotBinding

    private val adapter by lazy {
        GroupIotRecycleView(requireContext())
    }

    private val groupIoTDao by lazy {
        DataSource.instance(requireContext()).groupIoTDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityGroupIotBinding.inflate(inflater, container, false)

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

        binding.activityGroupIotFab.setOnClickListener {
            setFab(it)
        }
    }


    private suspend fun getAllGroupIoTOfUser(userId: String) {
        groupIoTDao.getAll()
            .collect { items ->
                binding.activityGroupIotTextView.visibility =
                    if (items.isEmpty()) {
                        binding.activityGroupIotRecyclerview.visibility = GONE
                        VISIBLE
                    } else {
                        binding.activityGroupIotRecyclerview.visibility = VISIBLE
                        adapter.update(items)
                        GONE
                    }
            }
    }

    fun setFab(view: View) {
        val intent = Intent(requireActivity(), GroupIotMgmtActivity::class.java)
        startActivity(intent)
    }

    private fun configRecyclerView() {
        binding.activityGroupIotRecyclerview.adapter = adapter
        adapter.groupIotOnClickEvent = { groupIot ->
            val intent = Intent(requireActivity(), GroupIotMgmtActivity::class.java)
            intent.putExtra(GROUP_IOT_ID, groupIot.id)
            startActivity(intent)
        }
    }
}