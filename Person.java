
import com.github.javafaker.Faker;
import java.util.Random;

/**
 * Representa una persona a partir de un nombre y una edad.
 * 
 * @author Álvaro Jiménez
 * @version date 24/05/2021
 */
public class Person {
    private String name;
    private int age;

    /**
     * Constructor parametrizado.
     * 
     * @param name nombre
     * @param age edad
     */
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    /**
     * Crea una persona random.
     * Utiliza la librería Faker para crear un nombre al azar.
     * 
     * @return Person
     */
    public static Person randomPersonGen(){
        Faker faker = new Faker();
        Random r = new Random();
        return new Person(faker.name().firstName(), r.nextInt(121));
    }

    /**
     * Obtiene el nombre.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la edad.
     * @return age
     */
    public int getAge() {
        return age;
    }
    
}
