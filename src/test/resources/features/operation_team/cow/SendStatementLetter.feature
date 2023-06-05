@SendStatementLetter
Feature: Sent Statement Letter

  Scenario: Submit Statement Letter
    When user hit body "submit_statement_letter.json" for send GET request "submit statement letter"

  Scenario: Create Statement Letter
    When user hit body "send_statement_letter.json" for send POST request "send statement letter"

    Scenario: Submission Statement Letter
      When user hit body "submission_statement_letter.json" for send GET request "submission statement letter"

  Scenario: Approve Statement Letter
    When user hit body "approve_statement_letter.json" for send POST request "approve statement letter"