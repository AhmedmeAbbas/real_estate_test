package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.PhotoUtils.getImageByteArray
import gmail.ahmedmeabbas.realestateapp.listings.models.*

class AddListingRepositoryImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : AddListingRepository {

    override val newListing = Listing()
    private var chosenPropertyType: String? = null
    private var chosenPhotoUris = listOf<Uri?>()
    private val uploadedPhotoUrls = mutableListOf<String>()


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
        block: Int,
        propertyNumber: Int
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
        chosenPhotoUris = photoUris
    }

    override fun getPreviewPhotos(): List<Uri> {
        return chosenPhotoUris.filterNotNull()
    }

    override fun addPrice(
        currency: String,
        price: Double,
        installments: Boolean?,
        downPayment: Double?,
        monthlyInstallment: Double?,
        installmentPeriod: Int?
    ) {
        val newPrice = PriceModel(price = price)
        newListing.apply {
            this.currency = currency
            this.price = newPrice
            this.installments = installments
            this.downPayment = downPayment
            this.monthlyInstallment = monthlyInstallment
            this.installmentPeriod = installmentPeriod
            this.priceHistory[0] = newPrice
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
        Log.d(TAG, "logResults: property price: ${newListing.price.price}")
        Log.d(TAG, "logResults: property installments: ${newListing.installments}")
        Log.d(TAG, "logResults: property down payment: ${newListing.downPayment}")
        Log.d(TAG, "logResults: property monthly installment: ${newListing.monthlyInstallment}")
        Log.d(TAG, "logResults: property period: ${newListing.installmentPeriod}")
        Log.d(TAG, "logResults: property lng: ${newListing.longitude}")
        Log.d(TAG, "logResults: property lat: ${newListing.latitude}")
        Log.d(TAG, "logResults: property additional info: ${newListing.additionalInfo}")
        Log.d(TAG, "logResults: property price history: ${newListing.priceHistory[0]?.dateAdded}")
        Log.d(TAG, "logResults: property status: ${newListing.listingStatus}")
    }

    override suspend fun getPhotoUrls(contentResolver: ContentResolver, height: Int) {
        for ((index, photoUri) in chosenPhotoUris.filterNotNull().withIndex()) {
            val imageByteArray = getImageByteArray(contentResolver, photoUri, height)
            val filePath = "images/listing_id/${System.currentTimeMillis()}-$index.jpg"

            val photoRef = storage.reference.child(filePath)
            Log.d(TAG, "getPhotoUrls: ${photoRef.path}")
            photoRef.putBytes(imageByteArray)
                .continueWithTask { photoUploadTask ->
                    Log.d(TAG, "Uploaded bytes: ${photoUploadTask.result?.bytesTransferred}")
                    photoRef.downloadUrl
                }.addOnCompleteListener { downloadUrlTask ->
                    if (!downloadUrlTask.isSuccessful) {
                        Log.d(TAG, "Exception with firebase storage", downloadUrlTask.exception)
                        return@addOnCompleteListener
                    }
                    val downloadUrl = downloadUrlTask.result.toString()
                    uploadedPhotoUrls.add(downloadUrl)

                    Log.d(
                        TAG,
                        "Finished uploading $photoUri, num uploads ${uploadedPhotoUrls.size}"
                    )

                    if (uploadedPhotoUrls.size == chosenPhotoUris.size) {
                        for (i in uploadedPhotoUrls.indices) {
                            Log.d(TAG, "getPhotoUrls: url$i: ${uploadedPhotoUrls[i]}")
                        }
                        addPhotosToListing(uploadedPhotoUrls)
                    }
                }
        }
        Log.d(TAG, "getPhotoUrls: size after: ${uploadedPhotoUrls.size}")
    }

    private fun addPhotosToListing(photoUrls: MutableList<String>) {
        newListing.photoUrls = photoUrls
    }

    companion object {
        private const val TAG = "AddListingRepositoryImpl"
    }
}
