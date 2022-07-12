package gmail.ahmedmeabbas.realestateapp.saved.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSavedSearchesBinding

class SavedSearchesFragment: Fragment() {

    private var _binding: FragmentSavedSearchesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSearchesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}