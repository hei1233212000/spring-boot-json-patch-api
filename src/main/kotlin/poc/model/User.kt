package poc.model

data class User(
    var id: Int,
    var name: String,
    var nickname: String = "",
    var age: Int,
    var address: Address
)
