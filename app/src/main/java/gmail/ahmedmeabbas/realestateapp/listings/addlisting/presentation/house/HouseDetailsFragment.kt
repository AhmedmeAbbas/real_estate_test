package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentHouseDetailsBinding

class HouseDetailsFragment : Fragment() {

    private var _binding: FragmentHouseDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setupViews()
        setUpUpperApartments()
        setUpNumberOfCars()
        setUpNumberOfApartments()
        setUpContinueButton()
    }

    private fun setUpNumberOfCars() {
        binding.carGarage.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                showNumberOfCars(false)
                return@setOnCheckedStateChangeListener
            }
            when (checkedIds[0]) {
                R.id.chipYes -> showNumberOfCars(true)
                R.id.chipNo -> showNumberOfCars(false)
                else -> {  }
            }
        }
    }

    private fun showNumberOfCars(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.tvNumberCars.visibility = visibility
        binding.numberCarsTIL.visibility = visibility
    }

    private fun setUpUpperApartments() {
        binding.etFloors.addTextChangedListener {
            try {
                if (it.toString().toInt() > 1) {
                    showUpperApartments(true)
                } else {
                    showUpperApartments(false)
                }
            } catch (e: NumberFormatException) {
                showUpperApartments(false)
            }
        }
    }

    private fun showUpperApartments(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.apartments.root.visibility = visibility
    }

    private fun setUpNumberOfApartments() {
        binding.apartments.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                showNumberOfApartments(false)
                return@setOnCheckedStateChangeListener
            }
            when (checkedIds[0]) {
                R.id.chipYes -> showNumberOfApartments(true)
                R.id.chipNo -> showNumberOfApartments(false)
                else -> return@setOnCheckedStateChangeListener
            }
        }
    }

    private fun showNumberOfApartments(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        binding.tvNumberApartments.visibility = visibility
        binding.numberApartmentsTIL.visibility = visibility
    }

    private fun setupViews() {
        binding.basement.tvHeader.text = getString(R.string.add_house_basement)
        binding.carGarage.tvHeader.text = getString(R.string.add_house_garage)
        binding.apartments.tvHeader.text = getString(R.string.add_house_apartments)
    }

    private fun setUpContinueButton() {
        binding.btnContinue.tvButton.text = getString(R.string.confirm_and_continue)
        binding.btnContinue.root.setOnClickListener {
            findNavController().navigate(R.id.action_houseDetailsFragment_to_utilitiesOtherFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarHouseDetails.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}