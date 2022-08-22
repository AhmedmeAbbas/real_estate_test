package gmail.ahmedmeabbas.realestateapp.listings.presentation.addlisting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAddApartmentBinding

class AddApartmentFragment : Fragment() {

    private var _binding: FragmentAddApartmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddApartmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpCitySpinner()
        setUpSelectRegion()
        setUpCurrencySpinner()
        setUpAdvertiserChipListener()
        setUpInstallmentsChipListener()
        setUpSubmitButton()
    }

    private fun setUpSubmitButton() {
        binding.btnSubmitApartment.tvButton.text = getString(R.string.add_listing_submit)
    }

    private fun setUpInstallmentsChipListener() {
        binding.cgInstallments.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                enableInstallmentViews(false)
                return@setOnCheckedStateChangeListener
            }
            if (checkedIds[0] == R.id.chipInstallmentsYes) {
                enableInstallmentViews(true)
            } else {
                enableInstallmentViews(false)
            }
        }
    }

    private fun enableInstallmentViews(enable: Boolean) {
        val alpha = if (enable) 1.0F else 0.4F
        with(binding) {
            tvDownPayment.alpha = alpha
            etDownPayment.isEnabled = enable
            etDownPayment.alpha = alpha
            tvMonthlyInstallment.alpha = alpha
            etMonthlyInstallment.isEnabled = enable
            etMonthlyInstallment.alpha = alpha
            tvInstallmentPeriod.alpha = alpha
            etInstallmentPeriod.isEnabled = enable
            etInstallmentPeriod.alpha = alpha
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
                    if (position == 0) {
                        binding.tvPrice.text = getString(R.string.add_listing_price, "")
                        binding.tvDownPayment.text =
                            getString(R.string.add_listing_down_payment, "")
                        binding.tvMonthlyInstallment.text =
                            getString(R.string.add_listing_monthly_installment, "")
                    } else {
                        binding.tvPrice.text = getString(
                            R.string.add_listing_price,
                            "\n(${adapter.getItem(position)})"
                        )
                        binding.tvDownPayment.text = getString(
                            R.string.add_listing_down_payment,
                            "\n(${adapter.getItem(position)})"
                        )
                        binding.tvMonthlyInstallment.text = getString(
                            R.string.add_listing_monthly_installment,
                            "\n(${adapter.getItem(position)})"
                        )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
    }

    private fun setUpAdvertiserChipListener() {
        binding.cgAdvertiser.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                binding.tvAdvertiserAcknowledgement.visibility = View.GONE
                return@setOnCheckedStateChangeListener
            }
            when (checkedIds[0]) {
                R.id.chipOwner -> {
                    binding.tvAdvertiserAcknowledgement.text =
                        getString(R.string.add_listing_owner_acknowledgement)
                    binding.tvAdvertiserAcknowledgement.visibility = View.VISIBLE
                }
                R.id.chipBroker -> {
                    binding.tvAdvertiserAcknowledgement.text =
                        getString(R.string.add_listing_broker_acknowledgement)
                    binding.tvAdvertiserAcknowledgement.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setUpSelectRegion() {
        val regions = listOf(
            "Almansheya",
            "Altayef",
            "Alryiadh",
            "Almamoura",
            "Alblabel",
            "Aljref",
            "Arkweet",
            "Alamarat",
            "Aldem",
            "Asahafa",
            "Burri",
            "Nasir"
        )
        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._400sdp).toInt()
        binding.tvSelectRegion.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.apply {
                setContentView(R.layout.dialog_select_region)
                window?.setLayout(dialogWidth, dialogHeight)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                show()
            }
            val etEnterRegion = dialog.findViewById<EditText>(R.id.etEnterRegion)
            val lvSelectRegion = dialog.findViewById<ListView>(R.id.lvSelectRegion)
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, regions)
            lvSelectRegion.adapter = adapter
            etEnterRegion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    adapter.filter.filter(p0)
                }

                override fun afterTextChanged(p0: Editable?) {

                }
            })
            lvSelectRegion.setOnItemClickListener { _, _, position, _ ->
                binding.tvSelectRegion.text = adapter.getItem(position)
                dialog.dismiss()
            }
        }
    }

    private fun setUpCitySpinner() {
        val cities =
            listOf(getString(R.string.add_listing_city_spinner), "Khartoum", "Omdurman", "Bahri")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        binding.spinnerCity.adapter = adapter
    }

    private fun setUpToolbar() {
        binding.toolbarAddApartment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}