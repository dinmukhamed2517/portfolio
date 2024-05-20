package kz.sdk.portfolio.firebase


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object FirebaseAuthModule {

    @Provides
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    fun provideUserDao(firebaseAuth: FirebaseAuth):UserDao{
        return UserDao(firebaseAuth)
    }
    @Provides
    fun provideStorageRef(firebaseAuth: FirebaseAuth): StorageReference {
        return FirebaseStorage.getInstance()
            .getReference("Users/" + "${firebaseAuth.currentUser?.uid}")
    }

}