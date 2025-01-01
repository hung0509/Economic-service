package vn.shop.economic_service.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.shop.economic_service.dto.request.MailRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.service.EmailService;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/mails")
public class MailController {
    EmailService emailService;

//    @GetMapping
//    public ApiResponse<String> sendMail(@RequestBody MailRequest request)  {
//        emailService.sendMail(request);
//        return ApiResponse.<String>builder()
//                .result("Gui thanh cong!!")
//                .build();
//    }
}
