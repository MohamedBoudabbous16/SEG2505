public abstract class Animal implements SoundsMaker {
    // Champ statique pour suivre le nombre total d’animaux
    static int numberOfAnimals = 0;

    // Champ non-statique pour le nom de l’animal
    protected String name;

    // Constructeur par défaut
    public Animal() {
        numberOfAnimals += 1;
        System.out.println("Animal Constructor called");
        this.name = "Unknown Animal";
    }

    // Constructeur paramétré
    public Animal(String name) {
        numberOfAnimals += 1;
        System.out.println("Animal Constructor with name called");
        this.name = name;
    }

    // Méthode abstraite pour obtenir le type d'animal
    public abstract String getAnimalType();

    // Méthode statique pour obtenir le nombre total d'animaux
    public static int getNumberOfAnimals() {
        return numberOfAnimals;
    }
}



