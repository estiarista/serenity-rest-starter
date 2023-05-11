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
        }
    }

    @When("User send GET request for {string} with body {string}")
    public void userSendGETRequestForWithBody(String action, String objPayload) {
        switch (action) {
            case "get the transaction":
                String path = "src/test/resources/payload/" + objPayload;
                File file = new File(String.format(path));
                method.getWithNoAuth(file);
                break;
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
