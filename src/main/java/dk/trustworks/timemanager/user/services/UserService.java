package dk.trustworks.timemanager.user.services;

import dk.trustworks.timemanager.user.model.User;
import dk.trustworks.timemanager.user.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Iterable<User> getAllUsers() {
    	return userRepository.findAll();
	}
	
	public User findUserByUUID(String userUUID) {
    	return userRepository.findOne(userUUID);
	}
	
}
