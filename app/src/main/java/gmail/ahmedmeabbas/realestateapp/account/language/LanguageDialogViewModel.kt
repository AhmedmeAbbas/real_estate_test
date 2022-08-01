package gmail.ahmedmeabbas.realestateapp.account.language

import androidx.lifecycle.ViewModel
import gmail.ahmedmeabbas.realestateapp.userpreferences.UserPreferencesRepository
import java.util.*
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LanguageDialogUiState(
    val languageCode: String = Locale.getDefault().language
)

@HiltViewModel
class LanguageDialogViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(LanguageDialogUiState())
    val uiState: StateFlow<LanguageDialogUiState> = _uiState.asStateFlow()

    fun changeAppLanguage(languageCode: String) {
        viewModelScope.launch {
            userPreferencesRepository.writeAppLanguage(languageCode)
            _uiState.update {
                it.copy(
                    languageCode = userPreferencesRepository.fetchAppLanguage() ?: return@launch
                )
            }
        }
    }
}