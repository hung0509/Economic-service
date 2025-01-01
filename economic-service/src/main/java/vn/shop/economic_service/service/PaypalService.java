package vn.shop.economic_service.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.dto.request.PaypalRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PaypalService {
    APIContext apiContext;

    public Payment creatPayment(PaypalRequest request) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(request.getCurrency());
        amount.setTotal(String.format(Locale.forLanguageTag(request.getCurrency()), "%.2f", request.getTotal()));

        Transaction transaction = new Transaction();
        transaction.setDescription(request.getDescription());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(request.getMethod());

        Payment payment = new Payment();
        payment.setIntent(request.getIntent());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(request.getUrlError());
        redirectUrls.setReturnUrl(request.getUrlSuccess());

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String payment_id, String payer_id) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(payment_id);

        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payer_id);

        return payment.execute(apiContext, execution);
    }
}
