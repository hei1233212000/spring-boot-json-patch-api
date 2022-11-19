package poc.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.web.bind.annotation.*
import poc.model.Address
import poc.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("users")
class UserController(
    private val objectMapper: ObjectMapper
) {
    private val userRepository: MutableMap<Int, User> = mutableMapOf()

    init {
        userRepository[1] = User(
            id = 1,
            name = "Peter",
            age = 18,
            nickname = "Handsome",
            address = Address(country = "USA", city = "LA")
        )
        userRepository[2] = User(id = 2, name = "Marry", age = 22, address = Address(country = "JP", city = "tokyo"))
    }

    @GetMapping("/{id}")
    private fun getUserById(@PathVariable id: Int): Mono<User> {
        return if (userRepository.containsKey(id)) {
            Mono.just(userRepository[id]!!)
        } else {
            Mono.empty()
        }
    }

    @GetMapping
    private fun getAllUsers() = Flux.fromIterable(userRepository.values)

    @PatchMapping("/{id}", consumes = ["application/json-patch+json"])
    private fun updateUserByJsonPatch(@PathVariable id: Int, @RequestBody patch: JsonPatch) {
        val user = userRepository[id]!!
        val patched: JsonNode = patch.apply(objectMapper.convertValue(user, JsonNode::class.java))
        val patchedUser = objectMapper.treeToValue(patched, User::class.java)
        userRepository[id] = patchedUser
    }
}