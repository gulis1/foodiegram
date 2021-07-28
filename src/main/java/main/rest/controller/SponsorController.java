package main.rest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import main.application.service.PaypalService;
import main.application.service.SponsorService;
import main.domain.resource.SponsorResource;
import main.paypal.PaymentDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/sponsor")
public class SponsorController {

    // TIPOS DE PATROCINIO (duracion)
    //
    // type=1: 1 mes
    // type=2: 3 meses
    // type=3: 6 meses
    // type=4: 12 meses

    @Autowired
    private SponsorService sponsorService;

    @Autowired
    private PaypalService paypalService;


    // OBTENER PATROCINIO
    //
    // devuelve un patrocinio especifico buscado por id de colaborador
    // devuelve null si no existe
    @GetMapping
    public ResponseEntity<SponsorResource> getSponsorship() {
        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        SponsorResource sponsorship = sponsorService.getSponsorship(userID);
        return sponsorship != null ? ResponseEntity.ok(sponsorship) : ResponseEntity.notFound().build();
    }


    // AUTORIZAR EL PAGO
    //
    // se encarga de autorizar el pago por paypal
    // devuelve el link a la pagina para que haga login en paypal
    // despues redirige a una pagina para continuar/cancelar el proceso (esto es de paypal)
    @RequestMapping(value="/payment/auth", method = RequestMethod.POST)
    public ResponseEntity<String> authorizePayment(@RequestParam("sponsorType") String sponsorType) throws Exception {

        try {
            PaymentDetails payment = new PaymentDetails(sponsorType);
            String approvalLink = paypalService.authorizePayment(payment);

            return new ResponseEntity<>(approvalLink, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + ex.getMessage());
        }
    }

    // REVISAR EL PAGO
    //sy
    // se encarga de hacer la fase intermedia de mostrar el pago al cliente
    // devuelve datos necesarios para construir la pagina donde se muestra la transaccion
    @RequestMapping(value="/payment/review", method = RequestMethod.GET)
    public ModelAndView reviewPayment(Model model, @RequestParam("paymentId") String paymentId,
                                           @RequestParam("PayerID") String payerId) throws Exception {



        Payment payment = paypalService.getPaymentDetails(paymentId);

        PayerInfo payerInfo = payment.getPayer().getPayerInfo();
        Transaction transaction = payment.getTransactions().get(0);
        ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

        // se mete informacion en una lista para poder utilizar los datos en clases superiores
        Map<String, Object> data = new HashMap<>();
        data.put("paymentID", paymentId);
        data.put("payerID", payerId);
        data.put("payerInfo", payerInfo);
        data.put("transaction", transaction);
        data.put("shippingAddres", shippingAddress);

        Gson gson = new GsonBuilder().setPrettyPrinting().create()

                ;


        model.addAttribute("details", gson.toJson(data));
        return new ModelAndView("reviewPayment");


    }


    // EJECUTA EL PAGO
    //
    // se encarga de ejecutar el pago de paypal, despues de
    // ejecutarse con exito se encarga de crear/modificar el patrocinio del colaborador
    // devuelve los datos del patrocinio que se ha creado/modificado
    @RequestMapping(value="/payment/execute", method = RequestMethod.POST)
    public ResponseEntity<?> executePayment(@RequestParam("paymentID") String paymentId, @RequestParam("payerID") String payerId) throws Exception {

        Integer userID = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
       try {

           Payment payment = paypalService.executePayment(paymentId, payerId);
           SponsorResource sponsorship = null;

           // se obtiene el patrocinio actual del colaborador
           String type = payment.getTransactions().get(0).getItemList().getItems().get(0).getName();
           String money = payment.getTransactions().get(0).getAmount().getTotal();

           if(sponsorService.getSponsorship(userID) == null)  // se crea el patrocinio
               sponsorship = sponsorService.obtain(userID, Integer.parseInt(type), Float.parseFloat(money));

           else // se modifica el patrocinio
               sponsorship = sponsorService.modify(userID, Integer.parseInt(type), Float.parseFloat(money));


           return sponsorship != null ? ResponseEntity.ok(sponsorship) : ResponseEntity.notFound().build();
       }

       catch (Exception ex) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
       }
    }
}