package main.application.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import main.paypal.PaymentDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaypalServiceImpl implements PaypalService {

    @Value("${paypal.id}")
    private String ID;
    @Value("${paypal.secret}")
    private String SECRET;
    private final String MODE = "sandbox";


    @Value("${domain}")
    private String domain;

    // AUTORIZAR EL PAGO
    //
    // autoriza el pago y devuelve el link de aprobado
    public String authorizePayment(PaymentDetails payment) throws PayPalRESTException {

        Payer payer = getPayerInformation();
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(payment);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls); // se le proporcionan URLS para redirigir en un paso intermedio
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(ID, SECRET, MODE);

        Payment approvedPayment = requestPayment.create(apiContext);

        return getApprovalLink(approvedPayment);

    }

    // devuelve un objeto Payer (cliente) con los datos correspondientes para poder usarlo en la transaccion
    private Payer getPayerInformation() {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        return payer;
    }

    // devuelve las urls a las que redirigir en un paso intermedio de la transaccion
    // cancelURL es para cuando el cliente cancele la operacion
    // returnURL es para cuendo el cliente quiere seguir con la operacion
    private RedirectUrls getRedirectURLs() {

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://"+ domain+":8080/sponsor/payment/cancel.html");
        redirectUrls.setReturnUrl("http://"+ domain+":8080/sponsor/payment/review");

        return redirectUrls;
    }

    // devuelve una lista con los datos de la transaccion, esto permite que las clases superiores utilicen la info
    // para mostrar resultados, guardar en BD...
    private List<Transaction> getTransactionInformation(PaymentDetails payment) {

        Details details = new Details();
        details.setShipping(payment.getShipping());
        details.setSubtotal(payment.getSubtotal());
        details.setTax(payment.getTax());

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(payment.getTotal());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(payment.getSponsorType());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setCurrency("EUR");
        item.setName(payment.getSponsorType());
        item.setPrice(payment.getSubtotal());
        item.setTax(payment.getTax());
        item.setQuantity("1");

        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    // se encarga de extraer la url que se necesita para llevar la transaccion a cabo
    // practicamente lo implementa la API solo
    private String getApprovalLink(Payment approvedPayment) {

        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    // devuelve un objeto Payment que contiene informacion para poder realizar la transaccion
    // se podria decir que une la transaccion (por id) y los datos de a quien se le paga
    public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {

        APIContext apiContext = new APIContext(ID, SECRET, MODE);
        return Payment.get(apiContext, paymentId);
    }

    // este es el metodo que lleva a cabo la transaccion con los datos que se le proporcionan
    // es el que "ejecuta" la orden
    public Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException {

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(ID, SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }

}
