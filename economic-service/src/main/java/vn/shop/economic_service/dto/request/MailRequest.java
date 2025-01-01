package vn.shop.economic_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailRequest {
    String fromAddress;
    String toAddress;
    String subject;
    String content;
}
