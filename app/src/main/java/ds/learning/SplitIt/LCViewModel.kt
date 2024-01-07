package ds.learning.SplitIt

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import ds.learning.SplitIt.data.CHATS
import ds.learning.SplitIt.data.ChatData
import ds.learning.SplitIt.data.ChatUser
import ds.learning.SplitIt.data.Event
import ds.learning.SplitIt.data.Expense
import ds.learning.SplitIt.data.ExpenseGroup
import ds.learning.SplitIt.data.GROUP
import ds.learning.SplitIt.data.MESSAGE
import ds.learning.SplitIt.data.Message
import ds.learning.SplitIt.data.SplitUser
import ds.learning.SplitIt.data.USER_NODE
import ds.learning.SplitIt.data.UserData
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var database: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProgress = mutableStateOf(false)
    val chatting = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    var userData = mutableStateOf<UserData?>(null)

    val chatMessages = mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessage = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }


    fun populateMessages(chatId: String) {
        inProgressChatMessage.value = true
        currentChatMessageListener =
            database.collection(CHATS).document(chatId).collection(MESSAGE).addSnapshotListener{
                    value, error ->
                if(error!=null){
                    handleException(error)
                }
                if(value!=null){
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy { it.timestamp }
                    inProgressChatMessage.value = false
                }
            }
    }

    fun depopulateMessages(){
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun singUp(name: String, number: String, email: String, password: String) {

        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            return
        }

        inProgress.value = true
        database.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "signup: User Logged in")
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                    } else {
                        handleException(it.exception, customMessage = "Sign Up failed")
                        inProgress.value = false
                    }
                }
            } else {
                handleException(customMessage = "Number Already Exists")
                inProgress.value = false
            }
        }
    }

    fun onDeleteChat(chatId: String){
        database.collection(CHATS).document(chatId).delete()
    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            return
        }
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAG", "signup: User Logged in")
                signIn.value = true
                inProgress.value = false
                auth.currentUser?.uid?.let {
                    getUserData(it)
                }
            } else {
                handleException(it.exception, customMessage = "Login Failed")
            }
        }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {

        var uid = auth.currentUser?.uid
        val userData = UserData(
            uid,
            name ?: userData.value?.name,
            number ?: userData.value?.number,
            imageUrl ?: userData.value?.imageUrl
        )

        uid?.let {
            inProgress.value = true
            database.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    val existingData = it.toObject<UserData>()
                    existingData?.let {
                        // Update fields with new values if provided
                        it.name = name ?: it.name
                        it.number = number ?: it.number
                        it.imageUrl = imageUrl ?: it.imageUrl
                        database.collection(USER_NODE).document(uid).set(it)
                        Log.d("TAG", "signup: Profile UPDATED")
                        inProgress.value = false
                    }
                } else {
                    database.collection(USER_NODE).document(uid).set(userData)
                    Log.d("TAG", "signup: Profile created")
                    inProgress.value = false
                    getUserData(uid)
                }
            }
                .addOnFailureListener {
                    handleException(it, "Cannot Retrieve User")
                }
        }

    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        database.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->

            if (error != null) {
                handleException(error, "Can not retreive User")
            }

            if (value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                populateChats()
                populateGroups()
            }
            inProgress.value = false
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("LiveChatApp", "live chat exception", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage

        eventMutableState.value = Event(message)
        inProgress.value = false
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri)
    }


    fun uploadImage(uri: Uri) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            var result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                createOrUpdateProfile(imageUrl = it.toString())
            }
            inProgress.value = false

        }
            .addOnFailureListener {
                handleException(it)
            }
    }

    fun logout() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        depopulateMessages()
        eventMutableState.value = Event("Logged out")
    }

    //SingleChatScreen
    fun onSend(chatId: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val text = Message(userData.value?.userId, message, time)
        database.collection(CHATS).document(chatId).collection(MESSAGE).document().set(text)
    }

    fun onAddChat(number: String) {

        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number must contain digits only")
        } else {
            database.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user2.number", userData.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user2.number", number),
                        Filter.equalTo("user1.number", userData.value?.number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    database.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {

                            if (it.isEmpty) {
                                handleException(customMessage = "number not found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = database.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name, userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.imageUrl,
                                        chatPartner.number
                                    )
                                )
                                database.collection(CHATS).document(id).set(chat)
                            }

                        }
                        .addOnFailureListener {
                            handleException(it)
                        }

                } else {
                    handleException(customMessage = "Chat already exists")
                }
            }
        }

    }

    fun populateChats() {
        database.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error == null) {
                if (value != null) {
                    chats.value = value.documents.mapNotNull {
                        it.toObject<ChatData>()
                    }
                }
                else{
                    handleException(customMessage = "Didn't find")
                }
            } else {
                handleException(error)
            }
        }
    }

    val chats =  mutableStateOf<List<ChatData>>(listOf())
    val groups = mutableStateOf<List<ExpenseGroup>>(listOf())

    fun populateGroups(){
        database.collection(GROUP).where(
            Filter.arrayContains("members", SplitUser(userData.value?.userId,
                userData.value?.name, userData.value?.imageUrl,
                userData.value?.number))
        ).addSnapshotListener { value, error ->
            if (error == null) {
                if (value != null) {
                    groups.value = value.documents.mapNotNull {
                        it.toObject<ExpenseGroup>()
                    }
                }
                else{
                    handleException(customMessage = "Didn't find")
                }
            } else {
                handleException(error)
            }
        }
    }

    fun createGroup(groupName: String){
        val groupId = database.collection(GROUP).document().id
        val group = ExpenseGroup(
            groupId = groupId,
            groupName = groupName,
            members = mutableListOf(SplitUser(userData.value?.userId,
                userData.value?.name, userData.value?.imageUrl,
                userData.value?.number)),
            memberToIndex = mutableMapOf(userData.value?.number to 0),
            transactions = mutableListOf(Expense(mutableListOf(0)))
        )
        database.collection(GROUP).document(groupId).set(group)
    }

    fun addMembertoGroup(id: String, number: String){
        database.collection(USER_NODE).whereEqualTo("number", number).get()
            .addOnSuccessListener {

                if (it.isEmpty) {
                    handleException(customMessage = "number not found")
                } else {
                    val member = it.toObjects<UserData>()[0]
                    database.collection(GROUP).document(id).get().addOnSuccessListener {

                        if (it.exists()) {
                            val existingData = it.toObject<ExpenseGroup>()!!
                            val n = existingData.members?.size!!
                            val og = existingData.transactions!!
                            existingData.let {

                                if(it.members?.find { it.number == number } == null){
                                    // Update fields with new values if provided
                                    it.members?.add(
                                        SplitUser(
                                            member.userId,
                                            member.name,
                                            member.imageUrl,
                                            member.number,
                                        )
                                    )
                                    it.memberToIndex?.put(member.number!!,n)
                                    var newTrans: MutableList<Expense> = MutableList(n+1){Expense(MutableList(n+1){0})}
                                    it.transactions = newTrans

                                    database.collection(GROUP).document(id).set(it)
                                    Log.d("GRP", "signup: Member Added")
                                }

                            }
                        } else {
                            handleException(customMessage = "Error Adding member")
                        }
                    }
                        .addOnFailureListener {
                            handleException(it, "Cannot Retrieve User")
                        }
                }

            }
            .addOnFailureListener {
                handleException(it)
            }
    }

    var trans by mutableStateOf(mutableListOf(mutableListOf(0)))

    fun populateExpense(groupid: String){
        var group = groups.value.find {
            it.groupId == groupid
        }
        val n = group?.members?.size!!
        val mulla = group?.transactions!!
        val transactionArray = MutableList(n){MutableList(n){0} }
        for(i in 0 until n){
            val broPlease = mulla[i].array!!
            for( j in 0 until n){
                transactionArray[i][j] = broPlease[j]
            }
        }
        trans = transactionArray
    }

    fun addExpense(amount:Int = 0, number: String = "",groupid:String = ""){
        var group = groups.value.find {
            it.groupId == groupid
        }
        val n = group?.members?.size!!
        val mulla = group?.transactions!!
        val transactionArray = MutableList(n){MutableList(n){0} }
        for(i in 0 until n){
            val broPlease = mulla[i].array!!
            for( j in 0 until n){
                transactionArray[i][j] = broPlease[j]
            }
        }
        val distributedAmount = amount/n
        val idx = group.memberToIndex!![number]!!
        for(i in 0..n-1){
            if(i!=idx){
                transactionArray[i][idx] += distributedAmount
            }
        }

        val minFlow = MinCashFlow()

        val result = minFlow.minCashFlow(transactionArray,n)
        trans = result
        for(i in 0 until n){
            var mullok = MutableList(n){0}
            for( j in 0 until n){
                mullok[j] = trans[i][j]
            }
            mulla[i] = Expense(mullok)
        }
        database.collection(GROUP).document(groupid).get().addOnSuccessListener {
            Log.d("GRP", "updated")
            if (it.exists()) {
                Log.d("GRP", "updated1")
                val existingData = it.toObject<ExpenseGroup>()!!
                Log.d("GRP", "updated2")
                existingData.let {
                    it.transactions = mulla
                    Log.d("GRP", "updated3")

                    database.collection(GROUP).document(groupid).set(it)
                    Log.d("GRP", "updated4")
                }
            } else {
                handleException(customMessage = "Error Adding member")
            }
        }

    }



}
