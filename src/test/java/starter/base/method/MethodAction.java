package starter.base.method;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import starter.base.api.ApiEndpoints;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static starter.base.method.DataUtils.generateStringFromResource;


public class MethodAction {
    ApiEndpoints apiEndpoints = new ApiEndpoints();

    protected RequestSpecification defaultSpec = new RequestSpecBuilder()
            .setBaseUri(apiEndpoints.BASE_URL).setUrlEncodingEnabled(false)
            .setAccept("application/json").addHeader("Content-Type", "application/json")
            .build();

    public void postWithAuthorization(File file) {
        SerenityRest.given()
                .header("Authorization", "Bearer " + "src/test/resources/payload/api_token.json", "authCheckout.token")
                .header("Cookie", "src/test/resources/payload/api_token.json", "authCheckout.cookies")
                .header("Content-type", "application/json")
                .body(file)
                .post(apiEndpoints.checkout_url).then().extract().response();
    }

    public void getWithNoAuth(File file) {
        SerenityRest.given()
                .header("Content-type", "application/json")
                .body(file)
                .get(apiEndpoints.get_table_transaction).then().extract().response();

        String data = SerenityRest.lastResponse().jsonPath().getJsonObject("data.transactionToken").toString();
        System.out.println("Transaction Token : "+data);
        DataUtils.writeUsingFileWriter(data, "transaction_token");
    }

    public void postWithAuthenticateUser(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        System.out.println(authToken);

        String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .post(apiEndpoints.authenticate_user).then().extract().response();
    }

}
