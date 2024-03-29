package gmail.ahmedmeabbas.realestateapp.listings.addlisting.presentation.shared

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val addListingRepository: AddListingRepository
) : ViewModel() {

    fun getPropertyType() = addListingRepository.getPropertyType()

    fun addPrice(
        currency: String,
        price: Double,
        installments: Boolean?,
        downPayment: Double?,
        monthlyInstallment: Double?,
        installmentPeriod: Long?
    ) {
        addListingRepository.addPrice(
            currency,
            price,
            installments,
            downPayment,
            monthlyInstallment,
            installmentPeriod
        )
    }
}