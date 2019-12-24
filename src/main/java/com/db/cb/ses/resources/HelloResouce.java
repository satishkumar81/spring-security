package com.db.cb.ses.resources;

import com.db.cb.ses.models.AuthenticationRequest;
import com.db.cb.ses.models.AuthenticationResponse;
import com.db.cb.ses.service.MyUserDetailsService;
import com.db.cb.ses.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResouce {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @RequestMapping(value="/authenticate",method= RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest auth) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
        }catch(BadCredentialsException e){
            throw new Exception("Incorrect Credentials",e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(auth.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


}
