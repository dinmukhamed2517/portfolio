package kz.sdk.portfolio.fragments

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentProfileBinding
import kz.sdk.portfolio.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment: BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var userDao: UserDao
    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        userDao.getDataLiveData.observe(this){
            if(it?.isAdmin == false){
                binding.adminBtn.isVisible = false
            }

            binding.signOutBtn.setOnClickListener {
                signOut()
            }

            binding.name.text = it?.name
            binding.email.text = firebaseAuth.currentUser?.email
            if (it?.pictureUrl != null) {
                Glide.with(requireContext())
                    .load(it?.pictureUrl)
                    .into(binding.ava)
            } else {
                binding.ava.setImageResource(R.drawable.profile_icon)
            }
        }
    }



    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser == null){
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

    }
    private fun signOut() {
        var alertDialog: AlertDialog? = null
        alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Выход")
            .setMessage("Вы уверены что хотите выйти?")
            .setPositiveButton("Да") { _, _ ->
                firebaseAuth.signOut()
                alertDialog?.dismiss()
                findNavController().navigate(
                    R.id.action_profileFragment_to_loginFragment
                )
            }
            .setNegativeButton("Отмена") { _, _ ->
                alertDialog?.dismiss()
            }
            .show()
    }

}