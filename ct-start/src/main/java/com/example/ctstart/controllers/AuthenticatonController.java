package com.example.ctstart.controllers;

import com.example.ctapi.dtos.Response.EmployeeDto;
import com.example.ctapi.dtos.Response.ResponseDto;
import com.example.ctapi.dtos.sercurity.JwtAuthenticationResponse;
import com.example.ctapi.dtos.sercurity.SigninRequest;
import com.example.ctapi.dtos.sercurity.UserDto;
import com.example.ctapi.dtos.sercurity.UserPrinciple;
import com.example.ctapi.services.IEmployeeService;
import com.example.ctapi.services.JWTService;
import com.example.ctcommon.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticatonController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final IEmployeeService employeeService;
//    @PostMapping("/signup")
//    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
//        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
//
//    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest, HttpServletRequest request){
        UserDto userDto = new UserDto();
        EmployeeDto employeeDto = employeeService.getEmployeeByEmail(signinRequest.getEmail(),signinRequest.getPassword());
        if(employeeDto == null)
            return ResponseEntity.ok(new ResponseDto(List.of("Can't get Data"), HttpStatus.INTERNAL_SERVER_ERROR.value(), "username or password error!"));


        userDto.setId(employeeDto.getId());
        userDto.setFullName(employeeDto.getFirstname()+" "+employeeDto.getLastname());
        userDto.setPhone(employeeDto.getPhone());
        userDto.setCompanyId("XXX");
        userDto.setAgencyId(employeeDto.getAgency().getId());
        userDto.setEmail(signinRequest.getEmail());
        userDto.setRole(employeeDto.getIsGlobalAdmin() == 1 ? Role.ADMIN : Role.USER);



        UserPrinciple userDetail = UserPrinciple.build(userDto);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetail, null, userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateToken(authentication);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);

        // set thêm thời gian hiệu lực của token

        return ResponseEntity.ok(new ResponseDto(List.of("Login successfull"), HttpStatus.OK.value(),
                jwtAuthenticationResponse));
    }
//    @PostMapping("/refresh")
//    public ResponseEntity<JwtAuthenticationResponse>refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
//        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
//
//    }
}
