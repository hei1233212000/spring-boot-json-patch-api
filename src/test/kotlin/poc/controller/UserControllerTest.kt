package poc.controller

import io.restassured.RestAssured
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class UserControllerTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun loggingInit() {
            RestAssured.filters(RequestLoggingFilter(), ResponseLoggingFilter())
        }
    }

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port
    }

    @AfterEach
    fun afterEach() {
        RestAssured.reset()
    }

    @Test
    fun `should able to get list of users`() {
        RestAssured.`when`()
            .get("/users")
            .then()
            .statusCode(200)
            .assertThat()
            .body("size()", `is`(2))
            .body("[0].id", `is`(1))
            .body("[0].name", `is`("Peter"))
            .body("[0].age", `is`(18))
            .body("[0].address.country", `is`("USA"))
            .body("[0].address.city", `is`("LA"))
            .body("[1].id", `is`(2))
            .body("[1].name", `is`("Marry"))
            .body("[1].age", `is`(22))
            .body("[1].address.country", `is`("JP"))
            .body("[1].address.city", `is`("tokyo"))
    }
}