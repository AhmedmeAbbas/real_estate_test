package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAdvertiserInfoBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.Advertiser
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class AdvertiserInfoFragment : Fragment() {

    private var _binding: FragmentAdvertiserInfoBinding? = null
    private val binding get() = _binding!!
    private val advertiserInfoViewModel: AdvertiserInfoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdvertiserInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpEmailEditText()
        setUpProgressLayout()
        setUpProgressColor()
        setUpContinueButton()
        setUpAdvertiserChipListener()
    }

    private fun setUpEmailEditText() {
        advertiserInfoViewModel.getUserEmail()
        val userEmail = advertiserInfoViewModel.uiState.value.userEmail ?: ""
        binding.etEmail.setText(userEmail)
    }

    private fun setUpProgressColor() {
        binding.progressLayout.ivAdvertiserInfo.background =
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.oval_primary_background,
                requireActivity().theme
            )
    }

    private fun setUpProgressLayout() {
        when (advertiserInfoViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
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
                    binding.tvAdvertiserAcknowledgement.apply {
                        text = getString(R.string.add_listing_owner_acknowledgement)
                        visibility = View.VISIBLE
                    }
                }
                R.id.chipBroker -> {
                    binding.tvAdvertiserAcknowledgement.apply {
                        text = getString(R.string.add_listing_broker_acknowledgement)
                        visibility = View.VISIBLE
                    }
                }
                else -> binding.tvAdvertiserAcknowledgement.visibility = View.GONE
            }
            // This method is called to remove the continue button error
            resetAdvertiserError()
        }
    }

    private fun resetAdvertiserError() {
        binding.tvAdvertiser.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            setTextColor(requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary))
        }
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            val advertiserId = getAdvertiserId()
            val advertiser = getAdvertiser()
            val phoneNumberInput = binding.etPhoneNumber.text.toString()
            val email = binding.etEmail.text.toString().ifEmpty { null }

            if (!validateForm(advertiserId, advertiser, phoneNumberInput)) return@setOnClickListener
            advertiserInfoViewModel.addAdvertiserInfo(advertiserId!!, advertiser!!, phoneNumberInput.toInt(), email)
            findNavController().navigate(R.id.action_advertiserInfoFragment_to_propertyAddressFragment)
        }
    }

    private fun getAdvertiserId(): String? {
        advertiserInfoViewModel.getUserUID()
        return advertiserInfoViewModel.uiState.value.userId
    }

    private fun getAdvertiser(): String? {
        return when (binding.cgAdvertiser.checkedChipId) {
            R.id.chipOwner -> Advertiser.OWNER
            R.id.chipBroker -> Advertiser.BROKER
            else -> null
        }
    }

    private fun validateForm(
        advertiserId: String?,
        advertiser: String?,
        phoneNumber: String
    ): Boolean {
        var isValid = true

        if (advertiserId == null) {
            Snackbar.make(binding.root, getString(R.string.error_occurred), Snackbar.LENGTH_SHORT)
                .show()
            isValid = false
        }
        if (advertiser == null) {
            showAdvertiserError()
            isValid = false
        }
        if (phoneNumber.isEmpty()) {
            binding.phoneNumberTIL.error = getString(R.string.required)
            setUpPhoneNumberErrorListener()
            // Returning false here instead of isValid to skip the format check
            return false
        }
        try {
            phoneNumber.toInt()
        } catch (e: Exception) {
            binding.phoneNumberTIL.error = getString(R.string.error_invalid_number)
            setUpPhoneNumberErrorListener()
            isValid = false
        }
        return isValid
    }

    private fun setUpPhoneNumberErrorListener() {
        binding.etPhoneNumber.setOnClickListener {
            binding.phoneNumberTIL.error = null
        }
    }

    private fun showAdvertiserError() {
        binding.tvAdvertiser.apply {
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


    private fun setUpToolbar() {
        binding.toolbarAdvertiserInfo.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AdvertiserInfoFragment"
    }
}