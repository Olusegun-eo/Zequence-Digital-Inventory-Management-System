package com.zequence.ZequenceIms.utils;
/*
import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.auth.Secret;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponse;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Component
public class GoogleOAuth2Util {

    private OkHttpClient httpClient = new OkHttpClient();
    private JSONParser parser = new JSONParser();
    private ClientRegistrationRepository clientRegistrationRepository;

    public GoogleOAuth2Util(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    public String getUserIdFromGoogle(String idToken) {
        try {
            OIDCTokenResponse tokenResponse = parseTokenResponse(idToken);
            //JSONObject payload = (JSONObject) parser.parse(tokenResponse.getAccessToken().getValue());
            String decodedIdToken = JWT.decode(tokenResponse.getIdToken().getValue()).getPayload();
            JSONObject payload = (JSONObject) parser.parse(decodedIdToken);
            return (String) payload.get("sub");
        } catch (ParseException | IllegalArgumentException | URISyntaxException ex) {
            throw new IllegalStateException("Unable to retrieve user id from Google Id Token.", ex);
        }
    }

    public OIDCProviderMetadata getGoogleProviderMetadata() throws URISyntaxException {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        //return new OIDCProviderMetadata(clientRegistration.getProviderDetailsUrl());
        OIDCProviderMetadata providerMetadata = clientRegistration.getProviderDetails();
        OIDCTokenResponse tokenResponse = parseTokenResponse(idToken);

    }

    public IDTokenResponse parseTokenResponse(String idToken) throws ParseException {
        OIDCProviderMetadata providerMetadata = clientRegistration.getProviderDetails();
        return IDTokenResponse.parse(providerMetadata, idToken);
    }

    public String sendPostRequest(String uri, String body) throws IOException {
        RequestBody requestBody = RequestBody.create(body, StandardCharsets.UTF_8);
        Request request = new Request.Builder()
                .url(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected HTTP response returned: " + response.code());
        }
    }

    public String getGoogleProfileInfo(String accessToken) throws IOException, URISyntaxException, org.json.simple.parser.ParseException {
        String userInfoEndpoint = getGoogleProviderMetadata().getUserInfoEndpointURI().toString();
        String queryParameters = String.format("access_token=%s", accessToken);
        String jsonStr = sendPostRequest(userInfoEndpoint, queryParameters);
        return (String) ((JSONObject) parser.parse(jsonStr)).get("email");
    }

}
*/