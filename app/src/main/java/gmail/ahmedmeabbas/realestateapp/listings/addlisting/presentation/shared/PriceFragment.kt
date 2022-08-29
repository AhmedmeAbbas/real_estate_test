package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPriceBinding

class PriceFragment : Fragment() {

    private var _binding: FragmentPriceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpInstallments()
        setUpCurrencySpinner()
        setUpContinueButton()
    }

    private fun setUpInstallments() {
        binding.installments.tvHeader.text = getString(R.string.add_listing_installments)
        binding.installments.chipGroup.setOnCheckedStateChangeListener { group, _ ->
            if (group.checkedChipId == R.id.chipYes) {
                enableInstallmentViews(true)
            } else {
                enableInstallmentViews(false)
            }
        }
    }

    private fun enableInstallmentViews(enable: Boolean) {
        val alpha = if (enable) 1.0F else 0.6F
        with(binding) {
            tvDownPayment.alpha = alpha
            downPaymentTIL.isEnabled = enable
            downPaymentTIL.alpha = alpha
            tvMonthlyInstallment.alpha = alpha
            monthlyInstallmentTIL.isEnabled = enable
            monthlyInstallmentTIL.alpha = alpha
            tvInstallmentPeriod.alpha = alpha
            installmentPeriodTIL.isEnabled = enable
            installmentPeriodTIL.alpha = alpha
        }
    }

    private fun setUpCurrencySpinner() {
        val currencies = listOf(
            getString(R.string.add_listing_select_currency),
            getString(R.string.add_listing_currency_usd),
            getString(R.string.add_listing_currency_sdg)
        )
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, currencies)
        binding.spinnerCurrency.adapter = adapter
        binding.spinnerCurrency.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    clearAllTILs()
                    when (position) {
                        1 -> {
                            setTILCurrency("USD")
                        }
                        2 -> {
                            setTILCurrency("SDG")
                        }
                        else -> {}
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

    private fun setTILCurrency(currency: String) {
        if (currency == "USD") {
            binding.priceTIL.prefixText = getString(R.string.add_listing_usd_prefix)
            binding.downPaymentTIL.prefixText = getString(R.string.add_listing_usd_prefix)
            binding.monthlyInstallmentTIL.prefixText = getString(R.string.add_listing_usd_prefix)
        } else {
            binding.priceTIL.suffixText = getString(R.string.add_listing_sdg_suffix)
            binding.downPaymentTIL.suffixText = getString(R.string.add_listing_sdg_suffix)
            binding.monthlyInstallmentTIL.suffixText = getString(R.string.add_listing_sdg_suffix)
        }
    }

    private fun clearAllTILs() {
        binding.priceTIL.apply {
            prefixText = null
            suffixText = null
        }
        binding.downPaymentTIL.apply {
            prefixText = null
            suffixText = null
        }
        binding.monthlyInstallmentTIL.apply {
            prefixText = null
            suffixText = null
        }
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            findNavController().navigate(R.id.action_priceFragment_to_additionalInfoFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarPrice.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}