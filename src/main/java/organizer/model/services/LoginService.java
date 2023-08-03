package organizer.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import organizer.model.entities.Account;
import organizer.model.OrganizerException;
import organizer.model.repositories.AccountRepository;

@Service
public class LoginService{

    @Autowired
    private AccountRepository accountRepository;

    public Account login(String loginId, String password) {

        if (StringUtils.isEmpty(loginId)) {
            throw new OrganizerException("Пожалуйста, введите имя пользователя");
        }

        if (StringUtils.isEmpty(password)) {
            throw new OrganizerException("Пожалуйста, введите пароль");
        }

        Account account = accountRepository.findById(loginId).
                orElseThrow(() -> new OrganizerException("Пожалуйста, проверьте Ваше имя пользователя."));

        if (!password.equals(account.getPassword())){
            throw new OrganizerException("Пожалуйста, проверьте Ваш пароль.");
        }

        return account;
    }


}
