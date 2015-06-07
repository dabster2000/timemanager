package dk.trustworks.timemanager.user.repos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.trustworks.timemanager.user.model.User;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.springframework.data.repository.query.Param;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserRepository {
	
	private ServiceProvider serviceProvider;

	RestTemplate restTemplate;
	
	public UserRepository() {

	}
	
	public User save(User user) {
		try {
	        String uri = new String(serviceProvider.getInstance().buildUriSpec()+"/api/users");
	        user = restTemplate.postForObject(uri, user, User.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return user;
	}
	
	public Iterable<User> findAll() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users", List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User findOne(String uuid) {
		try {
			return restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/"+uuid, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<User> findByEmail(String email){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/search/findByEmail?email="+email, List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//@Cacheable("users")
	public List<User> findByUsername(String username){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/search/findByUsername?username="+username, List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<User> findByUsernameAndPasswordAndActiveTrue(@Param("username") String username, @Param("password") String password){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/search/findByUsernameAndPasswordAndActiveTrue?username="+username+"&password="+password, List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//@Cacheable("users")
	public List<User> findByActive(boolean active){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/search/findByActive", List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<User> findByActiveTrueOrderByFirstnameAsc(){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.convertValue(restTemplate.getForObject(serviceProvider.getInstance().buildUriSpec()+"/api/users/search/findByActiveTrueOrderByFirstnameAsc", List.class), new TypeReference<List<User>>() { });
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
