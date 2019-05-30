package com.roryharford.group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private List<User> userForAGroup = new ArrayList<>();

	@RequestMapping(value = "/addToGroup", method = RequestMethod.GET)
	public String addUserGroup(@RequestParam("id") String id,Model model,HttpSession session) {
		User user = (User) session.getAttribute("user");
		User friend = userService.getUser(Integer.parseInt(id));
		this.userForAGroup.add(friend);
		System.out.println(this.userForAGroup.size());
		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "viewUsers";
	}
	
	
	@RequestMapping(value = "/makeGroup", method = RequestMethod.GET)
	public String makeGroup(Model model,HttpSession session,@RequestParam("inputGroup") String groupName) {
		System.out.println("The Group Name "+groupName);
		User user = (User) session.getAttribute("user");
		

	    Group group = new Group(groupName);
//	    group.setUser(user);
	   
//	    
	    for(int i=0;i<this.userForAGroup.size();i++) {
	    	 group.getFriends().add(this.userForAGroup.get(i));
	    }
		group.setUser(user);
	    groupService.addGroup(group);
//	    userService.updateUser(Integer.toString(user.getId()), user);
	    
//	    groupService.updateGroup(group.getId(), group);
//	   
//	    
//		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "success";
	}
	
	
	
	@RequestMapping(value = "/createGroup", method = RequestMethod.GET)
	public String createGroup(Model model,HttpSession session) {
		
		User user = (User) session.getAttribute("user");
		this.userForAGroup = new ArrayList<>();
		model.addAttribute("lists", userService.getAllUsersForGroup(user.getEmail()));
		return "viewUsers";
	}
}
