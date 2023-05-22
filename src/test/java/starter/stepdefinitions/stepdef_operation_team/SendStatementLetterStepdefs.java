package starter.stepdefinitions.stepdef_operation_team;

import io.cucumber.java.en.When;
import starter.base.method.OperationMethodAction;

import java.io.File;
import java.io.IOException;

public class SendStatementLetterStepdefs {

    OperationMethodAction oprmethod = new OperationMethodAction();
    String pathPayload = "src/test/resources/payload/payload_operation_team/";
    File file;

    @When("user hit body {string} for send POST request {string}")
    public void userHitBodyForSendPOSTRequest(String objPayload, String objAction) throws IOException {
        pathPayload += objPayload;
        file = new File(String.format(pathPayload));
        switch (objAction) {
            case "send statement letter":
                oprmethod.postSendStatementLetter(file);
                break;
            case "approve statement letter":
                oprmethod.postApproveStatementLetter(file);
                break;
            default:
                System.out.println("Not found parameter");
                break;
        }
    }

    @When("user hit body {string} for send GET request {string}")
    public void userHitBodyForSendGETRequest(String objPayload, String objAction) throws IOException {
        pathPayload += objPayload;
        file = new File(String.format(pathPayload));
        switch (objAction) {
            case "submit statement letter":
                oprmethod.getSubmitStatementletter(file);
                break;
            case "submission statement letter":
                oprmethod.getSubmissionStatementLetter(file);
                break;
            default:
                System.out.println("Not found parameter");
                break;
        }
    }
}
