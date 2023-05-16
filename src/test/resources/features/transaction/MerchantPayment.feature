@merchant_payment
Feature: Disbursed the Payment using Merchant Payment

  Scenario: Calculate the Merchant
    When User send POST request for "calculate" with body "calculate_merchant.json"

  Scenario: Get Outstanding Payment
    When User send GET request for "get the outstanding payment" with body "get_outstanding_payment.json"

  Scenario: Put The Payment
    When User send PUT request for "download mcm" with body "put_download_mcm.json"

  Scenario: Get Waiting Payment Confirmation
    When User send GET request for "get the waiting payment" with body "get_waiting_payment.json"

  Scenario: Put to Indicate The Payment as successful
    When User send PUT request for "indicate payment as successful" with body "put_indicate_payment.json"

  Scenario: Get the Payment History
    When User send GET request for "get payment history" with body "get_payment_history.json"