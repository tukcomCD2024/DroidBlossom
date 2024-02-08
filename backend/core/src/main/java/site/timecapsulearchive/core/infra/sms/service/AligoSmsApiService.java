package site.timecapsulearchive.core.infra.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.infra.sms.config.AligoSmsProperties;
import site.timecapsulearchive.core.infra.sms.dto.SmsApiResponse;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;

@Service
@RequiredArgsConstructor
public class AligoSmsApiService implements SmsApiService {

    private static final String SEND_URL = "https://apis.aligo.in/send/";
    private static final Integer SUCCESS_STATUS = 1;
    private static final String KEY = "key";
    private static final String USER_ID = "user_id";
    private static final String SENDER = "sender";
    private static final String TESTMODE_YN = "testmode_yn";
    private static final String MSG = "msg";
    private static final String RECEIVER = "receiver";

    private final RestTemplate restTemplate;
    private final AligoSmsProperties aligoSmsProperties;

    public SmsApiResponse sendMessage(
        final String receiver,
        final String message
    ) throws ExternalApiException {
        final HttpEntity<MultiValueMap<String, String>> request = createMessageRequest(receiver,
            message);

        final SmsApiResponse response = restTemplate.postForObject(
            SEND_URL,
            request,
            SmsApiResponse.class
        );

        if (isError(response)) {
            throw new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR);
        }

        return response;
    }

    private HttpEntity<MultiValueMap<String, String>> createMessageRequest(
        final String receiver,
        final String message
    ) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add(KEY, aligoSmsProperties.apiKey());
        map.add(USER_ID, aligoSmsProperties.userId());
        map.add(SENDER, aligoSmsProperties.sender());
        map.add(TESTMODE_YN, aligoSmsProperties.testmodeYn());
        map.add(MSG, message);
        map.add(RECEIVER, receiver);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(map, headers);
    }

    private boolean isError(final SmsApiResponse body) {
        return body == null || !body.resultCode().equals(SUCCESS_STATUS);
    }
}