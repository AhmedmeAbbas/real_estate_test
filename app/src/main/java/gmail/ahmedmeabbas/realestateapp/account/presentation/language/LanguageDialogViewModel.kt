package gmail.ahmedmeabbas.realestateapp.account.presentation.language

import androidx.lifecycle.ViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.DataStoreKeys.KEY_APP_LANGUAGE
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import java.util.*
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LanguageDialogUiState(
    val languageCode: String = Locale.getDefault().language
)

class LanguageDialogViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LanguageDialogUiState())
    val uiState: StateFlow<LanguageDialogUiState> = _uiState.asStateFlow()

    fun changeLanguageCode(languageCode: String) {
        viewModelScope.launch {
            userPreferencesRepository.writeString(KEY_APP_LANGUAGE, languageCode)
            _uiState.update {
                it.copy(
                    languageCode = userPreferencesRepository.readString(KEY_APP_LANGUAGE)
                        ?: Locale.getDefault().language
                )
            }
        }
    }
}