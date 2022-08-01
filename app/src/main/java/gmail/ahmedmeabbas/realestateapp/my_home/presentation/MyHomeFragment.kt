package gmail.ahmedmeabbas.realestateapp.my_home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentMyHomeBinding

class MyHomeFragment: Fragment() {

    private var _binding: FragmentMyHomeBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}