package kz.sdk.portfolio.fragments

import kz.sdk.portfolio.base.BaseFragment

import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.databinding.FragmentUpdateProfileBinding
import kz.sdk.portfolio.firebase.UserDao
import javax.inject.Inject


@AndroidEntryPoint
class UpdateProfileFragment:BaseFragment<FragmentUpdateProfileBinding>(FragmentUpdateProfileBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null

    @Inject
    lateinit var userDao: UserDao

    override var showBottomNavigation: Boolean = false

    override fun onBindView() {


        super.onBindView()


        binding.ava.setOnClickListener {
            resultLauncher.launch("image/*")
        }


        binding.updateBtn.setOnClickListener {
            if (imageUri != null) {
                uploadProfilePic()
            }
        }

    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        binding.ava.setImageURI(it)
        imageUri = it
    }

    private fun uploadProfilePic() {
        imageUri?.let {
            storageReference.putFile(it).addOnSuccessListener { task ->
                task.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    val imgUrl = uri.toString()
                    userDao.saveProfilePic(imgUrl)
                    showCustomDialog("Успех", "Обновлено")

                }


            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}