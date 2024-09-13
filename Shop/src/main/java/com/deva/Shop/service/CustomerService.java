package com.deva.Shop.service;

import com.deva.Shop.entity.Customer;
import com.deva.Shop.exception.CommonResponse;
import com.deva.Shop.repository.CustomerRepository;
import com.deva.Shop.request.CustomerLoginRequest;
import com.deva.Shop.response.CustomerLoginResponse;
import com.deva.Shop.response.ServiceResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthService authService;

    public ServiceResponse<CustomerLoginResponse> customerLogin(CustomerLoginRequest request){
        Customer customer = customerRepository.findByEmail(request.getEmail());
        if (customer == null || !customer.getPassword().equals(request.getPassword())) {
            return new ServiceResponse<>(null, "Invalid email or password.", HttpStatus.BAD_REQUEST);
        }
        if(!customer.isVerified()){
            return new ServiceResponse<>(null, "Verify first by giving otp.", HttpStatus.BAD_REQUEST);

        }

        String token = generateToken(customer);



        CustomerLoginResponse response = new CustomerLoginResponse(token, customer.getName());

        return new ServiceResponse<>(response, "Login successfully", HttpStatus.OK);

    }

    public CommonResponse customerLogout(HttpServletRequest req, HttpServletResponse res){
        String token = req.getHeader("Authorization");
        Customer customer = authService.getUserFromToken(token);

        if (customer != null) {
            customerRepository.updateToken(customer.getId(), null);
            Cookie tokenCookie = new Cookie("token", null);
            tokenCookie.setMaxAge(0);
            tokenCookie.setPath("/");
            res.addCookie(tokenCookie);
            CommonResponse response = new CommonResponse(HttpStatus.OK, "Logout successful!");
            return response;
        } else {
            CommonResponse response = new CommonResponse(HttpStatus.UNAUTHORIZED, "Invalid Token or Network related Issue!");
            return response;
        }
    }

    private String generateToken(Customer customer) {
        byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
        String token = Jwts.builder()

                .setSubject(customer.getEmail())
                .claim("name", customer.getName())
                .claim("role", "customer")
                .signWith(SignatureAlgorithm.HS512, keyBytes)
                .compact();

        customer.setToken(token);
        customerRepository.updateToken(customer.getId(), token);

        return token;
    }
}
