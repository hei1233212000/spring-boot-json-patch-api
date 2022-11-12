package poc.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import poc.model.Address
import poc.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("users")
class UserController {
    private val userRepository: MutableMap<Int, User> = mutableMapOf()

    init {
        userRepository[1] = User(id = 1, name = "Peter", age = 18, address = Address(country = "USA", city = "LA"))
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
}