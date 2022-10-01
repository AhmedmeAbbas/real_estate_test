package gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentHouseListingBinding
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation.adapters.ItemSliderAdapter
import gmail.ahmedmeabbas.realestateapp.listings.models.*
import gmail.ahmedmeabbas.realestateapp.listings.models.Currency
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class HouseListingFragment: Fragment() {

    private var _binding: FragmentHouseListingBinding? = null
    private val binding get() = _binding!!
    private val houseListingViewModel: HouseListingViewModel by activityViewModels()
    private val args: HouseListingFragmentArgs by navArgs()
    private var listing = Listing()
    private var property = Property.House()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        houseListingViewModel.getListingById(args.listingId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hidePreviewViews()
        setUpToolbar()
        observeListing()
        observeLoading()
    }

    private fun observeLoading() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseListingViewModel.uiState
                    .map { it.isLoading }
                    .distinctUntilChanged()
                    .collect {
                        Log.d(TAG, "observeLoading: loading: $it")
                        updateViews(it)
                    }
            }
        }
    }

    private fun updateViews(isLoading: Boolean) {
        val loadingVisibility = if (isLoading) View.VISIBLE else View.GONE
        binding.clLoading.visibility = loadingVisibility
    }

    private fun setUpContact() {
        setUpContactHeader()
        setUpName()
        setUpPhoneNumber()
        setUpEmail()
    }

    private fun setUpName() {
        if (listing.name == null) {
            binding.contactAdvertiser.tvName.visibility = View.GONE
            return
        }
        binding.contactAdvertiser.tvName.text = getString(R.string.single_string, listing.name)
    }

    private fun setUpEmail() {
        setUpStringValues(
            binding.contactAdvertiser.tvEmail,
            binding.contactAdvertiser.tvEmailValue,
            listing.email
        )
        binding.contactAdvertiser.tvEmailValue.setOnLongClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(getString(R.string.add_listing_email), listing.email)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun setUpPhoneNumber() {
        var phoneNumber = listing.phoneNumber.toString()
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
        if (listing.advertiser == Advertiser.OWNER) {
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
        setUpLongValues(
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

    private fun setUpHouseDetails() {
        setUpLongValues(
            binding.houseDetails.tvBedrooms,
            binding.houseDetails.tvBedroomsValue,
            property.bedrooms
        )
        setUpLongValues(
            binding.houseDetails.tvBathrooms,
            binding.houseDetails.tvBathroomsValue,
            property.bathrooms
        )
        setUpLongValues(
            binding.houseDetails.tvKitchens,
            binding.houseDetails.tvKitchensValue,
            property.kitchens
        )
        setUpLongValues(
            binding.houseDetails.tvHalls,
            binding.houseDetails.tvHallsValue,
            property.halls
        )
        setUpLongValues(
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

        setUpLongValues(
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
        setUpLongValues(
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

    private fun setUpLongValues(textView: TextView, value: TextView, detail: Long?) {
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
        value.text = getString(R.string.listing_item_area_m2, formatDouble(double))
    }

    private fun setUpAbout() {
        if (listing.additionalInfo.isNullOrEmpty()) {
            binding.houseAbout.root.visibility = View.GONE
            return
        }
        binding.houseAbout.tvAboutHeader.text =
            getString(R.string.listing_preview_about_house)
        binding.houseAbout.tvAboutBody.text = listing.additionalInfo
    }

    private fun setUpInstallments() {
        if (listing.installments == null || listing.installments == false) {
            binding.houseInstallments.root.visibility = View.GONE
            return
        }
        val downPayment = formatDouble(listing.downPayment!!)
        setUpMoneyText(binding.houseInstallments.tvDownPaymentValue, downPayment)

        val monthlyInstallment = formatDouble(listing.monthlyInstallment!!)
        setUpMoneyText(binding.houseInstallments.tvMonthlyInstallmentValue, monthlyInstallment)

        binding.houseInstallments.tvInstallmentPeriodValue.text = getString(
            R.string.listing_preview_installment_period_value,
            listing.installmentPeriod.toString()
        )
    }

    private fun setUpHeader() {
        setUpPhotos()
        setUpPrice()
        setUpAddress()
        setUpType()
        setUpAdvertiser()
        setUpDateAdded()
        setUpStatus()
    }

    private fun setUpPhotos() {
        setUpEmptyPhotos()
        binding.houseHeader.previewViewPager.adapter =
            ItemSliderAdapter(listing.photos)
        TabLayoutMediator(
            binding.houseHeader.previewTabLayout,
            binding.houseHeader.previewViewPager
        ) { _, _ -> }.attach()
    }

    private fun setUpEmptyPhotos() {
        if (listing.photos.isEmpty()) {
            binding.houseHeader.previewTabLayout.visibility = View.GONE
            binding.houseHeader.previewViewPager.visibility = View.GONE
            binding.houseHeader.ivPlaceholderPhoto.visibility = View.VISIBLE
        }
    }

    private fun setUpPrice() {
        val price = formatDouble(listing.price!!)
        setUpMoneyText(binding.houseHeader.tvPrice, price)
    }

    private fun setUpStatus() {
        binding.houseHeader.tvStatusValue.text =
            if (listing.listingStatus == ListingStatus.FOR_SALE) getString(
                R.string.listing_preview_status_for_sale
            ) else getString(R.string.listing_preview_status_sold)
    }

    private fun setUpDateAdded() {
        binding.houseHeader.tvDateAddedValue.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)
    }

    private fun setUpAdvertiser() {
        val owner = getString(R.string.listing_preview_for_sale_by_owner)
        val broker = getString(R.string.listing_preview_for_sale_by_broker)
        if (listing.advertiser == Advertiser.OWNER) {
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
        val city = listing.city
        val region = listing.region
        val block = getString(
            R.string.double_string,
            getString(R.string.listing_preview_block),
            listing.block.toString()
        )
        val propertyNumber = listing.propertyNumber.toString()
        address = if (Locale.getDefault().language == "en") {
            "$propertyNumber, $block, $region, $city"
        } else {
            "$propertyNumber، $block، $region، $city"
        }
        binding.houseHeader.tvAddress.text = address
    }

    private fun observeListing() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                houseListingViewModel.uiState
                    .map { it.listing }
                    .distinctUntilChanged()
                    .collect {
                        Log.d(TAG, "observeListing: $it")
                        it?.let {
                            listing = it
                            property = it.property as Property.House
                            setUpViews()
                        }
                    }
            }
        }
    }

    private fun setUpMoneyText(textView: TextView, input: String) {
        if (Locale.getDefault().language == "en" && listing.currency == getString(R.string.add_listing_currency_usd)) {
            textView.text =
                getString(R.string.double_string, getString(R.string.add_listing_usd_prefix), input)
        } else {
            textView.text = getString(R.string.double_string, input, " ${getCurrencySuffix()}")
        }
    }

    private fun getCurrencySuffix(): String {
        return when (listing.currency) {
            Currency.USD -> getString(R.string.add_listing_currency_usd)
            Currency.SDG -> getString(R.string.add_listing_currency_sdg)
            else -> ""
        }
    }

    private fun formatDouble(d: Double): String {
        return DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(d)
    }

    private fun setUpViews() {
        setUpHeader()
        setUpInstallments()
        setUpAbout()
        setUpConstruction()
        setUpHouseDetails()
        setUpUtilities()
        setUpContact()
    }

    private fun hidePreviewViews() {
        binding.houseHeader.tvPreviewSub.visibility = View.GONE
    }

    private fun setUpToolbar() {
        binding.toolbarHouseListing.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: triggered")
        houseListingViewModel.clearListing()
    }

    companion object {
        private const val TAG = "HouseListingFragment"
    }
}