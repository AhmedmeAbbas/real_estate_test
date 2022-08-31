package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentUtilitiesApartmentBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class UtilitiesApartmentFragment : Fragment() {

    private var _binding: FragmentUtilitiesApartmentBinding? = null
    private val binding get() = _binding!!
    private val utilitiesApartmentViewModel: UtilitiesApartmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUtilitiesApartmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
        setUpViews()
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
        }
    }

    private fun setUpProgressLayout() {
        when (utilitiesApartmentViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun setUpViews() {
        binding.electricity.tvHeader.text = getString(R.string.add_apartment_electricity)
        binding.water.tvHeader.text = getString(R.string.add_apartment_water)
        binding.elevator.tvHeader.text = getString(R.string.add_apartment_elevator)
        binding.parking.tvHeader.text = getString(R.string.add_apartment_parking)
        binding.backupGenerator.tvHeader.text = getString(R.string.add_apartment_backup_generator)
        binding.security.tvHeader.text = getString(R.string.add_apartment_security)
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            findNavController().navigate(R.id.action_utilitiesApartmentFragment_to_addPhotosFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarUtilities.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}