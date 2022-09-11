package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.house

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
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentHousePreviewBinding
import gmail.ahmedmeabbas.realestateapp.listings.models.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class HousePreviewFragment : Fragment() {

    private var _binding: FragmentHousePreviewBinding? = null
    private val binding get() = _binding!!
    private val housePreviewViewModel: HousePreviewViewModel by activityViewModels()
    private var house = Listing()
    private var property = Property.House()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHousePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        getHouse()
        setUpHeader()
        setUpInstallments()
        setUpAbout()
        setUpConstruction()
        setUpHouseDetails()
        setUpUtilities()
        setUpContact()
        setUpSubmitButton()
    }

    private fun setUpConstruction() {
        setUpAreaValues(
            binding.construction.tvLotArea,
            binding.construction.tvLotAreaValue,
            property.lotArea
        )
        setUpAreaValues(
            binding.construction.tvBuiltArea,
            binding.construction.tvBuiltAreaValue,
            property.builtArea
        )
        setUpIntValues(
            binding.construction.tvYearBuilt,
            binding.construction.tvYearBuiltValue,
            property.yearBuilt
        )

        setUpStructureType()
        setUpFinishing()

        if (property.constructionMoreInfo == null) {
            binding.construction.tvMoreInfo.visibility = View.GONE
            binding.construction.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.construction.tvMoreInfoValue.text = property.constructionMoreInfo
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
        if (house.name == null) {
            binding.contactAdvertiser.tvName.visibility = View.GONE
            return
        }
        binding.contactAdvertiser.tvName.text = getString(R.string.single_string, house.name)
    }

    private fun setUpEmail() {
        setUpStringValues(
            binding.contactAdvertiser.tvEmail,
            binding.contactAdvertiser.tvEmailValue,
            house.email
        )
        binding.contactAdvertiser.tvEmailValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.add_listing_email), house.email)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpPhoneNumber() {
        var phoneNumber = house.phoneNumber.toString()
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
        if (house.advertiser == Advertiser.OWNER) {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, owner)
        } else {
            binding.contactAdvertiser.tvContactHeader.text =
                getString(R.string.listing_preview_contact, broker)
        }
    }

    private fun setUpUtilities() {
        setUpBooleanValues(
            binding.houseUtilities.tvElectricity,
            binding.houseUtilities.tvElectricityValue,
            property.electricity
        )
        setUpBooleanValues(
            binding.houseUtilities.tvWater,
            binding.houseUtilities.tvWaterValue,
            property.water
        )
        if (property.utilitiesMoreInfo == null) {
            binding.houseUtilities.tvMoreInfo.visibility = View.GONE
            binding.houseUtilities.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.houseUtilities.tvMoreInfoValue.text = property.utilitiesMoreInfo
    }

    private fun setUpHouseDetails() {
        setUpIntValues(
            binding.houseDetails.tvBedrooms,
            binding.houseDetails.tvBedroomsValue,
            property.bedrooms
        )
        setUpIntValues(
            binding.houseDetails.tvBathrooms,
            binding.houseDetails.tvBathroomsValue,
            property.bathrooms
        )
        setUpIntValues(
            binding.houseDetails.tvKitchens,
            binding.houseDetails.tvKitchensValue,
            property.kitchens
        )
        setUpIntValues(
            binding.houseDetails.tvHalls,
            binding.houseDetails.tvHallsValue,
            property.halls
        )
        setUpIntValues(
            binding.houseDetails.tvFloors,
            binding.houseDetails.tvFloorsValue,
            property.floors
        )
        setUpUpperApartments()

        setUpBooleanValues(
            binding.houseDetails.tvBasement,
            binding.houseDetails.tvBasementValue,
            property.basement
        )
        setUpBooleanValues(
            binding.houseDetails.tvGarage,
            binding.houseDetails.tvGarageValue,
            property.carGarage
        )

        setUpIntValues(
            binding.houseDetails.tvCars,
            binding.houseDetails.tvCarsValue,
            property.numberCars
        )
        if (property.houseMoreInfo == null) {
            binding.houseDetails.tvMoreInfo.visibility = View.GONE
            binding.houseDetails.tvMoreInfoValue.visibility = View.GONE
            return
        }
        binding.houseDetails.tvMoreInfoValue.text = property.houseMoreInfo
    }

    private fun setUpUpperApartments() {
        if (property.apartments == null || property.apartments == false) {
            binding.houseDetails.tvUpperApartments.visibility = View.GONE
            binding.houseDetails.tvUpperApartmentsValue.visibility = View.GONE
            return
        }
        setUpIntValues(
            binding.houseDetails.tvUpperApartments,
            binding.houseDetails.tvUpperApartmentsValue,
            property.numberApartments
        )
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

    private fun setUpStructureType() {
        if (property.structureType == null) {
            binding.construction.tvStructureType.visibility = View.GONE
            binding.construction.tvStructureTypeValue.visibility = View.GONE
            return
        }
        binding.construction.tvStructureTypeValue.text =
            if (property.structureType == StructureType.RC_FRAME)
                getString(
                    R.string.single_string,
                    getString(R.string.add_house_rc_frame)
                ) else getString(R.string.add_house_loadbearing)
    }

    private fun setUpFinishing() {
        if (property.finishing == null) {
            binding.construction.tvFinishing.visibility = View.GONE
            binding.construction.tvFinishingValue.visibility = View.GONE
            return
        }
        when (property.finishing) {
            Finishing.FINISHED -> binding.construction.tvFinishingValue.text =
                getString(R.string.single_string, getString(R.string.add_house_finished))
            Finishing.UNFINISHED -> binding.construction.tvFinishingValue.text =
                getString(R.string.single_string, getString(R.string.add_house_unfinished))
            Finishing.PARTIALLY_FINISHED -> binding.construction.tvFinishingValue.text =
                getString(R.string.single_string, getString(R.string.add_house_partially_finished))
        }
    }

    private fun setUpAreaValues(textView: TextView, value: TextView, double: Double?) {
        if (double == null) {
            textView.visibility = View.GONE
            value.visibility = View.GONE
            return
        }
        value.text = getString(R.string.listing_item_area_m2, double.toString())
    }

    private fun setUpAbout() {
        if (house.additionalInfo.isNullOrEmpty()) {
            binding.houseAbout.root.visibility = View.GONE
            return
        }
        binding.houseAbout.tvAboutHeader.text =
            getString(R.string.listing_preview_about_apartment)
        binding.houseAbout.tvAboutBody.text = house.additionalInfo
    }

    private fun setUpInstallments() {
        val hideInstallments = house.installments == null || house.installments == false
        if (hideInstallments) {
            binding.houseInstallments.root.visibility = View.GONE
            return
        }
        val downPayment = formatDouble(house.downPayment!!)
        setUpMoneyText(binding.houseInstallments.tvDownPaymentValue, downPayment)

        val monthlyInstallment = formatDouble(house.monthlyInstallment!!)
        setUpMoneyText(binding.houseInstallments.tvMonthlyInstallmentValue, monthlyInstallment)

        binding.houseInstallments.tvInstallmentPeriodValue.text = getString(
            R.string.listing_preview_installment_period_value,
            house.installmentPeriod.toString()
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
        val price = formatDouble(house.price.price!!)
        setUpMoneyText(binding.houseHeader.tvPrice, price)
    }

    private fun setUpStatus() {
        binding.houseHeader.tvStatusValue.text =
            getString(R.string.listing_preview_status_for_sale)
    }

    private fun setUpDateAdded() {
        binding.houseHeader.tvDateAddedValue.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)
    }

    private fun setUpAdvertiser() {
        val owner = getString(R.string.listing_preview_for_sale_by_owner)
        val broker = getString(R.string.listing_preview_for_sale_by_broker)
        if (house.advertiser == Advertiser.OWNER) {
            binding.houseHeader.tvAdvertiser.text = owner
        } else {
            binding.houseHeader.tvAdvertiser.text = broker
        }
    }

    private fun setUpType() {
        binding.houseHeader.tvType.text = getString(R.string.property_type_house)
    }

    private fun setUpAddress() {
        val address: String
        val city = house.city
        val region = house.region
        val block = getString(
            R.string.double_string,
            getString(R.string.listing_preview_block),
            house.block.toString()
        )
        val propertyNumber = house.propertyNumber.toString()
        address = if (Locale.getDefault().language == "en") {
            "$propertyNumber, $block, $region, $city"
        } else {
            "$propertyNumber، $block، $region، $city"
        }
        binding.houseHeader.tvAddress.text = address
    }

    private fun setUpMoneyText(textView: TextView, input: String) {
        if (Locale.getDefault().language == "en" && house.currency == getString(R.string.add_listing_currency_usd)) {
            textView.text =
                getString(R.string.double_string, getString(R.string.add_listing_usd_prefix), input)
        } else {
            textView.text = getString(R.string.double_string, input, " ${house.currency}")
        }
    }

    private fun getHouse() {
        house = housePreviewViewModel.getHouse()
        property = house.property as Property.House
    }

    private fun setUpToolbar() {
        binding.toolbarPreview.setNavigationOnClickListener {
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