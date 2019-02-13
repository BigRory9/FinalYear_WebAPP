package com.roryharford.ticket;

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
//		Event event = userService.getOneEvent(id);
//		Ticket ticket = new Ticket (event.getDisplayName());
//		User user = (User) session.getAttribute("user");
//		System.out.println(user.getName());
//		ticketService.createTicket(ticket);
//		user.addTicket(ticket);
//		userService.updateUser(id, user);
//		httpServletResponse.setHeader("Location", "http://localhost/paypage/");
//	    httpServletResponse.setStatus(302);
		return "creditCard";
	}
}


