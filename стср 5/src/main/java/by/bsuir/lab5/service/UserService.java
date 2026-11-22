package by.bsuir.lab5.service;
import by.bsuir.lab5.exception.*;
import by.bsuir.lab5.model.User;
import by.bsuir.lab5.repository.UserRepository;
import java.security.GeneralSecurityException;
import java.util.Optional;
public class UserService {
    private final UserRepository repo=new UserRepository();
    public Optional<User> login(String login,String password) throws ServiceException,IncorrectDataException {
        validate(login,password);
        try {Optional<User> user=repo.find(login.trim());return user.isPresent() && Passwords.verify(password,user.get().getPasswordHash())?user:Optional.empty();}
        catch(RepositoryException | GeneralSecurityException e){throw new ServiceException("Ошибка авторизации",e);}
    }
    public boolean register(String login,String password) throws ServiceException,IncorrectDataException {
        validate(login,password);
        try{return repo.add(login.trim(),Passwords.hash(password));}catch(RepositoryException | GeneralSecurityException e){throw new ServiceException("Ошибка регистрации",e);}
    }
    private void validate(String login,String password) throws IncorrectDataException {
        if(login==null || !login.trim().matches("[A-Za-z0-9_]{3,30}") || password==null || password.length()<8 || password.length()>128) throw new IncorrectDataException("Логин: 3–30 латинских букв, цифр или знаков подчёркивания; пароль: 8–128 символов.");
    }
}
