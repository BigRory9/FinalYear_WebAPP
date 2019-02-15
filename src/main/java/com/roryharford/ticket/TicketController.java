package com.roryharford.ticket;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.roryharford.event.Event;
import com.roryharford.user.User;
import com.roryharford.user.UserService;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

@Controller
public class TicketController {
	
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private UserService userService;
	
//	@RequestMapping(value = "/tickets")
//	public String countsList(Model model) {
//	    model.addAttribute("tickets", ticketService.getAllTickets());
//	    return "tickets";
//	}
	
	@RequestMapping(value = "/purchase-tickets/{id}", method=RequestMethod.GET)
	public String purchase(@PathVariable("id") String id,HttpServletResponse httpServletResponse,HttpSession session,@ModelAttribute User customer) {
		return "creditCard";
	}
	
	@RequestMapping(value = "/purchase-tickets/{id}/process", method=RequestMethod.POST)
	public String process(@PathVariable("id") String id,HttpServletResponse httpServletResponse,HttpSession session,@ModelAttribute User customer) throws AuthenticationException, InvalidRequestException, APIConnectionException, CardException, APIException {
		System.out.println(id);
		Event event = userService.getOneEvent(id);
		Ticket ticket = new Ticket (event.getDisplayName(),event.getArena(),event.getDate(),event.getPrice(),event.getTime());
		User user = (User) session.getAttribute("user");
		System.out.println(user.getName());
		ticketService.createTicket(ticket);
		user.addTicket(ticket);
		userService.updateUser(id, user);
		Stripe.apiKey = "sk_test_UIcZ6w9lltQi6Vn5VlDCtRk5";
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (event.getPrice()*100));
		chargeParams.put("currency", "eur");
		chargeParams.put("description", event.getDisplayName());
		chargeParams.put("source", "tok_amex");
		Charge.create(chargeParams);
//		Transaction_ticket t = new Transaction_ticket(event.getPrice(), "eur");
//		ticketService.addTransaction();
		return "homepage";
	}
}


