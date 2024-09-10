package Kevin.demo_jwt.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Kevin.demo_jwt.Jwt.JwtService;
import Kevin.demo_jwt.User.Role;
import Kevin.demo_jwt.User.User;
import Kevin.demo_jwt.User.UserRepository;
import lombok.RequiredArgsConstructor;
	
@Service
@RequiredArgsConstructor
public class AuthService {
	
	 private final UserRepository userRepository;
	 private final JwtService jwtService;
	 private final PasswordEncoder passwordEncoder;
	 private final AuthenticationManager authenticationManager;
	 
	public AuthResponse login(loginRequest request) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		String token =jwtService.getToken(user);
		return AuthResponse.builder()
			.token(token)
			.build();
	}
	public AuthResponse register(RegisterRequest request) {
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.firstname(request.getFirstname())
				.lastname(request.getLastname())
				.country(request.getCountry())
				.role(Role.USER)
				.build();
		

		userRepository.save(user);
		
		return AuthResponse.builder()
				.token(jwtService.getToken(user))
				.build();
		
	}
}