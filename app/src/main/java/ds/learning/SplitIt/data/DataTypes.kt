package ds.learning.SplitIt.data

data class  UserData(
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?=""
){

    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}

data class ChatData(
    val chatId: String?="",
    val user1:ChatUser=ChatUser(),
    val user2:ChatUser=ChatUser()
)

data class ChatUser(
    val userId: String?="",
    val name: String?="",
    val imageUrl: String?="",
    val number: String?=""
)

data class Message(
    val sendBy: String?="",
    val messageval : String?="",
    val timestamp: String?=""
)

data class SplitUser(
    val userId: String?="",
    val name: String?="",
    val imageUrl: String?="",
    val number: String?="",
)

data class Expense(
    val array: MutableList<Int>?=null
)

data class ExpenseGroup(
    val groupId: String?="",
    val groupName: String?="",
    var members:MutableList<SplitUser>?=null,
    var membersId:MutableList<String?>?=null,
    var memberToIndex: MutableMap<String?,Int>?=null,
    var transactions: MutableList<Expense>?=null
){
    fun toMap() = mapOf(
        "groupId" to groupId,
        "groupName" to groupName,
        "members" to members,
        "memberToIndex" to memberToIndex,
        "transactions" to transactions
    )
}

data class trans(
    val description: String?="",
    val amount: Int
)