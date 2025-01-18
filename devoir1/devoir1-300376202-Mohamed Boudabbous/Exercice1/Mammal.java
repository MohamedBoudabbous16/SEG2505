public class Mammal extends Animal{

    // Champ statique pour suivre le nombre total de mammifères
    static int numberOfMammals = 0;

    // Constructeur par défaut
    public Mammal() {
        super();
        numberOfMammals += 1;
        System.out.println("Mammal Constructor called");
    }

    // Constructeur paramétré
    public Mammal(String name) {
        super(name);
        numberOfMammals += 1;
        System.out.println("Mammal Constructor with name called");
    }

    // Implémentation de la méthode makeSound()
    @Override
    public void makeSound() {
        System.out.println("Mammal sound");
    }

    // Implémentation de la méthode getAnimalType()
    @Override
    public String getAnimalType() {
        return "Mammal";
    }

    // Méthode statique pour obtenir le nombre total de mammifères créés
    public static int getNumberOfMammals() {
        return numberOfMammals;
    }
}
