package starter.base.api;

public class ApiEndpoints {

    public static final String BASE_URL = "https://staging.kredivo.com";
    public static final String BASE_URL_V2 = BASE_URL + "/kredivo/v2";
    public static final String BASE_URL_V3 = BASE_URL + "/kredivo/v3";
    public static final String BASE_URL_TREX_ECS_STAGING = "https://trex-ecs-staging.kredivo.com";
    public static final String checkout_url = BASE_URL_V2 + "/checkout_url";
    public static final String authenticate_user = BASE_URL_V3 + "/authenticate_user";
    public static final String get_table_transaction = BASE_URL_TREX_ECS_STAGING + "/api/testing/transaction";
    public static final String get_otp = BASE_URL_TREX_ECS_STAGING + "/api/testing/otp/";
    public static final String change_payment_type = BASE_URL_V3 + "/change_payment_type";
    public static final String send_challenge = BASE_URL_V3 + "/send_challenge";
    public static final String verify_challenge = BASE_URL_V3 + "/verify_challenge";
    public static final String get_transaction_status = BASE_URL_V3 + "/get_transaction_status";

    //Merchant Payment
    public static final String calculate_merchant_payment = BASE_URL_TREX_ECS_STAGING + "/merchant/payments/calculate/";
    public static final String get_status_payment = BASE_URL_TREX_ECS_STAGING + "/merchant/payments?status=";
    public static final String put_status_payment = BASE_URL_TREX_ECS_STAGING + "/merchant/payments";


}
