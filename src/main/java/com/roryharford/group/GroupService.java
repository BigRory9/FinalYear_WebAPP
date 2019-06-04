package com.roryharford.group;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roryharford.user.User;
import com.roryharford.user.UserRepository;

@Service
public class GroupService {
	
	@Autowired
	private GroupRepository groupRepository;

	public Group getGroup(int id) {
		// return Users.stream().filter(t -> t.getId().equals(id)).findFirst().get();
		// It knows the id is a String because we set it in the User class
		return groupRepository.getOne( id);
	}
	
	public List<Group> getAllGroups() {
		// connects to the database and runs a query
		List<Group> groups = new ArrayList<>();
		// adds each User into the array
		groupRepository.findAll().forEach(groups::add);
		return groups;
	}
	
	public List<Group> getAllUsersGroups(int id) {
		List<Group> groups = new ArrayList<>();
		for(int i=0;i<this.getAllGroups().size();i++) {
			if(this.getAllGroups().get(i).getUser().getId() ==id) {
				groups.add(this.getAllGroups().get(i));
			}
		}
		return groups;
	}

	public void addGroup(Group group) {
		groupRepository.save(group);
	}

}
