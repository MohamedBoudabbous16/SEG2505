public class Parrot extends Bird {


    // Constructeur par défaut
    public Parrot() {
        super();
        System.out.println("Parrot Constructor called");
    }

    // Constructeur paramétré
    public Parrot(String name) {
        super(name);
        System.out.println("Parrot Constructor with name called");
    }

    // Réimplémentation de la méthode makeSound()
    @Override
    public void makeSound() {
        System.out.println("Squawk!");
    }

    // Réimplémentation de la méthode getAnimalType()
    @Override
    public String getAnimalType() {
        return "Parrot";
    }
}
