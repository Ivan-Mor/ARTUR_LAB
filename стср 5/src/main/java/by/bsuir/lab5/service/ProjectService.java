package by.bsuir.lab5.service;
import by.bsuir.lab5.exception.*;
import by.bsuir.lab5.model.*;
import by.bsuir.lab5.repository.*;
import by.bsuir.lab5.repository.specification.ProjectById;
import java.util.*;
public class ProjectService {
    private final ProjectRepository projects=new ProjectRepository(); private final DeveloperRepository developers=new DeveloperRepository();
    public List<Project> all() throws ServiceException {try{return projects.query(null);}catch(RepositoryException e){throw new ServiceException("Не удалось получить проекты",e);}}
    public Optional<Project> one(int id) throws ServiceException {try{return projects.query(new ProjectById(id)).stream().findFirst();}catch(RepositoryException e){throw new ServiceException("Не удалось получить проект",e);}}
    public List<Developer> developers() throws ServiceException {try{return developers.findAll();}catch(RepositoryException e){throw new ServiceException("Не удалось получить сотрудников",e);}}
    public boolean save(int id,String name,String description,int developerId) throws ServiceException,IncorrectDataException {
        if(id<0 || name==null || name.isBlank() || name.trim().length()>100 || description==null || description.isBlank() || description.trim().length()>500 || developerId<=0) throw new IncorrectDataException("Заполните название (до 100 символов), описание (до 500) и выберите сотрудника.");
        if(developers().stream().noneMatch(d->d.getId()==developerId)) throw new IncorrectDataException("Выбранный сотрудник не найден.");
        try{return projects.save(id,name.trim(),description.trim(),developerId);}catch(RepositoryException e){throw new ServiceException("Не удалось сохранить проект",e);}
    }
    public boolean delete(int id) throws ServiceException,IncorrectDataException {if(id<=0)throw new IncorrectDataException("Неверный номер проекта.");try{return projects.delete(id);}catch(RepositoryException e){throw new ServiceException("Не удалось удалить проект",e);}}
}
