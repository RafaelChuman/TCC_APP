package br.univesp.tcc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.databinding.ActivityOrdersBinding
import br.univesp.tcc.repository.OrdersRepository
import br.univesp.tcc.ui.activity.ORDERS_ID
import br.univesp.tcc.webclient.OrdersWebClient
import kotlinx.coroutines.launch

private const val TAG = "OrderActivity"

class OrdersActivity : Fragment() {
    private lateinit var binding: ActivityOrdersBinding

    private val recycleViewAdapter by lazy {
        OrdersRecycleView(requireContext())
    }

    private val ordersDao by lazy {
        DataSource.getDatabase(requireContext()).OrdersDAO()
    }

    private val ordersWebClient by lazy {
        OrdersWebClient()
    }

    private val ctx by lazy {
        requireContext()
    }

    private val ordersRepository by lazy {
        OrdersRepository(
            ordersDao,
            ordersWebClient,
            ctx
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityOrdersBinding.inflate(inflater, container, false)

        configRecyclerView()

        lifecycleScope.launch {
                ordersRepository.syncOrders()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getAllOrder()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.fab.setOnClickListener {
            setFab()
        }
    }

    private suspend fun getAllOrder() {
        val orders = ordersRepository.getOrders()

        Log.i(TAG, "getAllOrder - orders: ${orders.toString()}")

        if(orders.isNullOrEmpty()) {
            binding.textView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }
        else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.textView.visibility =  View.GONE

            recycleViewAdapter.update(orders)
        }
    }

    private fun setFab() {
        val intent = Intent(requireActivity(), OrdersMgmtActivity::class.java)
        startActivity(intent)
    }

    private fun configRecyclerView() {
        binding.recyclerView.adapter = recycleViewAdapter
        recycleViewAdapter.ordersOnClickEvent = { orders ->
            val intent = Intent(requireActivity(), OrdersMgmtActivity::class.java)

            intent.putExtra(ORDERS_ID, orders.orderId)
            startActivity(intent)
        }
    }


}