package kz.sdk.portfolio.fragments

import android.net.Uri
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.adapters.FilterAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentAddDocumentBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Document
import kz.sdk.portfolio.models.Filter
import javax.inject.Inject


@AndroidEntryPoint
class AddDocumentFragment:BaseFragment<FragmentAddDocumentBinding>(FragmentAddDocumentBinding::inflate) {
    override var showBottomNavigation = false
    private lateinit var filterAdapter: FilterAdapter
    private var selectedFilterTitle: String? = null


    private var imageUri: Uri? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao


    @Inject
    lateinit var storageReference: StorageReference

    private val imageResultLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.img.setImageURI(it)
            imageUri = it
            binding.textImg.isVisible = false
        }
    }

    private fun setupRecyclerView() {
        filterAdapter = FilterAdapter().apply {
            itemClick = { filter ->
                selectedFilterTitle = filter.title
                Toast.makeText(context, "Выбрано: ${filter.title}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = filterAdapter
        }

        val filters = listOf(
            Filter(1, "Диссертация"),
            Filter(2, "Статья"),
            Filter(3, "Монография"),
        )
        filterAdapter.submitList(filters)
    }





    override fun onBindView() {
        super.onBindView()
        setupRecyclerView()

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.uploadImg.setOnClickListener {
            selectEventImage()
        }
        binding.createBtn.setOnClickListener {
            if (binding.nameInput.text.isNullOrEmpty() || binding.descriptionInput.text.isNullOrEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage { imageUrl ->

                    val document = Document(
                        img = imageUrl,
                        description = binding.nameInput.text.toString(),
                        title = binding.nameInput.text.toString(),
                        category = selectedFilterTitle,
                    )
                    userDao.saveDocumentToList(document)
                    showCustomDialog("Успех", "Вы добавили научную работу")
                }
            }
        }

    }

    fun selectEventImage() {
        imageResultLauncher.launch("image/*")
    }
    private fun uploadImage(callback: (String) -> Unit) {
        imageUri?.let { uri ->
            binding.img.setImageURI(uri)
            val ref = storageReference.child(uri.lastPathSegment ?: "temp")
            ref.putFile(uri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

}