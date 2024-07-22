package br.univesp.pji610.ui.activity

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
import br.univesp.pji610.database.DataSource
import br.univesp.pji610.databinding.ActivityIotBinding
import br.univesp.pji610.ui.recyclerview.IoTRecycleView
import kotlinx.coroutines.launch

class IoTActivity : Fragment() {

    private lateinit var binding: ActivityIotBinding

    private val adapter by lazy {
        IoTRecycleView(requireContext())
    }

    private val ioTDao by lazy {
        DataSource.instance(requireContext()).iotTDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityIotBinding.inflate(inflater, container, false)

        configRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllIoTOfUser("")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.activityIotActivityFloatingButton.setOnClickListener {
            setFab(it)
        }
    }

    private suspend fun getAllIoTOfUser(userId: String) {
        ioTDao.getAllByUser().collect { items ->

            Log.i("getAllIoTOfUser","${items}")
            binding.activityIotActivityTextView.visibility =
                if (items.isEmpty()) {
                    binding.activityIotActivityRecyclerView.visibility = GONE
                    VISIBLE
                } else {
                    binding.activityIotActivityRecyclerView.visibility = VISIBLE
                    adapter.update(items)
                    GONE
                }
        }
    }

    private fun configRecyclerView() {
        binding.activityIotActivityRecyclerView.adapter = adapter
        adapter.iotOnClickEvent = { iot ->
            val intent = Intent(requireActivity(), IotMgmtActivity::class.java)
            intent.putExtra(IOT_ID, iot.ioT.id)
            startActivity(intent)
        }
    }

    fun setFab(view: View) {
        val intent = Intent(requireActivity(), IotMgmtActivity::class.java)
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