package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAdvertiserInfoBinding

class AdvertiserInfoFragment : Fragment() {

    private var _binding: FragmentAdvertiserInfoBinding? = null
    private val binding get() = _binding!!

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
        setUpContinueButton()
        setUpAdvertiserChipListener()
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
        }
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            findNavController().navigate(R.id.action_advertiserInfoFragment_to_propertyAddressFragment)
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
}