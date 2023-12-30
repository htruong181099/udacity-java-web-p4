package com.example.demo.controllers;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final PasswordEncoder passwordEncoder;
	private final static int PASSWORD_MIN_SIZE = 7;

	public UserController(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		try {
			Optional<User> userOpt = userRepository.findById(id);
			if (!userOpt.isPresent()) {
				log.info("findById | User not found | id " + id);
				return ResponseEntity.notFound().build();
			}
			log.info("findById | Success | id " + id);
			return ResponseEntity.ok(userOpt.get());
		} catch (Exception e) {
			log.error("findById | Error " ,e);
			return ResponseEntity.status(500).build();
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		try {
			User user = userRepository.findByUsername(username);
			if (user == null) {
				log.info("findByUserName | User not found | username " + username);
				return ResponseEntity.notFound().build();
			}
			log.info("findByUserName | Success | username " + username);
			return ResponseEntity.ok(user);
		}
		catch (Exception e){
			log.error("findByUserName | Error | username " + username ,e);
			return ResponseEntity.status(500).build();
		}
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		try {
			if (!isGoodPassword(createUserRequest.getPassword(), createUserRequest.getConfirmPassword())) {
				log.info("CreateUser | Password did not meet requirements.");
				return ResponseEntity.badRequest().build();
			}
			User user = new User();
			Cart cart = new Cart();
			cartRepository.save(cart);
			user.setCart(cart);

			user.setUsername(createUserRequest.getUsername());

			String encodedPassword = passwordEncoder
					.encode(createUserRequest.getPassword());
			user.setPassword(encodedPassword);
			userRepository.save(user);
			log.info("CreateUser | Success ");
			return ResponseEntity.ok(user);
		} catch (Exception e){
			log.error("CreateUser | Error " ,e);
			return ResponseEntity.status(500).build();
		}
	}

	boolean isGoodPassword(String password, String confirmPassword){
		return password != null && password.length() > PASSWORD_MIN_SIZE
				&& password.equals(confirmPassword);
	}
	
}
