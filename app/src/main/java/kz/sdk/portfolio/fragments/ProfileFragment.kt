package kz.sdk.portfolio.fragments

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.internal.synchronizedImpl
import kz.sdk.portfolio.R
import kz.sdk.portfolio.adapters.EducationAdapter
import kz.sdk.portfolio.adapters.LicenseAdapter
import kz.sdk.portfolio.adapters.SkillAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentProfileBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.License
import kz.sdk.portfolio.models.Skill
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private val educations: MutableList<Education> = mutableListOf()
    private val licenses:MutableList<License> = mutableListOf()
    private val skills:MutableList<Skill> = mutableListOf()

    @Inject
    lateinit var userDao: UserDao

    override fun onBindView() {
        val educationAdapter = EducationAdapter()
        val licenseAdapter = LicenseAdapter()
        val skillAdapter = SkillAdapter()
        super.onBindView()
        userDao.getData()

        with(binding) {

            signOutBtn.setOnClickListener {
                signOut()
            }
            cardView.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
            }

            educationRecycler.adapter = educationAdapter
            educationRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            skillRecycler.adapter = skillAdapter
            skillRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addEducationBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addEducationFragment)
            }
            editEducationBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_deleteEducationFragment)
            }
            licenseRecycler.adapter = licenseAdapter
            licenseRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            addLicenseBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addLicenseFragment)
            }
            editLicenseBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_deleteLicenseFragment)
            }
            editSkillBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_deleteSkillFragment)
            }

            addSkillBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addSkillFragment)
            }
            libraryBtn.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_documentsFragment)
            }
        }

        userDao.getDataLiveData.observe(viewLifecycleOwner) { user ->
            Log.d("ProfileFragment", "LiveData triggered with user: $user")
            user?.let {
                educations.clear()
                licenses.clear()
                skills.clear()
                it.educations?.values?.let { educationList ->
                    educations.addAll(educationList)
                }
                it.licenses?.values?.let { licenseList ->
                    licenses.addAll(licenseList)
                }
                it.skills?.values?.let { skillList ->
                    skills.addAll(skillList)
                }
                skillAdapter.submitList(skills)
                licenseAdapter.submitList(licenses.toList())
                educationAdapter.submitList(educations.toList())
                Log.d("ProfileFragment", "Updated education list: $educations")

                binding.name.text = it.name
                if (it.pictureUrl != null) {
                    Glide.with(requireContext())
                        .load(it.pictureUrl)
                        .into(binding.ava)
                } else {
                    binding.ava.setImageResource(R.drawable.profile_icon)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser == null) {
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
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
            .setNegativeButton("Отмена") { _, _ ->
                alertDialog?.dismiss()
            }
            .show()
    }
}