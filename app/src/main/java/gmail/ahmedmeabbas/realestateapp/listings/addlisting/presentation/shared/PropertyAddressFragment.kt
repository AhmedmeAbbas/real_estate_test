package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentPropertyAddressBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class PropertyAddressFragment : Fragment() {

    private var _binding: FragmentPropertyAddressBinding? = null
    private val binding get() = _binding!!
    private val propertyAddressViewModel: PropertyAddressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
        setUpCitySpinner()
        setUpSelectRegion()
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
        }

    }

    private fun setUpProgressLayout() {
        when (propertyAddressViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun setUpCitySpinner() {
        val cities =
            listOf(getString(R.string.add_listing_city_spinner), "Khartoum", "Omdurman", "Bahri")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, cities)
        binding.spinnerCity.adapter = adapter
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

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            when (propertyAddressViewModel.getPropertyType()) {
                PropertyType.HOUSE -> navigateTo(R.id.action_propertyAddressFragment_to_constructionDetailsFragment)
                PropertyType.APARTMENT -> navigateTo(R.id.action_propertyAddressFragment_to_apartmentDetailsFragment)
                else -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_occurred),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    navigateTo(R.id.action_propertyAddressFragment_to_propertyTypeFragment)
                }
            }

        }
    }

    private fun setUpToolbar() {
        binding.toolbarPropertyAddress.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}