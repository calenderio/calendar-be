/*
 * @author : Oguz Kahraman
 * @since : 5.08.2021
 *
 * Copyright - Calendar App Java API
 **/
package com.io.fastmeet.services.impl;

import com.google.gson.Gson;
import com.io.fastmeet.core.exception.CalendarAppException;
import com.io.fastmeet.core.i18n.Translator;
import com.io.fastmeet.enums.CalendarProviderType;
import com.io.fastmeet.models.remotes.google.TokenRefreshResponse;
import com.io.fastmeet.models.remotes.microsoft.Attachment;
import com.io.fastmeet.models.remotes.microsoft.CalendarEventItem;
import com.io.fastmeet.models.remotes.microsoft.CalendarResponse;
import com.io.fastmeet.models.remotes.microsoft.DateType;
import com.io.fastmeet.models.remotes.microsoft.EmailAddress;
import com.io.fastmeet.models.remotes.microsoft.EmailAddressItem;
import com.io.fastmeet.models.remotes.microsoft.EventResponse;
import com.io.fastmeet.models.remotes.microsoft.MicrosoftCalendarEventsRequest;
import com.io.fastmeet.services.MicrosoftService;
import com.io.fastmeet.utils.GeneralMessageUtil;
import com.io.fastmeet.utils.GenericHttpRequestUtil;
import com.io.fastmeet.utils.GenericProviderUtil;
import com.io.fastmeet.utils.MicrosoftGraphURL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MicrosoftServiceImpl implements MicrosoftService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    @Value("${spring.security.oauth2.client.registration.microsoft.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.microsoft.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.microsoft.scope}")
    private String scopes;
    @Value("${spring.security.oauth2.client.registration.microsoft.redirect-uri}")
    private String redirectUri;

    public static void example() throws IOException, InterruptedException {
        CalendarEventItem eventRequest = new CalendarEventItem();
        eventRequest.setSubject("Exmaple Subject");
        eventRequest.setStart(new DateType(LocalDateTime.now().toString(), "GMT"));
        eventRequest.setEnd(new DateType(LocalDateTime.now().plusHours(1).toString(), "GMT"));
        eventRequest.getAttendees().add(new EmailAddressItem(new EmailAddress("oguzkahraman52@gmail.com", "Oguz Kahraman")));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MicrosoftGraphURL.CREATE_CALENDAR_URL))
                .header(GenericHttpRequestUtil.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(GenericProviderUtil.AUTHORIZATION,
                        "Bearer EwB4A8l6BAAU6k7+XVQzkGyMv7VHB/h4cHbJYRAAAav4r4YjrYlnQyngBhV0ExVT28cmAJ/xMSCjD/wOHO+CWk2LjD2ExXEwiuZKg5kl8CLX8R2+x8t1F70diHpCmYBwTS+sBpMNZKm5F4A+sxyrdV564lKknTslLezqiV/+jn5kLsVktS1t2wD+PrYUlTFhuo7NhkMBZDDChRc7rV2oBqDqbusT4DIm5RE3H9Fpj5IURA8MmgMPhYj30WRXJRbPHudxY7Vh6OKBLu1CextgedSmL49fW/XT0bUM7u2P8u7fd3Hv0TqHwzT766xh7CsN7svDHcYMSsTF36earBSkYIle1NG09I0qDqPRaF72xSKutikUSusFeKRfcE2vg5MDZgAACGSGceBAQZuoSAJ2lE+bDFSgk5Afjp4EMFneXCtfe6aLWqTEx+kmyiD1/7eUH7VV40IFu2Nk1U8QR+5v6Ebrnoq8aCAJ6oSK2Rf2mQPw1KGovSOiXDTEBBg/RgOW3KY8mW7450CJVWa0yPN9QBwZ+sPiFa/mZ4EPacosiO+AdcMVMNuCRz807HVZnViFJ1BJVqi0uzO009KXes3m+qEVSb3r5TIuS40jcbsC7cFWrSIED8T1YPQE1GYBE5xh8UXOC45gnsjO1Rz4zzrEi965ZNWhRoj4Sr/0nyXEFq922uedAUp6NN+9X6ozI++m0R8arlSjQFBQh1OlT1dXe27rVtrI5g7cL+PUESIQ5KgjiI7rhRf4PQDCDum1ZGXBocMT91O7H+Zr5urfLA8N2cazqdvkTQbD5oIN/cWjnUn+Oajosrjs+0o7GoYoO1fV0ye9cenHK8HAUiC6ggZNACj9uuwGh5EYSpP7roNqq4Hm5+CjmzkgvRtwCF61TXMoouO65E4YvHJyUh9A0DTXLHQzfLVnAnsvJmyCG7UiquP1YHkV319crMZt5sfV2pDc/fNl37A78CpoBwTwNwA2/y8Fr4y6yidNyv3v7heJK4862yXOCqjlDmR+oJfkgnprXhBO1rWTljflkOAjhoN/rycYpDCkeoj3y528IeJ6Vcfep0CnAzwl/LjkafG9eWFhjHkt931mjZVSu0M/Akslepa8jQ3AJy57fsn78bh2fGMgtI06upZC9u8JG84LeCk0Fe9QQrgBrVv0skZG9cvo86QPnsBrfXkC"
                )
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(eventRequest)))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        EventResponse response2 = new Gson().fromJson(response.body(), EventResponse.class);

        File file = new File("example.txt");
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        Attachment attachment = new Attachment();
        attachment.setName("deneme.txt");
        attachment.setContentBytes(new String(encoded, StandardCharsets.US_ASCII));
        HttpClient client2 = HttpClient.newHttpClient();
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create(String.format(MicrosoftGraphURL.ADD_ATTACHMENT_URL, response2.getId())))
                .header(GenericHttpRequestUtil.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(GenericProviderUtil.AUTHORIZATION,
                        "Bearer EwB4A8l6BAAU6k7+XVQzkGyMv7VHB/h4cHbJYRAAAav4r4YjrYlnQyngBhV0ExVT28cmAJ/xMSCjD/wOHO+CWk2LjD2ExXEwiuZKg5kl8CLX8R2+x8t1F70diHpCmYBwTS+sBpMNZKm5F4A+sxyrdV564lKknTslLezqiV/+jn5kLsVktS1t2wD+PrYUlTFhuo7NhkMBZDDChRc7rV2oBqDqbusT4DIm5RE3H9Fpj5IURA8MmgMPhYj30WRXJRbPHudxY7Vh6OKBLu1CextgedSmL49fW/XT0bUM7u2P8u7fd3Hv0TqHwzT766xh7CsN7svDHcYMSsTF36earBSkYIle1NG09I0qDqPRaF72xSKutikUSusFeKRfcE2vg5MDZgAACGSGceBAQZuoSAJ2lE+bDFSgk5Afjp4EMFneXCtfe6aLWqTEx+kmyiD1/7eUH7VV40IFu2Nk1U8QR+5v6Ebrnoq8aCAJ6oSK2Rf2mQPw1KGovSOiXDTEBBg/RgOW3KY8mW7450CJVWa0yPN9QBwZ+sPiFa/mZ4EPacosiO+AdcMVMNuCRz807HVZnViFJ1BJVqi0uzO009KXes3m+qEVSb3r5TIuS40jcbsC7cFWrSIED8T1YPQE1GYBE5xh8UXOC45gnsjO1Rz4zzrEi965ZNWhRoj4Sr/0nyXEFq922uedAUp6NN+9X6ozI++m0R8arlSjQFBQh1OlT1dXe27rVtrI5g7cL+PUESIQ5KgjiI7rhRf4PQDCDum1ZGXBocMT91O7H+Zr5urfLA8N2cazqdvkTQbD5oIN/cWjnUn+Oajosrjs+0o7GoYoO1fV0ye9cenHK8HAUiC6ggZNACj9uuwGh5EYSpP7roNqq4Hm5+CjmzkgvRtwCF61TXMoouO65E4YvHJyUh9A0DTXLHQzfLVnAnsvJmyCG7UiquP1YHkV319crMZt5sfV2pDc/fNl37A78CpoBwTwNwA2/y8Fr4y6yidNyv3v7heJK4862yXOCqjlDmR+oJfkgnprXhBO1rWTljflkOAjhoN/rycYpDCkeoj3y528IeJ6Vcfep0CnAzwl/LjkafG9eWFhjHkt931mjZVSu0M/Akslepa8jQ3AJy57fsn78bh2fGMgtI06upZC9u8JG84LeCk0Fe9QQrgBrVv0skZG9cvo86QPnsBrfXkC"
                )
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(attachment)))
                .build();
        HttpResponse<String> response3 = client2.send(request2,
                HttpResponse.BodyHandlers.ofString());
        response3.body();

    }

    @Override
    public void getCalendarEvents(MicrosoftCalendarEventsRequest request) {
        String timeZone = StringUtils.defaultIfBlank(request.getTimeZone(), "UTC");
        UriComponentsBuilder builder;
        if (request.getTimeMin() == null || request.getTimeMax() == null) {
            builder = UriComponentsBuilder.
                    fromUriString(String.format(MicrosoftGraphURL.CALENDAR_ALL_URL));
        } else {
            builder = UriComponentsBuilder.
                    fromUriString(String.format(MicrosoftGraphURL.CALENDAR_URL));
            if (request.getTimeMax() != null) {
                builder.queryParam("enddatetime", formatter.format(ZonedDateTime.of(request.getTimeMax(), ZoneId.of(timeZone)))
                        .replace('+', '-'));
            }
            if (request.getTimeMin() != null) {
                builder.queryParam("startdatetime", formatter.format(ZonedDateTime.of(request.getTimeMin(), ZoneId.of(timeZone)))
                        .replace('+', '-'));
            }
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(builder.build().toString()))
                .header(GenericProviderUtil.AUTHORIZATION,
                        "Bearer " + request.getAccessToken()
                )
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            CalendarResponse response1 = new Gson().fromJson(response.body(), CalendarResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.EXTERNAL_APP_MSG, CalendarProviderType.GOOGLE.name()),
                    GeneralMessageUtil.EXTERNAL_APP);
        }
    }

    @Override
    public TokenRefreshResponse getNewAccessToken(String refreshToken) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", clientId);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", "refresh_token");
        parameters.put("scope", scopes);
        parameters.put("refresh_token", refreshToken);
        parameters.put("redirect_uri", redirectUri);
        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MicrosoftGraphURL.REFRESH_TOKEN))
                .header(GenericHttpRequestUtil.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), TokenRefreshResponse.class);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            throw new CalendarAppException(HttpStatus.BAD_REQUEST, Translator.getMessage(GeneralMessageUtil.EXTERNAL_APP_MSG, CalendarProviderType.GOOGLE.name()),
                    GeneralMessageUtil.EXTERNAL_APP);
        }
    }

}
