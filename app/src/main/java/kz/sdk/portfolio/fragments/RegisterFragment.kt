package kz.sdk.portfolio.fragments


import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentRegisterBinding
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment: BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override var showBottomNavigation: Boolean = false

    override fun onBindView() {
        super.onBindView()


        binding.haveAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.signupBtn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()



            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {

                                showCustomDialog("Успех!", "Вы успешно создали аккаунт")
                                binding.tilEmail.isErrorEnabled = false
                                findNavController().navigate(
                                    R.id.action_registerFragment_to_userDetailsFragment
                                )
                            } else {
                                binding.tilEmail.isErrorEnabled = true
                                binding.tilConfirmpassword.isErrorEnabled = true
                                binding.tilPassword.isErrorEnabled = true
                                binding.tilConfirmpassword.error = it.exception?.message
                                binding.tilPassword.error = "Something is wrong"
                                binding.tilEmail.error = "Something is wrong"
                            }
                        }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Passwords are not matching",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "Enter something", Toast.LENGTH_SHORT).show()
            }
        }
    }

}