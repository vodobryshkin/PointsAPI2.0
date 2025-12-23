package com.vdska.pointsapi2.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * DTO для передачи данных по очереди сообщений на отправку письма по электронной почте
 */
@Data
@AllArgsConstructor
public class ConfirmAccountMailRequest {
    private String from;
    private String to;
    private String subject;
    private String link;
}
