package by.bsuir.lab5.controller;
import jakarta.servlet.http.*;
import by.bsuir.lab5.exception.*;
public interface Command { String execute(HttpServletRequest request,HttpServletResponse response) throws ServiceException,IncorrectDataException; }
