package starter.base.method;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import starter.base.api.ApiEndpoints;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MethodAction {
    ApiEndpoints apiEndpoints = new ApiEndpoints();

    protected RequestSpecification defaultSpec = new RequestSpecBuilder()
            .setBaseUri(apiEndpoints.BASE_URL).setUrlEncodingEnabled(false)
            .setAccept("application/json").addHeader("Content-Type", "application/json")
            .build();

    public void postWithAuthorization(File file) throws IOException {
        String content = DataUtils.readFileintoString(file);
        SerenityRest.given()
                .header("Content-type", "application/json")
                .body(content.toString().replace("{{timestamp}}", DataUtils.getTimestamp()))
                .post(apiEndpoints.checkout_url).then().extract().response();
    }

    public void getWithNoAuth(File file) {
        SerenityRest.given()
                .header("Content-type", "application/json")
                .body(file)
                .get(apiEndpoints.get_table_transaction).then().extract().response();

        String data1 = SerenityRest.lastResponse().jsonPath().getJsonObject("data.transactionToken").toString();
        String data2 = SerenityRest.lastResponse().jsonPath().getJsonObject("data.paymentType").toString();
        System.out.println("Transaction Token : "+data1);
        System.out.println("Transaction Token : "+data2);

        DataUtils.writeUsingFileWriter(data1, "transaction_token");
        DataUtils.writeUsingFileWriter(data2, "payment_type");
    }

    public void postWithAuthenticateUser(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .post(apiEndpoints.authenticate_user).then().extract().response();
    }

    public void postWithPaymentType(File file) throws IOException {
        String fileName1 = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";
        String fileName2 = System.getProperty("user.dir") + "/src/test/java/outputfile/payment_type.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString()
                        .replace("{{token}}", DataUtils.readUsingFileReader(fileName1))
                        .replace("{{date}}", DataUtils.readUsingFileReader(fileName2)))
                .post(apiEndpoints.change_payment_type).then().extract().response();
    }

    public void postWithSendChallenge(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .post(apiEndpoints.send_challenge).then().extract().response();
    }

    public void getWithOTP() throws IOException {
        String phone_number_region = DataUtils.getTestData("src/test/resources/payload/authenticate_user.json", "user_name");
        String phone_number = DataUtils.charRemoveAt(phone_number_region, 0);

        SerenityRest.given()
                .header("Content-type", "application/json")
                .get(apiEndpoints.get_otp+phone_number).then().extract().response();

        String data = SerenityRest.lastResponse().jsonPath().getJsonObject("otp").toString();
        System.out.println("OTP Number : "+data);
        DataUtils.writeUsingFileWriter(data, "otp_number");
    }

    public void postWithVerifyChallenge(File file) throws IOException {
        String fileName1 = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";
        String fileName2 = System.getProperty("user.dir") + "/src/test/java/outputfile/otp_number.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString()
                        .replace("{{token}}", DataUtils.readUsingFileReader(fileName1))
                        .replace("{{otp}}", DataUtils.readUsingFileReader(fileName2)))
                .post(apiEndpoints.verify_challenge).then().extract().response();
    }

    public void postWithTransactionStatus(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";

        Response response = SerenityRest.given().auth().preemptive()
                .basic(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientID"),
                        DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.clientSecret"))
                .contentType("application/x-www-form-urlencoded").formParam("grant_type", "client_credentials").when()
                .post(DataUtils.getTestData("src/test/resources/payload/auth_trex_tokenUrl.json", "authTrexStagToken.tokenURL"));

        String authToken = DataUtils.getDataFromJsonpath(response, "access_token");
        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .spec(defaultSpec)
                .auth().oauth2(authToken)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .post(apiEndpoints.get_transaction_status).then().extract().response();
    }

}
