package br.univesp.tcc.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import br.univesp.tcc.databinding.ActivityCarBinding
import br.univesp.tcc.databinding.ActivityInsertUserBinding
import br.univesp.tcc.ui.recyclerview.CarRecycleView
import kotlinx.coroutines.launch

private const val TAG = "CarActivity"

class CarActivity : Fragment() {

    private lateinit var binding: ActivityCarBinding

    private val adapter by lazy {
        CarRecycleView(requireContext())
    }

    private val carDao by lazy {
        DataSource.instance(requireContext()).CarDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        super.onCreate(savedInstanceState)
//
//        binding = ActivityCarBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        binding = ActivityCarBinding.inflate(inflater, container, false)

//        configRecyclerView()
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                getAllCarOfUser("")
//            }
//        }
//
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityCarFloatingButton.setOnClickListener {
            setFab()
        }
    }

    private suspend fun getAllCarOfUser(userId: String) {
        carDao.getAllByUser(userId)?.collect { item ->

            Log.i(TAG,"getAllCarOfUser: ${item}")
            binding.activityCarTextView.visibility =
                if (item.isEmpty()) {
                    binding.activityCarRecyclerView.visibility = GONE
                    VISIBLE
                } else {
                    binding.activityCarRecyclerView.visibility = VISIBLE
                    adapter.update(item)
                    GONE
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

    fun setFab() {
        val intent = Intent(requireActivity(), CarMgmtActivity::class.java)
        startActivity(intent)
    }

}

//    override fun onCreateView(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(binding.root)
//
//        configRecyclerView()
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                getAllIoTOfUser("")
//            }
//        }
//    }
//
//
////    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
////        menuInflater.inflate(R.menu.iot_menu, menu)
////        return super.onCreateOptionsMenu(menu)
////    }
////
////    override fun onOptionsItemSelected(item: MenuItem): Boolean {
////        when (item.itemId) {
////            R.id.iot_menu_logout -> {
////                lifecycleScope.launch {
////                    logout()
////                }
////            }
////        }
////        return super.onOptionsItemSelected(item)
////    }
//
//    private suspend fun getAllIoTOfUser(userId: String) {
//        ioTDao.getAll()
//            .collect { items ->
//                binding.activityIotActivityTextView.visibility =
//                    if (items.isEmpty()) {
//                        binding.activityIotActivityRecyclerView.visibility = GONE
//                        VISIBLE
//                    } else {
//                        binding.activityIotActivityRecyclerView.visibility = VISIBLE
//                        adapter.update(items)
//                        GONE
//                    }
//            }
//    }
//
//    fun setFab(view: View) {
//        RedirectTo(IotMgmtActivity::class.java)
//    }
//
//    private fun configRecyclerView() {
//        binding.activityIotActivityRecyclerView.adapter = adapter
//        adapter.iotOnClickEvent = { iot ->
//            RedirectTo(IotMgmtActivity::class.java) {
//                putExtra(IOT_ID, iot.id)
//            }
//        }
//    }
//
//}