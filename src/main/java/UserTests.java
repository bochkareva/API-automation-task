import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTests {

  private RequestHelper requestHelper;

  @BeforeClass
  public void beforeSuite() {
    requestHelper = new RequestHelper();
  }

  @Test(description = "POST")
  public void testCreateUser() {
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName  = RandomStringUtils.randomAlphabetic(10);

    User user = new User(firstName, lastName);

    String response = requestHelper.createUser(user);

    user.setId(getFirstId(response));
    requestHelper.getAndCheckUser(user);
  }

  @Test(description = "PUT")
  public void testUpdateUser() {
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName  = RandomStringUtils.randomAlphabetic(10);

    User   user     = new User(firstName, lastName);
    String response = requestHelper.createUser(user);

    firstName = RandomStringUtils.randomAlphabetic(10);
    lastName = RandomStringUtils.randomAlphabetic(10);
    int firstUserId = getFirstId(response);

    user = new User(firstUserId, firstName, lastName);

    requestHelper.updateUser(user);

    requestHelper.getAndCheckUser(user);
  }

  @Test(description = "DELETE")
  public void testDeleteUser() {
    String firstName = RandomStringUtils.randomAlphabetic(10);
    String lastName  = RandomStringUtils.randomAlphabetic(10);

    User user = new User(firstName, lastName);
    requestHelper.createUser(user);

    firstName = RandomStringUtils.randomAlphabetic(10);
    lastName = RandomStringUtils.randomAlphabetic(10);
    user = new User(firstName, lastName);
    requestHelper.createUser(user);

    String response    = requestHelper.getAllUsers();
    int    firstUserId = getFirstId(response);

    requestHelper.deleteUser(firstUserId);

    response = requestHelper.getAllUsers();

    String id = "ID=" + firstUserId + ",";
    Assert.assertFalse(response.contains(id));
  }

  private int getFirstId(String response) {
    return Integer.parseInt(response.split("ID=")[1].split(",")[0]);
  }
}
