package gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain

import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Property

class AddApartmentUseCase(
    private val addListingRepository: AddListingRepository
) {

    private val apartment = Property.Apartment()

    fun addApartmentDetails(
        area: Double?,
        yearBuilt: Int?,
        finished: Boolean?,
        furnished: Boolean?,
        bedrooms: Int?,
        bathrooms: Int?,
        kitchen: Boolean?,
        livingRoom: Boolean?,
        balcony: Boolean?,
        floorNumber: Int?,
        moreInfo: String?
    ) {
        apartment.apply {
            this.area = area
            this.yearBuilt = yearBuilt
            this.finished = finished
            this.furnished = furnished
            this.bedrooms = bedrooms
            this.bathrooms = bathrooms
            this.kitchen = kitchen
            this.livingRoom = livingRoom
            this.balcony = balcony
            this.floorNumber = floorNumber
            this.detailsMoreInfo = moreInfo
        }
    }

    fun addApartmentUtilities(
        electricity: Boolean?,
        water: Boolean?,
        elevator: Boolean?,
        parking: Boolean?,
        backupGenerator: Boolean?,
        security: Boolean?,
        moreInfo: String?
    ) {
        apartment.apply {
            this.electricity = electricity
            this.water = water
            this.elevator = elevator
            this.parking = parking
            this.backupGenerator = backupGenerator
            this.security = security
            this.utilitiesMoreInfo = moreInfo
        }
        addListingRepository.addApartment(apartment)
    }
}