package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityCarBinding
import br.univesp.tcc.ui.activity.CAR_ID

private const val TAG = "CarActivity"

class CarActivity : Fragment() {

    private lateinit var binding: ActivityCarBinding

    private val adapter by lazy {
        CarRecycleView(requireContext())
    }

    private val carDao by lazy {
        DataSource.getDatabase(requireContext()).CarDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ActivityCarBinding.inflate(inflater, container, false)

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityCarFloatingButton.setOnClickListener {
            setFab()
        }
    }

    private suspend fun getAllCarOfUser(userId: String) {
        carDao.getByUser(userId).collect { item ->

            Log.i(TAG, "getAllCarOfUser: $item")

            binding.activityCarTextView.visibility =
                if (item.isEmpty()) {
                    binding.activityCarRecyclerView.visibility = View.GONE
                    View.VISIBLE
                } else {
                    binding.activityCarRecyclerView.visibility = View.VISIBLE
                    adapter.update(item)
                    View.GONE
                }
        }
    }

    private fun configRecyclerView() {
        binding.activityCarRecyclerView.adapter = adapter
        adapter.carOnClickEvent = { car ->
            val intent = Intent(requireActivity(), CarMgmtActivity::class.java)
            intent.putExtra(CAR_ID, car.id)
            startActivity(intent)
        }
    }

    private fun setFab() {
        val intent = Intent(requireActivity(), CarMgmtActivity::class.java)
        startActivity(intent)
    }

}