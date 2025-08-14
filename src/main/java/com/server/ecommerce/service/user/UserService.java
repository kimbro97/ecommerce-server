package com.server.ecommerce.service.user;

import static com.server.ecommerce.support.exception.BusinessError.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.server.ecommerce.domain.user.User;
import com.server.ecommerce.domain.user.UserRepository;
import com.server.ecommerce.service.user.command.UserJoinCommand;
import com.server.ecommerce.service.user.info.UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Transactional
	public UserInfo join(UserJoinCommand command) {

		userRepository.findByEmail(command.getEmail())
			.ifPresent( user -> { throw USER_JOIN_EMAIL_DUPLICATION.exception(); });

		String encodePassword = bCryptPasswordEncoder.encode(command.getPassword());

		User user = User.join(command.getEmail(), encodePassword, command.getName());
		user = userRepository.save(user);

		return UserInfo.from(user);

	}

	public UserInfo findUser(Long userId) {

		User user = userRepository.findByUserId(userId)
			.orElseThrow(USER_NOT_FOUND::exception);

		return UserInfo.from(user);
	}
}
