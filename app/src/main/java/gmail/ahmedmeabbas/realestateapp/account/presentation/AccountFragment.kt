package gmail.ahmedmeabbas.realestateapp.account.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentAccountBinding

class AccountFragment: Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAccountSignIn.setOnClickListener {
            binding.apply {
                clAccountHeader.background = null
                tvAccountSignInSub.visibility = View.GONE
                btnAccountSignIn.visibility = View.GONE
                tvAccountSignInHeader.text = "Welcome"
                tvAccountDisplayName.visibility = View.VISIBLE
                tvAccountProfile.visibility = View.VISIBLE
                tvAccountSignOut.visibility = View.VISIBLE
            }
        }

        binding.tvAccountLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_dialogLanguage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}