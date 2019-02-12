package com.roryharford.user;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, String>{
	//Only thing that changes is the type of class methods stay the same 
	//Have all the crud Statements
	//Only have to implement custom statements
	
	
}
