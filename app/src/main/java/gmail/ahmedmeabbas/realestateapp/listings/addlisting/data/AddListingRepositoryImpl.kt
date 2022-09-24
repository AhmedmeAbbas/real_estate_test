package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import android.content.ContentResolver
import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.PhotoUtils.getImageByteArray
import gmail.ahmedmeabbas.realestateapp.listings.models.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class AddListingRepositoryImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AddListingRepository {

    override val newListing = Listing()
    private var chosenPropertyType: String? = null
    private var chosenPhotoUris = listOf<Uri>()
    private val uploadedPhotoUrls = mutableListOf<String>()
    private val _addListingMessagesFlow = MutableSharedFlow<String>()
    override val addListingMessagesFlow = _addListingMessagesFlow.asSharedFlow()
    private val _uploadPhotosProgress = MutableStateFlow(0)
    override val uploadPhotosProgress = _uploadPhotosProgress.asStateFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun setPropertyType(propertyType: String) {
        chosenPropertyType = propertyType
    }

    override fun getPropertyType(): String? {
        return chosenPropertyType
    }

    override fun addAdvertiserInfo(
        advertiserId: String,
        advertiser: String,
        phoneNumber: Long,
        name: String?,
        email: String?
    ) {
        newListing.apply {
            this.advertiserId = advertiserId
            this.advertiser = advertiser
            this.phoneNumber = phoneNumber
            this.name = name
            this.email = email
        }
    }

    override fun addPropertyAddress(
        state: String,
        city: String,
        region: String,
        block: Long,
        propertyNumber: Long
    ) {
        newListing.apply {
            this.state = state
            this.city = city
            this.region = region
            this.block = block
            this.propertyNumber = propertyNumber
        }
    }

    override fun addApartment(apartment: Property.Apartment) {
        newListing.property = apartment
    }

    override fun addHouse(house: Property.House) {
        newListing.property = house
    }

    override fun addPreviewPhotos(photoUris: List<Uri?>) {
        chosenPhotoUris = photoUris.filterNotNull()
    }

    override fun getPreviewPhotos(): List<Uri> {
        return chosenPhotoUris
    }

    override fun addPrice(
        currency: String,
        price: Double,
        installments: Boolean?,
        downPayment: Double?,
        monthlyInstallment: Double?,
        installmentPeriod: Long?
    ) {
        val dateAdded = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(Calendar.getInstance().time)

        newListing.apply {
            this.currency = currency
            this.price = price
            this.installments = installments
            this.downPayment = downPayment
            this.monthlyInstallment = monthlyInstallment
            this.installmentPeriod = installmentPeriod
            this.history = mapOf(dateAdded to price)
        }
    }

    override fun addAdditionalInfo(additionalInfo: String?) {
        newListing.additionalInfo = additionalInfo
    }

    override fun getListing(): Listing {
        return newListing
    }

    override fun logResults() {
        Log.d(TAG, "logResults: id: ${newListing.id}")
        Log.d(TAG, "logResults: date added: ${newListing.dateAdded}")
        Log.d(TAG, "logResults: advertiser id: ${newListing.advertiserId}")
        Log.d(TAG, "logResults: advertiser: ${newListing.advertiser}")
        Log.d(TAG, "logResults: phone number: ${newListing.phoneNumber}")
        Log.d(TAG, "logResults: email: ${newListing.email}")
        Log.d(TAG, "logResults: state: ${newListing.state}")
        Log.d(TAG, "logResults: city: ${newListing.city}")
        Log.d(TAG, "logResults: region: ${newListing.region}")
        Log.d(TAG, "logResults: block: ${newListing.block}")
        Log.d(TAG, "logResults: property number: ${newListing.propertyNumber}")
        Log.d(TAG, "logResults: property: ${newListing.property}")
        Log.d(TAG, "logResults: property currency: ${newListing.currency}")
        Log.d(TAG, "logResults: property price: ${newListing.price}")
        Log.d(TAG, "logResults: property installments: ${newListing.installments}")
        Log.d(TAG, "logResults: property down payment: ${newListing.downPayment}")
        Log.d(TAG, "logResults: property monthly installment: ${newListing.monthlyInstallment}")
        Log.d(TAG, "logResults: property period: ${newListing.installmentPeriod}")
        Log.d(TAG, "logResults: property lng: ${newListing.longitude}")
        Log.d(TAG, "logResults: property lat: ${newListing.latitude}")
        Log.d(TAG, "logResults: property additional info: ${newListing.additionalInfo}")
        Log.d(TAG, "logResults: property price history: ${newListing.history}")
        Log.d(TAG, "logResults: property status: ${newListing.listingStatus}")
    }

    override suspend fun submitListing(contentResolver: ContentResolver, height: Int) {
        val state = newListing.state?.lowercase()
        val city = newListing.city?.lowercase()
        val listingRef = db.collection("listings/$state/$city").document()

        if (chosenPhotoUris.isEmpty()) {
            uploadListingToFireStore(listingRef)
            return
        }

        for ((index, photoUri) in chosenPhotoUris.withIndex()) {
            val imageByteArray = getImageByteArray(contentResolver, photoUri, height)
            val filePath = "images/${listingRef.id}/${System.currentTimeMillis()}-$index.jpg"

            val photoRef = storage.reference.child(filePath)
            photoRef.putBytes(imageByteArray)
                .continueWithTask { photoRef.downloadUrl }
                .addOnCompleteListener { downloadUrlTask ->
                    if (!downloadUrlTask.isSuccessful) {
                        when (downloadUrlTask.exception) {
                            is FirebaseNetworkException -> sendMessage(NETWORK_ERROR)
                            is UserNotAuthenticatedException -> sendMessage(UNAUTHENTICATED)
                            else -> sendMessage(FAILURE)
                        }
                        clearPhotosProgress()
                        return@addOnCompleteListener
                    }

                    sendMessage(UPLOADING_PHOTOS)
                    val downloadUrl = downloadUrlTask.result.toString()
                    uploadedPhotoUrls.add(downloadUrl)
                    _uploadPhotosProgress.value =
                        uploadedPhotoUrls.size * 100 / chosenPhotoUris.size

                    if (uploadedPhotoUrls.size == chosenPhotoUris.size) {
                        addPhotosToListing(uploadedPhotoUrls)
                        uploadListingToFireStore(listingRef)
                    }
                }
        }
    }

    private fun uploadListingToFireStore(listingRef: DocumentReference) {
        sendMessage(SUBMITTING_LISTING)
        newListing.id = listingRef.id
        newListing.type = chosenPropertyType
        listingRef.set(newListing)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendMessage(SUCCESS)
                    Log.d(TAG, "uploadListingToFireStore: uploaded to firestore")
                } else {
                    when (task.exception) {
                        is FirebaseNetworkException -> sendMessage(NETWORK_ERROR)
                        is StorageException -> {
                            if ((task.exception as StorageException).errorCode == StorageException.ERROR_NOT_AUTHENTICATED)
                            sendMessage(UNAUTHENTICATED)
                        }
                        else -> sendMessage(FAILURE)
                    }
                }
                clearPhotosProgress()
            }
    }

    private fun addPhotosToListing(photoUrls: MutableList<String>) {
        newListing.photos = photoUrls
    }

    private fun clearPhotosProgress() {
        _uploadPhotosProgress.value = 0
        uploadedPhotoUrls.clear()
    }

    private fun sendMessage(message: String) {
        scope.launch {
            _addListingMessagesFlow.emit(message)
        }
    }

    companion object {
        const val UPLOADING_PHOTOS = "uploading_photos"
        const val SUBMITTING_LISTING = "submitting_listing"
        const val SUCCESS = "success"
        const val FAILURE = "failure"
        const val NETWORK_ERROR = "network_error"
        const val UNAUTHENTICATED = "unauthenticated"
        private const val TAG = "AddListingRepositoryImpl"
    }
}
