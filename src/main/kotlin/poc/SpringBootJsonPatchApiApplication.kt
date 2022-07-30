package poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootJsonPatchApiApplication

fun main(args: Array<String>) {
    runApplication<SpringBootJsonPatchApiApplication>(*args)
}
