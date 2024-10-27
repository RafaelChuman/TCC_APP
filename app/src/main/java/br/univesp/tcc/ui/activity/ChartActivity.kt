package br.univesp.tcc.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import br.univesp.tcc.database.DataSource
import br.univesp.tcc.database.model.Total
import br.univesp.tcc.databinding.ActivityChartBinding
import br.univesp.tcc.repository.OrderAndItemsRepository
import br.univesp.tcc.webclient.OrderAndItemsWebClient
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.format.DateTimeFormatterBuilder

private const val TAG = "ChartActivity"

class ChartActivity : Fragment() {

    private val fromDateToString = DateTimeFormatterBuilder().appendPattern("dd.MM.yy").toFormatter()
    private val fromCurrencyToString = NumberFormat.getCurrencyInstance()

    private lateinit var binding: ActivityChartBinding

    private val orderAndItemsDao by lazy {
        DataSource.getDatabase(requireContext()).OrderAndItemsDAO()
    }

    private val orderAndItemsWebClient by lazy {
        OrderAndItemsWebClient()
    }

    private val ctx by lazy {
        requireContext()
    }

    private val orderAndItemsRepository by lazy {
        OrderAndItemsRepository(
            orderAndItemsDao,
            orderAndItemsWebClient,
            ctx
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityChartBinding.inflate(inflater, container, false)



        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getProfitCurrentMonth()

                getLiquidProfitCurrentMonth()

                getOrdersNumberMonth()

                getOrdersNotFinishedMonth()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private suspend fun getProfitCurrentMonth() {
        var profitCurrentMonth: Total = Total()

        profitCurrentMonth = orderAndItemsRepository.getProfitCurrentMonth()

        binding.textViewServiceProfitValue.text = fromCurrencyToString.format(profitCurrentMonth.total)
    }

    private suspend fun getLiquidProfitCurrentMonth() {
        var liquidProfitCurrentMonth: Total = Total()

        liquidProfitCurrentMonth = orderAndItemsRepository.getLiquidProfitCurrentMonth()

        binding.textViewWorkProfitValue.text = fromCurrencyToString.format(liquidProfitCurrentMonth.total)
    }

    private suspend fun getOrdersNumberMonth() {
        var ordersNumberMonth: Total = Total()

        ordersNumberMonth = orderAndItemsRepository.getOrdersNumberMonth()

        binding.textViewServiceMonthValue.text = ordersNumberMonth.total.toInt().toString()
    }


    private suspend fun getOrdersNotFinishedMonth() {
        var ordersNotFinishedMonth: Total = Total()

        ordersNotFinishedMonth = orderAndItemsRepository.getOrdersNotFinishedMonth()

        binding.textViewServiceOpenValue.text = ordersNotFinishedMonth.total.toInt().toString()
    }



}