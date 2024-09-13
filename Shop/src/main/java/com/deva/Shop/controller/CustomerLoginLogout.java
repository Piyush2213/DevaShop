package com.deva.Shop.controller;

import com.deva.Shop.exception.CommonResponse;
import com.deva.Shop.repository.CustomerRepository;
import com.deva.Shop.request.CustomerLoginRequest;
import com.deva.Shop.response.CustomerLoginResponse;
import com.deva.Shop.response.ServiceResponse;
import com.deva.Shop.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"}, exposedHeaders = { "*" }, methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})

public class CustomerLoginLogout {
    @Autowired
    private CustomerService customerService;


    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<CustomerLoginResponse>> customerLogin(@RequestBody CustomerLoginRequest customerLoginRequest){

        ServiceResponse<CustomerLoginResponse> serviceResponse = customerService.customerLogin(customerLoginRequest);
        return ResponseEntity.status(serviceResponse.getStatus()).body(serviceResponse);

    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest req, HttpServletResponse res) {
        CommonResponse response = customerService.customerLogout(req,res);
        return ResponseEntity.status(response.getStatus()).body(response);
    }





}