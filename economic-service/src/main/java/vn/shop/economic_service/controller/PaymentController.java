package vn.shop.economic_service.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import vn.shop.economic_service.dto.request.PaypalRequest;
import vn.shop.economic_service.dto.response.ApiResponse;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.service.OrderService;
import vn.shop.economic_service.service.PaypalService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {
    PaypalService paypalService;
    OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<String> createPayment(@RequestBody PaypalRequest request) throws PayPalRESTException, IOException {
        Payment payment = paypalService.creatPayment(request);

        for(Links link: payment.getLinks()){
            if(link.getRel().equals("approval_url")){
                return ApiResponse.<String>builder()
                        .result(link.getHref())
                        .build();
            }
        }

        throw new AppException(ErrorCode.PAYMENT_CREATE_ERROR);
    }

    @GetMapping("/success")
    public RedirectView paymentSuccess(@RequestParam("paymentId") String paymentId,
                                       @RequestParam("PayerID") String payerId
                                        )
            throws PayPalRESTException, IOException {
        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {

             return new RedirectView("http://localhost:4200/payment/success");
        }
        return new RedirectView("http://localhost:4200/payment/error");
    }

    @GetMapping("/error")
    public RedirectView paymentError(){
        return new RedirectView("http:/localhost:4200/payment/error");
    }

}
