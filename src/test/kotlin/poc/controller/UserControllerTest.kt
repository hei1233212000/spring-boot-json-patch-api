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
import org.springframework.http.HttpHeaders

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
            .body("[0].nickname", `is`("Handsome"))
            .body("[0].age", `is`(18))
            .body("[0].address.country", `is`("USA"))
            .body("[0].address.city", `is`("LA"))
            .body("[1].id", `is`(2))
            .body("[1].name", `is`("Marry"))
            .body("[1].nickname", `is`(""))
            .body("[1].age", `is`(22))
            .body("[1].address.country", `is`("JP"))
            .body("[1].address.city", `is`("tokyo"))
    }

    @Test
    fun `should able to get user details`() {
        RestAssured.`when`()
            .get("/users/1")
            .then()
            .statusCode(200)
            .assertThat()
            .body("id", `is`(1))
            .body("name", `is`("Peter"))
            .body("nickname", `is`("Handsome"))
            .body("age", `is`(18))
            .body("address.country", `is`("USA"))
            .body("address.city", `is`("LA"))
    }

    @Test
    fun `should able to update the user by using JSON patch`() {
        val requestBody = """
            [
              { "op": "replace", "path": "/age", "value": 30 },
              { "op": "replace", "path": "/address/city", "value": "AL" },
              { "op": "remove", "path": "/nickname" }
            ]
        """.trimIndent()
        RestAssured.given()
            .header(HttpHeaders.CONTENT_TYPE, "application/json-patch+json")
            .body(requestBody)
            .patch("/users/1")
            .then()
            .statusCode(200)

        RestAssured.`when`()
            .get("/users/1")
            .then()
            .statusCode(200)
            .assertThat()
            .body("id", `is`(1))
            .body("name", `is`("Peter"))
            .body("nickname", `is`(""))
            .body("age", `is`(30))
            .body("address.country", `is`("USA"))
            .body("address.city", `is`("AL"))
    }
}