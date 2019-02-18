import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.filter.log.ResponseLoggingFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

public class RequestHelper {

  public static final String               USER_ID = "/rs/users/{id}";
  public static       RequestSpecification requestSpec;

  public RequestHelper() {
    requestSpec = new RequestSpecBuilder()
        .setContentType(ContentType.TEXT)
        .setBaseUri("http://localhost")
        .setPort(28080)
        .addFilter(new ResponseLoggingFilter())
        .addFilter(new RequestLoggingFilter())
        .build();
  }

  public void updateUser(User user) {
    getRequestSpecification(user)
        .spec(requestSpec)
        .put(USER_ID, user.getId())
        .then()
        .statusCode(200);
  }

  public String createUser(User user) {
    return getRequestSpecification(user)
        .spec(requestSpec)
        .when()
        .post("/rs/users")
        .then()
        .spec(getResponseSpecification(user))
        .extract().asString();
  }

  public void getAndCheckUser(User user) {
    given()
        .spec(requestSpec)
        .get(USER_ID, user.getId())
        .then()
        .spec(getResponseSpecification(user));
  }

  public void deleteUser(int userId) {
    given()
        .spec(requestSpec)
        .delete(USER_ID, userId)
        .then()
        .statusCode(200);
  }

  public String getAllUsers() {
    return given()
        .spec(requestSpec)
        .when()
        .get("/rs/users")
        .then()
        .spec(getResponseSpecification())
        .extract().asString();
  }

  private RequestSpecification getRequestSpecification(User user) {
    return RestAssured
        .given()
        .header("firstName", user.getFirstName())
        .header("lastName", user.getLastName());
  }

  private ResponseSpecification getResponseSpecification(User user) {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectBody(containsString("ID="))
        .expectBody(containsString("FIRSTNAME=" + user.getFirstName()))
        .expectBody(containsString("LASTNAME=" + user.getLastName()))
        .build();
  }

  private ResponseSpecification getResponseSpecification() {
    return new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectBody(containsString("ID="))
        .expectBody(containsString("FIRSTNAME="))
        .expectBody(containsString("LASTNAME="))
        .build();
  }
}
