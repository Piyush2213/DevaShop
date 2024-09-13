package com.deva.Shop.service;
import com.deva.Shop.entity.Customer;
import com.deva.Shop.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public Customer getUserFromToken(String token) {
        Customer customer = customerRepository.findByToken(token);
        if (customer == null) {
            return null;
        }
        return customer;
    }

}
