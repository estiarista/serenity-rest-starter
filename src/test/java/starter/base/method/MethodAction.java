package starter.base.method;

import net.serenitybdd.rest.SerenityRest;
import starter.base.api.ApiEndpoints;

import java.io.File;


public class MethodAction {
    ApiEndpoints apiEndpoints = new ApiEndpoints();



    public void postWithAuthorization(File file) {
        SerenityRest.given()
                .header("Authorization", "Bearer " + "src/main/java/data/apiData.json", "authCheckout.token")
                .header("Cookie", "src/main/java/data/apiData.json", "authCheckout.cookies")
                .header("Content-type", "application/json")
                .body(file)
                .post(apiEndpoints.checkout_url).then().extract().response();
    }

    public void getWithNoAuth(File file) {
        SerenityRest.given()
                .header("Content-type", "application/json")
                .body(file)
                .get(apiEndpoints.get_table_transaction).then().extract().response();
    }
}
