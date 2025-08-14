package com.server.ecommerce.interfaces.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.server.ecommerce.service.user.info.UserInfo;
import com.server.ecommerce.service.user.UserService;
import com.server.ecommerce.support.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<UserInfo>> createUser(@RequestBody CreateUserRequest request) {
		return ApiResponse.CREATE(userService.join(request.toCommand()));
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<ApiResponse<UserInfo>> getUser(@PathVariable Long userId) {
		return ApiResponse.OK(userService.findUser(userId));
	}
}
