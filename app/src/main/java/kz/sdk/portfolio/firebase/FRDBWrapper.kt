package kz.sdk.portfolio.firebase



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class FRDBWrapper<T> {
    private val db = FirebaseDatabase.getInstance()

    protected abstract fun getTableName(): String
    protected abstract fun getClassType(): Class<T>

    private val _getDataLiveData = MutableLiveData<T?>()
    val getDataLiveData: LiveData<T?> = _getDataLiveData

    private val _updateLiveData = MutableLiveData<T?>()
    val updateLiveData: LiveData<T?> = _updateLiveData

    init {
        db.getReference(getTableName()).addValueEventListener(updateListener())
    }

    private fun updateListener() = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            _updateLiveData.postValue(snapshot.getValue(getClassType()))
        }

        override fun onCancelled(error: DatabaseError) {
            error.let {
                Log.e("FRDBWrapper", it.message)
            }
        }
    }

    fun saveData(value: T, successSave: ((Boolean) -> Unit)? = null) {
        db.getReference(getTableName()).setValue(value) { error, _ ->
            successSave?.invoke(error == null)
            error?.let {
                Log.e("FRDBWrapper", it.message)
            }
        }
    }
    fun saveName(value: String) {
        db.getReference(getTableName()).child("name").setValue(value)
    }
    fun saveProfilePic(value: String) {
        db.getReference(getTableName()).child("pictureUrl").setValue(value)
    }
//    fun saveEventToList(value: Event) {
//        val Id = db.getReference(getTableName()).push().key
//        if (Id != null) {
//            db.getReference(getTableName()).child("favorites").child(Id).setValue(value)
//        }
//    }
    fun deleteProductFromList(value:String){
        val ref = db.getReference(getTableName()).child("favorites").child(value)
        ref.removeValue()
    }

    fun saveBonus(value: Float){
        db.getReference(getTableName()).child("bonus").setValue(value)
    }
    fun clearCart(){
        db.getReference(getTableName()).child("cart").removeValue()
    }

    fun getData() {
        db.getReference(getTableName()).get().addOnSuccessListener {
            _getDataLiveData.postValue(it.getValue(getClassType()))
        }
    }

    fun removeData() {
        db.getReference(getTableName()).removeValue()
    }
}