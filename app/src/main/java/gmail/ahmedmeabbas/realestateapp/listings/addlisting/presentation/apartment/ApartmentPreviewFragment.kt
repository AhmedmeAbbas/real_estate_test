package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentApartmentPreviewBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.Advertiser
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import java.text.DecimalFormat
import java.util.*

class ApartmentPreviewFragment : Fragment() {

    private var _binding: FragmentApartmentPreviewBinding? = null
    private val binding get() = _binding!!
    private val apartmentPreviewViewModel: ApartmentPreviewViewModel by activityViewModels()
    private var apartment = Listing()
    private var property = Property.Apartment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentApartmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        getApartment()
        setUpHeader()
        setUpInstallments()
        setUpAbout()
        setUpApartmentDetails()
        setUpUtilities()
        setUpContact()
        setUpSubmitButton()
    }

    private fun setUpSubmitButton() {
        binding.btnSubmit.tvButton.text = getString(R.string.add_listing_submit)
        binding.btnSubmit.root.setOnClickListener {
            findNavController().navigate(R.id.action_global_myListingsFragment)
        }
    }

    private fun setUpContact() {
        setUpContactHeader()
        setUpName()
        setUpPhoneNumber()
        setUpEmail()
    }

    private fun setUpName() {
        if (apartment.name == null) {
            binding.contactAdvertiser.tvName.visibility = View.GONE
            return
        }
        binding.contactAdvertiser.tvName.text = getString(R.string.single_string, apartment.name)
    }

    private fun setUpEmail() {
        setUpStringValues(
            binding.contactAdvertiser.tvEmail,
            binding.contactAdvertiser.tvEmailValue,
            apartment.email
        )
        binding.contactAdvertiser.tvEmailValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.add_listing_email), apartment.email)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpPhoneNumber() {
        var phoneNumber = apartment.phoneNumber.toString()
        if (phoneNumber.length == 9) phoneNumber = "0$phoneNumber"
        if (phoneNumber.length == 12) phoneNumber = "+$phoneNumber"
        setUpStringValues(
            binding.contactAdvertiser.tvPhoneNumber,
            binding.contactAdvertiser.tvPhoneNumberValue,
            phoneNumber
        )
        binding.contactAdvertiser.tvPhoneNumberValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip =
                ClipData.newPlainText(getString(R.string.add_listing_phone_number), phoneNumber)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpContactHeader() {
        val owner = getString(R.string.listing_preview_contact_owner)
        val broker = getString(R.string.listing_preview_contact_broker)
        if (apartment.advertiser == Advertiser.OWNER) {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, owner)
        } else {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, broker)
        }
    }

    private fun setUpUtilities() {
        setUpBooleanValues(
            binding.apartmentUtilities.tvElectricity,
            binding.apartmentUtilities.tvElectricityValue,
            property.electricity
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvWater,
            binding.apartmentUtilities.tvWaterValue,
            property.water
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvElevator,
            binding.apartmentUtilities.tvElevatorValue,
            property.elevator
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvParking,
            binding.apartmentUtilities.tvParkingValue,
            property.parking
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvBackupGenerator,
            binding.apartmentUtilities.tvBackupGeneratorValue,
            property.backupGenerator
        )
        setUpBooleanValues(
            binding.apartmentUtilities.tvSecurity,
            binding.apartmentUtilities.tvSecurityValue,
            property.security
        )
        if (property.utilitiesMoreInfo == null) {
            binding.apartmentUtilities.tvMoreInfo.visibility = View.GONE
            binding.apartmentUtilities.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.apartmentUtilities.tvMoreInfoValue.text = property.utilitiesMoreInfo
    }

    private fun setUpApartmentDetails() {
        setUpArea()
        setUpIntValues(
            binding.apartmentDetails.tvYearBuilt,
            binding.apartmentDetails.tvYearBuiltValue,
            property.yearBuilt
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvFinishing,
            binding.apartmentDetails.tvFinishingValue,
            property.finished
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvFurniture,
            binding.apartmentDetails.tvFurnitureValue,
            property.furnished
        )
        setUpIntValues(
            binding.apartmentDetails.tvBedrooms,
            binding.apartmentDetails.tvBedroomsValue,
            property.bedrooms
        )
        setUpIntValues(
            binding.apartmentDetails.tvBathrooms,
            binding.apartmentDetails.tvBathroomsValue,
            property.bathrooms
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvKitchen,
            binding.apartmentDetails.tvKitchenValue,
            property.kitchen
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvLivingRoom,
            binding.apartmentDetails.tvLivingRoomValue,
            property.livingRoom
        )
        setUpBooleanValues(
            binding.apartmentDetails.tvBalcony,
            binding.apartmentDetails.tvBalconyValue,
            property.balcony
        )
        if (property.floorNumber == 0) {
            setUpStringValues(
                binding.apartmentDetails.tvFloor,
                binding.apartmentDetails.tvFloorValue,
                getString(R.string.add_apartment_floor_ground)
            )
        } else {
            setUpIntValues(
                binding.apartmentDetails.tvFloor,
                binding.apartmentDetails.tvFloorValue,
                property.floorNumber
            )
        }
        if (property.detailsMoreInfo == null) {
            binding.apartmentDetails.tvMoreInfo.visibility = View.GONE
            binding.apartmentDetails.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.apartmentDetails.tvMoreInfoValue.text = property.detailsMoreInfo
    }

    private fun setUpBooleanValues(textView: TextView, value: TextView, choice: Boolean?) {
        if (choice == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = if (choice) getString(R.string.yes) else getString(R.string.no)
    }

    private fun setUpIntValues(textView: TextView, value: TextView, detail: Int?) {
        if (detail == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = getString(R.string.single_string, detail.toString())
    }

    private fun setUpStringValues(textView: TextView, value: TextView, detail: String?) {
        if (detail == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = getString(R.string.single_string, detail)
    }

    private fun setUpArea() {
        if (property.area == null) {
            binding.apartmentDetails.tvArea.visibility = View.GONE
            binding.apartmentDetails.tvAreaValue.visibility = View.GONE
            return
        }
        binding.apartmentDetails.tvAreaValue.text =
            getString(R.string.listing_item_area_m2, property.area.toString())
    }

    private fun setUpAbout() {
        if (apartment.additionalInfo.isNullOrEmpty()) {
            binding.apartmentAbout.root.visibility = View.GONE
            return
        }
        binding.apartmentAbout.tvAboutHeader.text =
            getString(R.string.listing_preview_about_apartment)
        binding.apartmentAbout.tvAboutBody.text = apartment.additionalInfo
    }

    private fun setUpInstallments() {
        val hideInstallments = apartment.installments == null || apartment.installments == false
        if (hideInstallments) {
            binding.apartmentInstallments.root.visibility = View.GONE
            return
        }
        val downPayment = formatDouble(apartment.downPayment!!)
        setUpMoneyText(binding.apartmentInstallments.tvDownPaymentValue, downPayment)

        val monthlyInstallment = formatDouble(apartment.monthlyInstallment!!)
        setUpMoneyText(binding.apartmentInstallments.tvMonthlyInstallmentValue, monthlyInstallment)

        binding.apartmentInstallments.tvInstallmentPeriodValue.text = getString(
            R.string.listing_preview_installment_period_value,
            apartment.installmentPeriod.toString()
        )
    }

    private fun setUpHeader() {
        setUpPrice()
        setUpAddress()
        setUpType()
        setUpAdvertiser()
        setUpDateAdded()
        setUpStatus()
    }

    private fun setUpPrice() {
        val price = formatDouble(apartment.price.price!!)
        setUpMoneyText(binding.apartmentHeader.tvPrice, price)
    }

    private fun setUpStatus() {
        binding.apartmentHeader.tvStatusValue.text =
            getString(R.string.listing_preview_status_for_sale)
    }

    private fun setUpDateAdded() {
        binding.apartmentHeader.tvDateAddedValue.text = apartment.dateAdded.toString()
    }

    private fun setUpAdvertiser() {
        val owner = getString(R.string.listing_preview_for_sale_by_owner)
        val broker = getString(R.string.listing_preview_for_sale_by_broker)
        if (apartment.advertiser == Advertiser.OWNER) {
            binding.apartmentHeader.tvAdvertiser.text = owner
        } else {
            binding.apartmentHeader.tvAdvertiser.text = broker
        }
    }

    private fun setUpType() {
        binding.apartmentHeader.tvType.text = getString(R.string.property_type_apartment)
    }

    private fun setUpAddress() {
        val address: String
        val city = apartment.city
        val region = apartment.region
        val block = getString(
            R.string.double_string,
            getString(R.string.listing_preview_block),
            apartment.block.toString()
        )
        val propertyNumber = apartment.propertyNumber.toString()
        address = if (Locale.getDefault().language == "en") {
            "$propertyNumber, $block, $region, $city"
        } else {
            "$propertyNumber، $block، $region، $city"
        }
        binding.apartmentHeader.tvAddress.text = address
    }

    private fun setUpMoneyText(textView: TextView, input: String) {
        if (Locale.getDefault().language == "en" && apartment.currency == getString(R.string.add_listing_currency_usd)) {
            textView.text =
                getString(R.string.double_string, getString(R.string.add_listing_usd_prefix), input)
        } else {
            textView.text = getString(R.string.double_string, input, " ${apartment.currency}")
        }
    }

    private fun getApartment() {
        apartment = apartmentPreviewViewModel.getApartment()
        property = apartment.property as Property.Apartment
    }

    private fun setUpToolbar() {
        binding.toolbarApartmentPreview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun formatDouble(d: Double): String {
        return DecimalFormat("0.##").format(d)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}