package com.roryharford.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.zxing.WriterException;
import com.roryharford.event.Event;
import com.roryharford.ticket.Ticket;
import com.roryharford.ticket.TicketService;
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
public class GroupController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private TicketService ticketService;
	
	private Set<String> userForAGroup = new LinkedHashSet<>();

	@RequestMapping(value = "/addToGroup", method = RequestMethod.GET)
	public String addUserGroup(@RequestParam("id") String id,Model model,HttpSession session) {
		User user = (User) session.getAttribute("user");
		User friend = userService.getUser(Integer.parseInt(id));
		this.userForAGroup.add(friend.getEmail());
		System.out.println(this.userForAGroup.size());
		model.addAttribute("usersInGroup", userForAGroup);
		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "viewUsers";
	}
	
	@RequestMapping(value = "/removeFromGroup", method = RequestMethod.GET)
	public String removeUserGroup(@RequestParam("id") String id,Model model,HttpSession session) {
		User user = (User) session.getAttribute("user");
		User friend = userService.getUser(Integer.parseInt(id));
		this.userForAGroup.remove(friend.getEmail());
		model.addAttribute("usersInGroup", userForAGroup);
		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "viewUsers";
	}
	
	
	
	@RequestMapping(value = "/makeGroup", method = RequestMethod.GET)
	public String makeGroup(Model model,HttpSession session,@RequestParam("inputGroup") String groupName) {
		User user = (User) session.getAttribute("user");
	    Group group = new Group(groupName);;
    
	    for(String email : this.userForAGroup) {
	    	User friend =userService.getUserByEmail(email);
	    	 group.getFriends().add(friend);
	    }
		group.setUser(user);
	    groupService.addGroup(group);
	    model.addAttribute("lists", ticketService.getEventList());
		return "success";
	}
	
	
	
	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public String createGroup(Model model,HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		this.userForAGroup = new LinkedHashSet<>();
		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "viewUsers";
	}
}
