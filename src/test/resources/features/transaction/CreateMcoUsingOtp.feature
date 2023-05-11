@test
Feature: Create Merchant Checkout Using OTP

  Scenario: Checkout URL
    When User send POST request for "checkout" with body "checkout_url.json"
    Then The response body should be with jsonSchema "response_checkout_url.json"

  Scenario: Get the Transaction
    Given User already doing "Checkout URL"
    When User send GET request for "get the transaction" with body "get_table_transaction.json"
    Then The response body should be with jsonSchema "response_get_table_transaction.json"

  Scenario: Authenticate User
    When User send POST request for "authenticate user" with body "authenticate_user.json"
    Then The response body should be with jsonSchema "response_authenticate_user.json"

