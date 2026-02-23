package com.UserAuthenticationService.UserAuthenticationService.service;

import com.UserAuthenticationService.UserAuthenticationService.dto.*;
import com.UserAuthenticationService.UserAuthenticationService.entities.*;
import com.UserAuthenticationService.UserAuthenticationService.repository.CityRepository;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import com.UserAuthenticationService.UserAuthenticationService.dto.OwnerSignupEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    AuthenticationManager authManager;

    // private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);

    public User register(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstname(signUpRequest.getFirstname());
        user.setLastname(signUpRequest.getLastname());
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        // 3️⃣ Address mapping
        List<Address> addresses = new ArrayList<>();

        if (signUpRequest.getAddress() != null) {
            for (AddressCreateDTO dto : signUpRequest.getAddress()) {

                Address address = new Address();
                address.setAddressLine1(dto.getAddressLine1());
                address.setAddressLine2(dto.getAddressLine2());
                address.setLandmark(dto.getLandmark());
                address.setPincode(dto.getPincode());
                address.setCity(dto.getCity());
                address.setState(dto.getState());
                address.setType(dto.getType());

                // 🔥 VERY IMPORTANT
                // Cascading (CascadeType.ALL on User -> addresses) means: “when I save the
                // User, also save all addresses attached to it.”
                address.setUser(user);

                addresses.add(address);
            }
        }
        user.setAddresses(addresses);

        CityDto cityDto = signUpRequest.getCityDto();
        if (cityDto != null) {
            City city = cityRepository.findByName(cityDto.getName());
            if (city == null) {
                throw new RuntimeException("City not found!");
            } else {
                user.setCity(city);
            }
        }
        User savedUser = repo.save(user);

        return savedUser;
    }

    public JWTAuthenticationResponse verifyUser(SignInRequest signInRequest) throws IllegalAccessException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));

        User user = repo.findByEmail(signInRequest.getEmail());
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(signInRequest.getEmail());
            String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
            JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
            jwtAuthenticationResponse.setToken(token);
            jwtAuthenticationResponse.setRefreshToken(refreshToken);
            return jwtAuthenticationResponse;

        } else {
            throw new IllegalAccessException("Invalid User");
        }
    }

    public JWTAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = repo.findByEmail(userEmail);
        if (jwtService.validateRefreshToken(refreshTokenRequest.getToken(), userEmail)) {
            String token = jwtService.generateToken(userEmail);

            JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
            jwtAuthenticationResponse.setToken(token);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
