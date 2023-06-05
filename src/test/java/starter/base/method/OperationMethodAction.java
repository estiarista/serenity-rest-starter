package starter.base.method;

import net.serenitybdd.rest.SerenityRest;
import starter.base.api.ApiEndpoints;


import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;

public class OperationMethodAction {
    public void postSendStatementLetter(File fileSL) throws IOException{
        String accessToken = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/api_token_cow.json", "access-token");
        SerenityRest.given()
                .header("Content-Type", "application/json")
                .header("x-access-token", accessToken)
                .body(fileSL)
                .post(ApiEndpoints.post_send_statement_letter).then().extract().response();

    }
    public void getSubmissionStatementLetter(File file) throws IOException {
        String accessToken = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/api_token_cow.json", "access-token");
        SerenityRest.given()
                .header("x-access-token", accessToken)
                .get(ApiEndpoints.get_submission_statement_letter).then().extract().response();

        String letterID = SerenityRest.lastResponse().jsonPath().getJsonObject("data[0]['id']").toString();
        DataUtils.writeUsingFileWriter(letterID, "letter_id");

    }
    public void postApproveStatementLetter(File fileSL) throws IOException{
        String fileLetterID = System.getProperty("user.dir") + "/src/test/java/outputfile/letter_id.txt";
        String accessToken = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/api_token_cow.json", "access-token");
        String content = DataUtils.readFileintoString(fileSL);

        SerenityRest.given()
                .header("Content-Type", "application/json")
                .header("x-access-token", accessToken)
                .body(content.toString().replace("{{letterID}}", DataUtils.readUsingFileReader(fileLetterID)))
                .post(ApiEndpoints.post_approval_statement_letter).then().extract().response();

    }
//
    public void getSubmitStatementletter(File fileSL) throws IOException{
        String accessToken = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/api_token_cow.json", "access-token");
        String userID = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/send_statement_letter.json", "user_id");
        String transactionID = DataUtils.getTestData("src/test/resources/payload/payload_operation_team/params_statement_letter.json", "transaction_id");

        SerenityRest.given()
                .header("x-access-token", accessToken)
                .get(ApiEndpoints.get_submit_statement_letter + userID + "/trx?transactionId=" + transactionID).then().extract().response();

    }
}
