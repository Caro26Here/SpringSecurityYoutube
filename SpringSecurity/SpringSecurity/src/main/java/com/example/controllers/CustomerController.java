package com.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("V1")
public class CustomerController {

    @GetMapping("/index")
    public String index(){
        return "Hello World!";
    }

    @GetMapping("/index2")
    public String index2(){
        return "Hello World NOT SECURED!";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){

        String sessionId = "";
        User userObject = null;

        // Had to create an instance (wouldn't show up by itself)
//        SessionRegistry sessionRegistry = new SessionRegistryImpl();

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                userObject = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> response = new HashMap<>();

        response.put("response", "Hello World");
        response.put("sessionId", sessionId);
        response.put("sessionUser", userObject.toString());

        return ResponseEntity.ok(response);
    }
}
