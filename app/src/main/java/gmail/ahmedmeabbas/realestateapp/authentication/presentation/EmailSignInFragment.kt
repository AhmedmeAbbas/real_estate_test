package gmail.ahmedmeabbas.realestateapp.authentication.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentEmailSignInBinding

class EmailSignInFragment: Fragment() {

    private var _binding: FragmentEmailSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEditTextColor()

        binding.toolbarEmailSignIn.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnEmailSignIn.tvButton.text = getString(R.string.sign_in_button)

        binding.btnEmailSignIn.root.setOnClickListener {
            binding.btnEmailSignIn.progressBar.visibility = View.VISIBLE
            binding.btnEmailSignIn.tvButton.visibility = View.GONE
            binding.btnEmailSignIn.root.isEnabled = false
        }
    }

    private fun setUpEditTextColor() {
        val colorSecondary = requireContext().getColorFromAttr(com.google.android.material.R.attr.colorSecondary)
        val hintColor = ContextCompat.getColor(requireContext(), R.color.hint_color)
        binding.etEmailSignIn.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            binding.emailSignInTIL.apply {
                val mailOutlined =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_mail, null)
                val mailFilled =
                    ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_mail_selected, null)
                val drawable = if (hasFocus) mailFilled else mailOutlined
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
            }
        }

        binding.etPasswordSignIn.setOnFocusChangeListener { _, hasFocus ->
            val color = if (hasFocus) colorSecondary else hintColor
            val lockOutlined =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock, null)
            val lockFilled =
                ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_lock_selected, null)
            val drawable = if (hasFocus) lockFilled else lockOutlined
            binding.passwordSignInTIL.apply {
                startIconDrawable = drawable
                setStartIconTintList(ColorStateList.valueOf(color))
                setEndIconTintList(ColorStateList.valueOf(color))
            }
        }
    }

    private fun Context.getColorFromAttr(
        @AttrRes attrColor: Int,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}