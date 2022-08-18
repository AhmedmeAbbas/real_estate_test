package gmail.ahmedmeabbas.realestateapp.myhome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentMyHomeBinding

class MyHomeFragment: Fragment() {

    private var _binding: FragmentMyHomeBinding? = null
    private val binding get() = _binding!!
    private val myHomeViewModel: MyHomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMyHomeSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_global_authGraph)
        }

        binding.tvAdd.setOnClickListener {
            findNavController().navigate(R.id.action_myHomeFragment_to_addListingFragment)
        }
        observeAuthState()
    }

    private fun observeAuthState() {
        myHomeViewModel.isUserSignedIn.observe(viewLifecycleOwner) {
            binding.btnMyHomeSignIn.isEnabled = !it
        }
    }

    private fun generateListingId(): String {
        val chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var id = ""
        for (i in 0 until 28) {
            id += chars[(chars.indices).random()]
        }
        return id
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val TAG = "MyHomeFragment"
    }
}