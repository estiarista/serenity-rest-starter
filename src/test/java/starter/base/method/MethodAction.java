package starter.base.method;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;
import org.hamcrest.Matchers;
import starter.base.api.ApiEndpoints;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static net.serenitybdd.rest.SerenityRest.restAssuredThat;
import static org.hamcrest.Matchers.isA;

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
        System.out.println("Transaction Token : " + data1);
        System.out.println("Transaction Token : " + data2);

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
                .get(apiEndpoints.get_otp + phone_number).then().extract().response();

        String data = SerenityRest.lastResponse().jsonPath().getJsonObject("otp").toString();
        System.out.println("OTP Number : " + data);
        DataUtils.writeUsingFileWriter(data, "otp_number");
    }

    public void postWithParamtoCalculate(File file) throws IOException {

//        System.out.println("isi file =>>" + file);
        String merchant_id = DataUtils.getTestData("src/test/resources/payload/params_merchant_payment.json", "merchant_id");
        String date = DataUtils.getTestData("src/test/resources/payload/params_merchant_payment.json", "selectedDate");
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .post(apiEndpoints.calculate_merchant_payment + merchant_id + "?selectedDate=" + date).then().extract().response();
        ;
    }

    public void getWithParamtoOutstandingPayment(File file) throws IOException {


        String status = DataUtils.getTestData("src/test/resources/payload/params_merchant_payment.json", "statusWaiting");
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .get(apiEndpoints.get_status_payment + status).then().extract().response();

        String paymentCycleID = SerenityRest.lastResponse().jsonPath().getJsonObject("rows[0]['id']").toString();
        DataUtils.writeUsingFileWriter(paymentCycleID, "merchant_payment_cycle_id");

        System.out.println("cycle id => " + paymentCycleID);
    }

    public void putWithBodytoDownloadMCM(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/merchant_payment_cycle_id.txt";
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");

        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .put(apiEndpoints.put_status_payment).then().extract().response();
    }

    public void putWithBodytoIndicatePayment(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/merchant_payment_cycle_id.txt";
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");

        String content = DataUtils.readFileintoString(file);

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .body(content.toString().replace("{{token}}", DataUtils.readUsingFileReader(fileName)))
                .put(apiEndpoints.put_status_payment).then().extract().response();
    }

    public void getWithParamtoGetWaitingPayment() throws IOException {
        String status = DataUtils.getTestData("src/test/resources/payload/params_merchant_payment.json", "statusPending");
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .get(apiEndpoints.get_status_payment + status).then().extract().response();
    }

    public void getWithParamtoGetPaymentHistory() throws IOException {
        String cookie = DataUtils.getTestData("src/test/resources/payload/merchant_payment_cookie.json", "Cookie");
        String status = DataUtils.getTestData("src/test/resources/payload/get_payment_history.json", "status");
        String startDate = DataUtils.getTestData("src/test/resources/payload/get_payment_history.json", "startDate");
        String endDate = DataUtils.getTestData("src/test/resources/payload/get_payment_history.json", "endDate");
        String utc = DataUtils.getTestData("src/test/resources/payload/get_payment_history.json", "utcOffset");

        SerenityRest.given()
                .header("Content-type", "application/json")
                .header("Cookie", cookie)
                .get(apiEndpoints.get_status_payment + status + "&startDate=" + startDate + "&endDate=" + endDate + "&utcOffset=" + utc).then().extract().response();
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

    public void getWithParamtoGetTransactionStatus() throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";

        SerenityRest.given()
                .header("Content-type", "application/json")
                .queryParam("limit", "1")
                .queryParam("transactionToken", DataUtils.readUsingFileReader(fileName))
                .get(apiEndpoints.check_transaction_status).then().extract().response();
    }

    public void validateResponseBodyInTransactionStatus(String value) {
        restAssuredThat(validatableResponse ->
                validatableResponse.assertThat()
                        .body("data.status", Matchers.equalTo(Integer.valueOf(value))));

    }

}
