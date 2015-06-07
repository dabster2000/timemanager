package dk.trustworks.timemanager.user.rest;

import dk.trustworks.timemanager.user.model.User;
import dk.trustworks.timemanager.user.repos.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j
@RestController
@RequestMapping("/usermanager/users")
public class UserWebService {

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(value = "/{userUUID}/command/activateUser", method = RequestMethod.POST)
	@Transactional
	public void activateUser(@RequestParam("username") String username, @RequestParam("password") String password) {
		List<User> users = userRepository.findByUsername(username);
		System.out.println(users.size());
		if(users.size() == 0) return;
		User user = users.get(0);
		
		System.out.println(user.isActive());
		System.out.println(user.getPassword());
		
		if(!users.get(0).isActive() || !users.get(0).getPassword().equals("")) return;
		
		
		user.setPassword(password);
		user.setActive(true);
		
		return;
	}
}
