package starter.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import starter.base.method.MethodAction;

import java.io.File;
import java.io.IOException;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

public class CheckoutStepdefs {

    MethodAction method = new MethodAction();
    String pathPayload = "src/test/resources/payload/";
    File file;

    @When("User send POST request for {string} with body {string}")
    public void userSendPOSTRequestForWithBody(String action, String objPayload) throws IOException {
        pathPayload += objPayload;
        file = new File(String.format(pathPayload));
        switch (action) {
            case "checkout":
                method.postWithAuthorization(file);
                break;
            case "authenticate user":
                method.postWithAuthenticateUser(file);
                break;
            case "change payment":
                method.postWithPaymentType(file);
                break;
            case "send challenge":
                method.postWithSendChallenge(file);
                break;
            case "verify challenge":
                method.postWithVerifyChallenge(file);
                break;
            case "get transaction status":
                method.postWithTransactionStatus(file);
                break;
            default:
                System.out.println("Not found parameter");
                break;
        }
    }

    @When("User send GET request for {string} with body {string}")
    public void userSendGETRequestForWithBody(String action, String objPayload) throws IOException {
        pathPayload += objPayload;
        file = new File(String.format(pathPayload));
        switch (action) {
            case "get the transaction":
                method.getWithNoAuth(file);
                break;
            default:
                System.out.println("Not found parameter");
                break;
        }
    }

    @When("User send GET request for {string}")
    public void userSendGETRequestFor(String action) throws IOException {
        switch (action) {
            case "get otp":
                method.getWithOTP();
        }
    }

    @Given("User already doing {string}")
    public void userAlreadyDoing(String arg0) {

    }


    @Then("The response body should be with jsonSchema {string}")
    public void theResponseBodyShouldBeWithJsonSchema(String objSchema) {
        String path = "schema/" + objSchema;
        restAssuredThat(validatableResponse -> validatableResponse.assertThat().log().all().statusCode(200).body(matchesJsonSchemaInClasspath(path)));
    }
}
