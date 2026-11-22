package by.bsuir.lab5.model;
import java.io.Serializable;
public class Project implements Serializable {
    private final int id, developerId; private final String name, description, developerName;
    public Project(int id, String name, String description, int developerId, String developerName) { this.id=id; this.name=name; this.description=description; this.developerId=developerId; this.developerName=developerName; }
    public int getId(){return id;} public String getName(){return name;} public String getDescription(){return description;} public int getDeveloperId(){return developerId;} public String getDeveloperName(){return developerName;}
}
