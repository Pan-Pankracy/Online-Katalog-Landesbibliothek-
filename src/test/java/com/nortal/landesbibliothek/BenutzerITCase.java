package com.nortal.landesbibliothek;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BenutzerITCase {

    private static final String BASE_URL = "https://eiffage-xrechnungsgenerator.nortal.com";
    private static final String BENUTZER_PATH = "/v1/benutzer";

    private static final String REQUEST_BODY = "{\n" +
            "\"ausweisnummer\": \"169091050510\",\n" +
            "\"passwort\": \"20010212\",\n" +
            "\"email\":\n" +
            "\"maxmustermann@nortal.com\" }";

    private static final String REQUEST_BODY_WRONG_AUSWEISNUMMER = "{\n" +
            "\"ausweisnummer\": \"000000000001\",\n" +
            "\"passwort\": \"20010212\",\n" +
            "\"email\":\n" +
            "\"maxmustermann@nortal.com\" }";

    private static final String REQUEST_BODY_EMPTY_PROPERTIES = "{\n" +
            "\"ausweisnummer\": \"169091050510\",\n" +
            "\"passwort\": \"\",\n" +
            "\"email\":\n" +
            "\"\" }";

    private static final String REQUEST_BODY_WRONG_EMAIL = "{\n" +
            "\"ausweisnummer\": \"169091050510\",\n" +
            "\"passwort\": \"20010212\",\n" +
            "\"email\":\n" +
            "\"test\" }";


    @Test
        //Empty database to make sure this test will pass (unique card number is necessary)
    void registerUserTest() {
        given().baseUri(BASE_URL).contentType("application/json").body(REQUEST_BODY).when().post(BENUTZER_PATH).then().log().all().statusCode(201).body(
                "ausweisnummer", equalTo("169091050510"), "status", equalTo("aktiv"));
    }

    @Test
    void reRegisterUserTest() {
        given().baseUri(BASE_URL).contentType("application/json").body(REQUEST_BODY).when().post(BENUTZER_PATH).then().log().all().statusCode(409).body(
                "code", equalTo(409), "fehler", equalTo("Es gibt bereits einen Nutzer mit der Ausweisnummer '169091050510' in der Datenbank."));
    }

    @Test
    void RegisterUserTestWrongAusweisnummer() {
        given().baseUri(BASE_URL).contentType("application/json").body(REQUEST_BODY_WRONG_AUSWEISNUMMER).when().post(BENUTZER_PATH).then().log().all().statusCode(400).body(
                "code", equalTo(400), "fehler", equalTo("Ausweisnummer '000000000001' ist nicht registriert."));
    }

    @Test
    void RegisterUserTestEmptyProperties() {
        given().baseUri(BASE_URL).contentType("application/json").body(REQUEST_BODY_EMPTY_PROPERTIES).when().post(BENUTZER_PATH).then().log().all().statusCode(400).body(
                "code", equalTo(400), "fehler", equalTo("Es wurden nicht alle benötigten properties übergeben. Gefehlt haben: 'passwort, email'."));
    }

    @Test
        //Unique and unregistered "Ausweisnummer" is necessary in order to run the below test
    void RegisterUserTestWrongEmail() {
        given().baseUri(BASE_URL).contentType("application/json").body(REQUEST_BODY_WRONG_EMAIL).when().post(BENUTZER_PATH).then().log().all().statusCode(400).body(
                "code", equalTo(400), "fehler", equalTo("Email 'test' ist ungültig."));
    }


}



