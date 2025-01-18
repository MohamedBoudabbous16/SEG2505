public class Bird extends Animal{

    // Champ statique pour suivre le nombre total d'oiseaux
    static int numberOfBirds = 0;

    // Constructeur par défaut
    public Bird() {
        super();
        numberOfBirds += 1;
        System.out.println("Bird Constructor called");
    }

    // Constructeur paramétré
    public Bird(String name) {
        super(name);
        numberOfBirds += 1;
        System.out.println("Bird Constructor with name called");
    }

    // Implémentation de la méthode makeSound()
    @Override
    public void makeSound() {
        System.out.println("Bird sound");
    }

    // Implémentation de la méthode getAnimalType()
    @Override
    public String getAnimalType() {
        return "Bird";
    }

    // Méthode statique pour obtenir le nombre total d'oiseaux
    public static int getNumberOfBirds() {
        return numberOfBirds;
    }
}
