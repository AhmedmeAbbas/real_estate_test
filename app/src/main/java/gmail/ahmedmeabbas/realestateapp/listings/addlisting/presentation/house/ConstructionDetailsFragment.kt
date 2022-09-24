package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentConstructionDetailsBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.Finishing
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.listings.models.StructureType
import gmail.ahmedmeabbas.realestateapp.util.ColorUtils.getColorFromAttr

class ConstructionDetailsFragment : Fragment() {

    private var _binding: FragmentConstructionDetailsBinding? = null
    private val binding get() = _binding!!
    private val constructionDetailsViewModel: ConstructionDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConstructionDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpProgressLayout()
        setUpProgressColor()
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
        }
    }

    private fun setUpProgressLayout() {
        when (constructionDetailsViewModel.getPropertyType()) {
            PropertyType.APARTMENT -> {
                binding.progressLayout.apply {
                    ivConstructionDetails.visibility = View.GONE
                    line3.visibility = View.GONE
                }
            }
            else -> {}
        }
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            val lotArea = binding.etLotArea.text.toString().toDoubleOrNull()
            val builtArea = binding.etBuiltArea.text.toString().toDoubleOrNull()
            val yearBuilt = binding.etYearBuilt.text.toString().toLongOrNull()
            val structureType = getUserChoice(binding.cgStructureType)
            val finishing = getUserChoice(binding.cgFinishing)
            val moreInfo = binding.etConstructionDetailsMore.text.toString().ifEmpty { null }
            constructionDetailsViewModel.addConstructionDetails(
                lotArea,
                builtArea,
                yearBuilt,
                structureType,
                finishing,
                moreInfo
            )
            findNavController().navigate(R.id.action_constructionDetailsFragment_to_houseDetailsFragment)
        }
    }

    private fun getUserChoice(chipGroup: ChipGroup): String? {
        return when (chipGroup.checkedChipId) {
            R.id.chipRCFrame -> StructureType.RC_FRAME
            R.id.chipLoadbearing -> StructureType.LOADBEARING
            R.id.chipFinished -> Finishing.FINISHED
            R.id.chipUnfinished -> Finishing.UNFINISHED
            R.id.chipPartiallyFinished -> Finishing.PARTIALLY_FINISHED
            else -> null
        }
    }

    private fun setUpToolbar() {
        binding.toolbarConstructionDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}