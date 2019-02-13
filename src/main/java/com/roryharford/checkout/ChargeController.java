package com.roryharford.checkout;

import com.roryharford.checkout.ChargeRequest.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ChargeController {
 
    @Autowired
    private StripeService paymentsService;
 
    @RequestMapping(method = RequestMethod.POST, value = "charge")
    public String charge(ChargeRequest chargeRequest, Model model)
      throws StripeException {
    	System.out.println("HELL000O");
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.EUR);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";
    }
 
    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
    	System.out.println("HELLO");
        model.addAttribute("error", "HELLO MY OLD FRIEND "+ex.getMessage());
        return "result";
    }
}
