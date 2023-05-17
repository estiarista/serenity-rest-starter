@testsuite
Feature: Create Merchant Checkout Using OTP

  @hit @checkout
  Scenario: Checkout URL
    When User send POST request for "checkout" with body "checkout_url.json"
    Then The response body should be with jsonSchema "response_checkout_url.json"

  @hit @transaction_token
  Scenario: Get the Transaction
    When User send GET request for "get the transaction" with body "get_table_transaction.json"
    Then The response body should be with jsonSchema "response_get_table_transaction.json"

  @hit @authenticate
  Scenario: Authenticate User
    When User send POST request for "authenticate user" with body "authenticate_user.json"
    Then The response body should be with jsonSchema "response_authenticate_user.json"

  @hit @payment
  Scenario: Change Payment Type
    When User send POST request for "change payment" with body "change_payment_type.json"
    Then The response body should be with jsonSchema "response_change_payment_type.json"

  @hit @challenge
  Scenario: Send Challenge
    When User send POST request for "send challenge" with body "send_challenge.json"
    Then The response body should be with jsonSchema "response_send_challenge.json"

  @hit @get_otp
  Scenario: Get OTP
    When User send GET request for "get otp"
    Then The response body should be with jsonSchema "response_get_otp.json"

  @hit @challenge
  Scenario: Verify Challenge
    When User send POST request for "verify challenge" with body "verify_challenge.json"
    Then The response body should be with jsonSchema "response_verify_challenge.json"

  @hit @transaction_status
  Scenario: Get Transaction Status
    When User send POST request for "get transaction status" with body "transaction_status.json"
    Then The response body should be with jsonSchema "response_transaction_status.json"

  @hit @check_transaction_status
  Scenario: Get Transaction Status
    When User send GET request for "check transaction status" with body "check_transaction_status.json"
    Then The response body should be with jsonSchema "response_check_transaction_status.json"
    And Validate response body "transaction_status" is "4"