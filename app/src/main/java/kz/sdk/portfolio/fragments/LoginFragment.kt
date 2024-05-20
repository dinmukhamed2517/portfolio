package kz.sdk.portfolio.fragments

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentLoginBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override var showBottomNavigation: Boolean = false

    override fun onBindView() {
        super.onBindView()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.loginBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showCustomDialog("Успех!", "Вы успешно зашли в аккаунт")
                        findNavController().navigate(
                            R.id.action_loginFragment_to_homeFragment
                        )
                    } else {
                        binding.tilEmail.isErrorEnabled = true
                        binding.tilPassword.isErrorEnabled = true
                        binding.tilEmail.error = "Something is wrong"
                        binding.tilPassword.error = "Something is wrong"
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Enter something", Toast.LENGTH_SHORT).show()
            }
        }
        binding.noAccountBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigate(
                R.id.action_loginFragment_to_homeFragment
            )
        }
    }

}