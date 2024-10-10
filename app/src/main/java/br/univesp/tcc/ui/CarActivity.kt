package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityCarBinding
import br.univesp.tcc.repository.CarRepository
import br.univesp.tcc.repository.UserRepository
import br.univesp.tcc.ui.activity.CAR_ID
import br.univesp.tcc.webclient.CarWebClient
import br.univesp.tcc.webclient.UserWebClient
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private const val TAG = "CarActivity"

class CarActivity : Fragment() {

    private lateinit var binding: ActivityCarBinding

    private val recycleViewAdapter by lazy {
        CarRecycleView(requireContext())
    }

    private val carDao by lazy {
        DataSource.getDatabase(requireContext()).CarDAO()
    }

    private val carWebClient by lazy {
        CarWebClient()
    }

    private val ctx by lazy {
        requireContext()
    }

    private val carRepository by lazy {

        CarRepository(
            carDao,
            carWebClient,
            ctx
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityCarBinding.inflate(inflater, container, false)

        configRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllCar()
            }
        }
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.floatingButton.setOnClickListener {
            setFab()
        }
    }

    private suspend fun getAllCar() {
        val car = carRepository.getAll()

        Log.i(TAG, "getAllCarOfUser: $car")

        if(car.isNullOrEmpty()) {
            binding.textView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE

            Log.i(TAG, "getAllUser - user: user.isNullOrEmpty()")
        }
        else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.textView.visibility =  View.GONE

            Log.i(TAG, "getAllUser - user: ${car.toString()}")
            recycleViewAdapter.update(car)
        }
    }

    private fun setFab() {
        val intent = Intent(requireActivity(), CarMgmtActivity::class.java)
        startActivity(intent)
    }

    private fun configRecyclerView() {
        binding.recyclerView.adapter = recycleViewAdapter
        recycleViewAdapter.carOnClickEvent = { car ->
            val intent = Intent(requireActivity(), CarMgmtActivity::class.java)

            intent.putExtra(CAR_ID, car.carId)
            startActivity(intent)
        }
    }
}