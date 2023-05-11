package starter.base.method;

import net.serenitybdd.rest.SerenityRest;
import starter.base.api.ApiEndpoints;

import java.io.*;


public class MethodAction {
    ApiEndpoints apiEndpoints = new ApiEndpoints();

    public void postWithAuthorization(File file) {
        SerenityRest.given()
                .header("Authorization", "Bearer " + "src/.json", "authCheckout.token")
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

        String data = SerenityRest.lastResponse().jsonPath().getJsonObject("data.transactionToken").toString();
        System.out.println("Transaction Token : "+data);
        writeUsingFileWriter(data, "transaction_token");
    }

    public void postWithAuthenticateUser(File file) throws IOException {
        String fileName = System.getProperty("user.dir") + "/src/test/java/outputfile/transaction_token.txt";
        SerenityRest.given()
                .header("Authorization", "Bearer " + "src/.json", "authCheckout.token")
                .header("Cookie", "src/main/java/data/apiData.json", "authCheckout.cookies")
                .header("Content-type", "application/json")
                .body(file.toString().replace("{{token}}", readUsingFileReader(fileName)))
                .post(apiEndpoints.authenticate_user).then().extract().response();
    }

    private static void writeUsingFileWriter(String data, String filename) {
        File file = new File(System.getProperty("user.dir") + "/src/test/java/outputfile/"+filename+".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readUsingFileReader(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while((line = br.readLine()) != null){
            //process the line
            System.out.println(line);
            return line;
        }
        br.close();
        fr.close();
        return null;
    }
}
