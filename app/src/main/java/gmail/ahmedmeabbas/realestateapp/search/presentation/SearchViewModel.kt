package gmail.ahmedmeabbas.realestateapp.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.data.GetListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val isLoading: Boolean = false,
    val listings: List<Listing?> = emptyList(),
    val userMessage: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getListingRepository: GetListingRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllListings()
    }

    private fun getAllListings() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getListingRepository.listingsFlow.collect { listings ->
                Log.d(TAG, "getAllListings: listings size: ${listings.size}")
                _uiState.update { it.copy(listings = listings) }
            }
        }
    }
    
    companion object {
        private const val TAG = "SearchViewModel"
    }
}