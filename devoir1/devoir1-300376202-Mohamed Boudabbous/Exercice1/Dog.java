public class Dog extends Mammal{

    // Constructeur par défaut
    public Dog() {
        super();
        System.out.println("Dog Constructor called");
    }

    // Constructeur paramétré
    public Dog(String name) {
        super(name);
        System.out.println("Dog Constructor with name called");
    }

    // Réimplémentation de la méthode makeSound()
    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }

    // Réimplémentation de la méthode getAnimalType()
    @Override
    public String getAnimalType() {
        return "Dog";
    }
}