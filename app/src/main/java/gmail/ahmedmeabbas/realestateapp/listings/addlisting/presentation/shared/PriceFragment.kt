package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPriceBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.Currency
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class PriceFragment : Fragment() {

    private var _binding: FragmentPriceBinding? = null
    private val binding get() = _binding!!
    private val priceViewModel: PriceViewModel by activityViewModels()

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
        setUpProgressLayout()
        setUpProgressColor()
        setUpInstallments()
        setUpCurrencySpinner()
        setUpContinueButton()
    }

    private fun setUpProgressColor() {
        with(binding) {
            progressLayout.ivAdvertiserInfo.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line1.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPropertyAddress.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line2.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivConstructionDetails.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line3.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivHouseDetails.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line4.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivUtilities.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line5.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPhotos.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
            progressLayout.line6.background =
                ColorDrawable(requireContext().getColorFromAttr(androidx.appcompat.R.attr.colorPrimary))
            progressLayout.ivPrice.background =
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.oval_primary_background,
                    requireActivity().theme
                )
        }
    }

    private fun setUpProgressLayout() {
        when (priceViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
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
                    if (position != 0) resetTextError(binding.tvCurrency)
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
            val currency = getCurrency()
            val priceInput = binding.etPrice.text.toString()
            val installments = getInstallments()

            if (validateForm(currency, priceInput) && validateInstallments(installments)) {
                val downPayment = binding.etDownPayment.text.toString().toDoubleOrNull()
                val monthlyInstallment =
                    binding.etMonthlyInstallment.text.toString().toDoubleOrNull()
                val installmentPeriod = binding.etInstallmentPeriod.text.toString().toLongOrNull()
                priceViewModel.addPrice(
                    currency,
                    priceInput.toDouble(),
                    installments,
                    downPayment,
                    monthlyInstallment,
                    installmentPeriod
                )
                findNavController().navigate(R.id.action_priceFragment_to_additionalInfoFragment)
            }
        }
    }

    private fun getCurrency(): String {
        return if (
            binding.spinnerCurrency.selectedItem.toString() == getString(R.string.add_listing_currency_usd)
        )
            Currency.USD else Currency.SDG
    }

    private fun validateInstallments(installments: Boolean?): Boolean {
        var isValid = true
        if (installments == null || installments == false) {
            isValid = true
        } else {
            if (!validateDownPayment()) isValid = false
            if (!validateMonthlyInstallment()) isValid = false
            if (!validateInstallmentPeriod()) isValid = false
        }
        return isValid
    }

    private fun validateDownPayment(): Boolean {
        val downPayment = binding.etDownPayment.text.toString()

        return if (downPayment.isEmpty()) {
            binding.downPaymentTIL.error = getString(R.string.required)
            setUpTILErrorListener(binding.downPaymentTIL, binding.etDownPayment)
            false
        } else {
            try {
                downPayment.toDouble()
                true
            } catch (e: Exception) {
                binding.downPaymentTIL.error = getString(R.string.error_invalid)
                setUpTILErrorListener(binding.downPaymentTIL, binding.etDownPayment)
                false
            }
        }
    }

    private fun validateMonthlyInstallment(): Boolean {
        val monthlyInstallment = binding.etMonthlyInstallment.text.toString()

        return if (monthlyInstallment.isEmpty()) {
            binding.monthlyInstallmentTIL.error = getString(R.string.required)
            setUpTILErrorListener(binding.monthlyInstallmentTIL, binding.etMonthlyInstallment)
            false
        } else {
            try {
                monthlyInstallment.toDouble()
                true
            } catch (e: Exception) {
                binding.monthlyInstallmentTIL.error = getString(R.string.error_invalid)
                setUpTILErrorListener(binding.monthlyInstallmentTIL, binding.etMonthlyInstallment)
                false
            }
        }
    }

    private fun validateInstallmentPeriod(): Boolean {
        val installmentPeriod = binding.etInstallmentPeriod.text.toString()

        return if (installmentPeriod.isEmpty()) {
            binding.installmentPeriodTIL.error = getString(R.string.required)
            setUpTILErrorListener(binding.installmentPeriodTIL, binding.etInstallmentPeriod)
            false
        } else {
            try {
                installmentPeriod.toInt()
                true
            } catch (e: Exception) {
                binding.installmentPeriodTIL.error = getString(R.string.error_invalid)
                setUpTILErrorListener(binding.installmentPeriodTIL, binding.etInstallmentPeriod)
                false
            }
        }
    }

    private fun getInstallments(): Boolean? {
        return when (binding.installments.chipGroup.checkedChipId) {
            R.id.chipYes -> true
            R.id.chipNo -> false
            else -> null
        }
    }

    private fun validateForm(currency: String, priceInput: String): Boolean {
        var isValid = true

        if (currency == getString(R.string.add_listing_select_currency)) {
            showTextError(binding.tvCurrency)
            isValid = false
        }
        if (priceInput.isEmpty()) {
            binding.priceTIL.error = getString(R.string.required)
            setUpTILErrorListener(binding.priceTIL, binding.etPrice)
            isValid = false
        } else {
            try {
                priceInput.toDouble()
            } catch (e: Exception) {
                binding.priceTIL.error = getString(R.string.error_invalid)
                setUpTILErrorListener(binding.priceTIL, binding.etPrice)
                isValid = false
            }
        }
        return isValid
    }

    private fun setUpTILErrorListener(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText
    ) {
        textInputEditText.setOnClickListener {
            textInputLayout.error = null
        }
        textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) textInputLayout.error = null
        }
    }

    private fun showTextError(textView: TextView) {
        textView.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_error,
                    requireActivity().theme
                ),
                null, null, null
            )
            setTextColor(ResourcesCompat.getColor(resources, R.color.red, requireActivity().theme))
            compoundDrawablePadding = 15
        }
    }

    private fun resetTextError(textView: TextView) {
        textView.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            setTextColor(requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary))
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

    companion object {
        private const val TAG = "PriceFragment"
    }
}