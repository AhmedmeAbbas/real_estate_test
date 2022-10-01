package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.apartment

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentApartmentPreviewBinding
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepositoryImpl
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.adapters.PreviewSliderAdapter
import gmail.ahmedmeabbas.realestateapp.listings.models.Advertiser
import gmail.ahmedmeabbas.realestateapp.listings.models.Currency
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class ApartmentPreviewFragment : Fragment() {

    private var _binding: FragmentApartmentPreviewBinding? = null
    private val binding get() = _binding!!
    private val apartmentPreviewViewModel: ApartmentPreviewViewModel by activityViewModels()
    private var apartment = Listing()
    private var property = Property.Apartment()
    private var photoUris = listOf<Uri>()
    private lateinit var dialog: Dialog

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
        setUpBackToStartButton()
        observeUserMessages()
        observeUploadPhotosProgress()
    }

    private fun observeUploadPhotosProgress() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apartmentPreviewViewModel.uiState
                    .map { it.uploadPhotosProgress }
                    .distinctUntilChanged()
                    .collect { progress ->
                        if (progress > 0)
                            updatePhotosProgressBar(progress)
                    }
            }
        }
    }

    private fun updatePhotosProgressBar(progress: Int) {
        dialog.findViewById<ProgressBar>(R.id.pbUploadProgress).progress = progress
    }

    private fun observeUserMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                apartmentPreviewViewModel.uiState
                    .map { it.userMessage }
                    .distinctUntilChanged()
                    .collect { userMessage ->
                        when (userMessage) {
                            AddListingRepositoryImpl.UPLOADING_PHOTOS -> {
                                dialog.findViewById<TextView>(R.id.tvLoadingInfo).text =
                                    getString(R.string.add_listing_uploading_photos)
                                dialog.findViewById<ProgressBar>(R.id.pbUploadProgress).visibility =
                                    View.VISIBLE
                            }
                            AddListingRepositoryImpl.SUBMITTING_LISTING -> {
                                dialog.findViewById<TextView>(R.id.tvLoadingInfo).text =
                                    getString(R.string.add_listing_submitting_listing)
                            }
                            AddListingRepositoryImpl.SUCCESS -> showSuccessDialog()
                            AddListingRepositoryImpl.NETWORK_ERROR -> showErrorDialog(getString(R.string.error_network))
                            AddListingRepositoryImpl.UNAUTHENTICATED -> showErrorDialog(getString(R.string.error_unauthenticated))
                            AddListingRepositoryImpl.FAILURE -> showErrorDialog(getString(R.string.error_occurred))
                            else -> return@collect
                        }
                        apartmentPreviewViewModel.clearMessages()
                    }
            }
        }
    }

    private fun showSuccessDialog() {
        dialog.dismiss()

        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()

        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.dialog_upload_success)
            window?.setLayout(dialogWidth, dialogHeight)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            findViewById<TextView>(R.id.btnSuccessOk).setOnClickListener {
                findNavController().navigate(R.id.action_global_myListingsFragment)
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        dialog.dismiss()

        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()

        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.dialog_upload_error)
            window?.setLayout(dialogWidth, dialogHeight)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            findViewById<TextView>(R.id.tvErrorSub).text = errorMessage
            findViewById<TextView>(R.id.btnErrorOk).setOnClickListener {
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    private fun showLoadingDialog() {
        val dialogWidth =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._250sdp).toInt()
        val dialogHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._150sdp).toInt()

        dialog = Dialog(requireContext())
        dialog.apply {
            setContentView(R.layout.dialog_submitting_listing)
            window?.setLayout(dialogWidth, dialogHeight)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            show()
        }
    }

    private fun setUpSubmitButton() {
        binding.btnSubmit.tvButton.text = getString(R.string.add_listing_submit)
        binding.btnSubmit.root.setOnClickListener {
            if (!apartmentPreviewViewModel.isConnectionAvailable()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_network),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            apartmentPreviewViewModel.submitListing(
                requireActivity().contentResolver,
                resources.getDimension(R.dimen.listing_photo_height).toInt()
            )
            showLoadingDialog()
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
        setUpLongValues(
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
        setUpLongValues(
            binding.apartmentDetails.tvBedrooms,
            binding.apartmentDetails.tvBedroomsValue,
            property.bedrooms
        )
        setUpLongValues(
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
        if (property.floorNumber == 0L) {
            setUpStringValues(
                binding.apartmentDetails.tvFloor,
                binding.apartmentDetails.tvFloorValue,
                getString(R.string.add_apartment_floor_ground)
            )
        } else {
            setUpLongValues(
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

    private fun setUpArea() {
        if (property.area == null) {
            binding.apartmentDetails.tvArea.visibility = View.GONE
            binding.apartmentDetails.tvAreaValue.visibility = View.GONE
            return
        }
        binding.apartmentDetails.tvAreaValue.text =
            getString(R.string.listing_item_area_m2, formatDouble(property.area!!))
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
        setUpPhotos()
        setUpPrice()
        setUpAddress()
        setUpType()
        setUpAdvertiser()
        setUpDateAdded()
        setUpStatus()
    }

    private fun setUpPhotos() {
        photoUris = apartmentPreviewViewModel.getPhotoUris()
        setUpEmptyPhotos()
        binding.apartmentHeader.previewViewPager.adapter =
            PreviewSliderAdapter(photoUris)
        TabLayoutMediator(
            binding.apartmentHeader.previewTabLayout,
            binding.apartmentHeader.previewViewPager
        ) { _, _ -> }.attach()
    }

    private fun setUpEmptyPhotos() {
        if (photoUris.isEmpty()) {
            binding.apartmentHeader.previewTabLayout.visibility = View.GONE
            binding.apartmentHeader.previewViewPager.visibility = View.GONE
            binding.apartmentHeader.ivPlaceholderPhoto.visibility = View.VISIBLE
        }
    }

    private fun setUpPrice() {
        val price = formatDouble(apartment.price!!)
        setUpMoneyText(binding.apartmentHeader.tvPrice, price)
    }

    private fun setUpStatus() {
        binding.apartmentHeader.tvStatusValue.text =
            getString(R.string.listing_preview_status_for_sale)
    }

    private fun setUpDateAdded() {
        binding.apartmentHeader.tvDateAddedValue.text =
            SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)
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
            textView.text = getString(R.string.double_string, input, " ${getCurrencySuffix()}")
        }
    }

    private fun getCurrencySuffix(): String {
        return when (apartment.currency) {
            Currency.USD -> getString(R.string.add_listing_currency_usd)
            Currency.SDG -> getString(R.string.add_listing_currency_sdg)
            else -> ""
        }
    }

    private fun getApartment() {
        apartment = apartmentPreviewViewModel.getApartment()
        property = apartment.property as Property.Apartment
    }

    private fun setUpBackToStartButton() {
        binding.tvBackToStart.setOnClickListener {
            findNavController().navigate(R.id.action_global_myListingsFragment)
        }
    }

    private fun setUpToolbar() {
        binding.toolbarApartmentPreview.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun formatDouble(d: Double): String {
        return DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(d)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}