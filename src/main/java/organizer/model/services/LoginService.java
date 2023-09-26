package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.entities.Account;
import organizer.model.OrganizerException;
import organizer.model.repositories.AccountRepository;

import static organizer.utils.Constants.*;

@Service
public class LoginService {

	@Autowired
	private AccountRepository accountRepository;

	public Account login(String loginId, String password) {

		if (StringUtils.isEmpty(loginId)) {
			throw new OrganizerException(ADD_LOGIN);
		}

		if (StringUtils.isEmpty(password)) {
			throw new OrganizerException(ADD_PASSWORD);
		}

		Account account = accountRepository.findById(loginId).
				orElseThrow(() -> new OrganizerException(CHECK_LOGIN));

		if (!password.equals(account.getPassword())) {
			throw new OrganizerException(CHECK_PASSWORD);
		}

		return account;
	}


}
