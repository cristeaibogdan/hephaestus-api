package org.personal.washingmachine.service;

import lombok.RequiredArgsConstructor;
import org.personal.shared.exception.CustomException;
import org.personal.shared.exception.ErrorCode;
import org.personal.washingmachine.entity.User;
import org.personal.washingmachine.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public void register(User user) {
		boolean existingEmail = userRepository.existsByEmail(user.getEmail());
		if (existingEmail) {
			throw new CustomException(ErrorCode.EMAIL_ALREADY_TAKEN);
		}

		boolean existingUsername = userRepository.existsByUsername(user.getUsername());
		if (existingUsername) {
			throw new CustomException(ErrorCode.USERNAME_ALREADY_TAKEN);
		}

		userRepository.save(user);
	}

	public User login(String username, String password) {
		return userRepository
				.findByUsernameAndPassword(username, password)
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));
	}
}
