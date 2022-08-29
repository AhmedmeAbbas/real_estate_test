package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentUtilitiesApartmentBinding

class UtilitiesApartmentFragment : Fragment() {

    private var _binding: FragmentUtilitiesApartmentBinding? = null
    private val binding get() = _binding!!

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
        setUpViews()
        setUpContinueButton()
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