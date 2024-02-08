package site.timecapsulearchive.core.infra.sms.service;

import site.timecapsulearchive.core.infra.sms.data.response.SmsApiResponse;
import site.timecapsulearchive.core.infra.sms.exception.ExternalApiException;

public interface SmsApiService {

    /**
     * 수신자와 메시지를 받아서 Sms Api에 요청을 보낸다. 실제 문자 도착에는 지연이 발생할 수 있다.
     *
     * @param receiver 수신자
     * @param message  보낼 메시지
     * @return Sms Api 응답
     */
    SmsApiResponse sendMessage(String receiver, String message) throws ExternalApiException;
}
