import java.util.ArrayList;
import java.util.Iterator;

public class AnimalTest {
    public static void main(String[] args){

        //: création de liste pour La classe parent Animal
        ArrayList<Animal> animalList = new ArrayList<>();

       //creatyion de listes pour chaque type d'animal
        ArrayList<Mammal> mammalList = new ArrayList<>();
        ArrayList<Bird> birdList = new ArrayList<>();
        ArrayList<Dog> dogList = new ArrayList<>();
        ArrayList<Parrot> parrotList = new ArrayList<>();
// Création de deux instances par défaut et deux paramétrées pour la classe Animal
        //on procéde ainsi parceque Animal est une classe abstraite, on ne peut donc pas l'instancier
        // pour faire: on crée des sous classe anonymes à chaque fois qu'on veut créer un objet de type Animal

        Animal animal1 = new Animal() {
            @Override
            public String getAnimalType() {
                return "Anonymous Animal";
            }

            @Override
            public void makeSound() {
                System.out.println("Unknown animal sound");
            }
        };
        Animal animal2 = new Animal() {
            @Override
            public String getAnimalType() {
                return "Anonymous Animal";
            }

            @Override
            public void makeSound() {
                System.out.println("Unknown animal sound");
            }
        };
        Animal animal3 = new Animal("Lion") {
            @Override
            public String getAnimalType() {
                return "Anonymous Animal";
            }

            @Override
            public void makeSound() {
                System.out.println("Named animal sound");
            }
        };
        Animal animal4 = new Animal("Tiger") {
            @Override
            public String getAnimalType() {
                return "Anonymous Animal";
            }

            @Override
            public void makeSound() {
                System.out.println("Named animal sound");
            }
        };
        animalList.add(animal1);
        animalList.add(animal2);
        animalList.add(animal3);
        animalList.add(animal4);
        //L'ajout de deux instances paramétré et deux instances non paramétré pour chaque liste

        // pour Mammal
        Mammal mammal1 = new Mammal();
        Mammal mammal2 = new Mammal();
        Mammal mammal3 = new Mammal("Elephant");
        Mammal mammal4 = new Mammal("Giraffe");
        mammalList.add(mammal1);
        mammalList.add(mammal2);
        mammalList.add(mammal3);
        mammalList.add(mammal4);

        // pour  Bird
        Bird bird1 = new Bird();
        Bird bird2 = new Bird();
        Bird bird3 = new Bird("Eagle");
        Bird bird4 = new Bird("Sparrow");
        birdList.add(bird1);
        birdList.add(bird2);
        birdList.add(bird3);
        birdList.add(bird4);

        // pour  Dog
        Dog dog1 = new Dog();
        Dog dog2 = new Dog();
        Dog dog3 = new Dog("Bulldog");
        Dog dog4 = new Dog("Labrador");
        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
        dogList.add(dog4);


        // pour  Parrot
        Parrot parrot1 = new Parrot();
        Parrot parrot2 = new Parrot();
        Parrot parrot3 = new Parrot("Macaw");
        Parrot parrot4 = new Parrot("Cockatoo");
        parrotList.add(parrot1);
        parrotList.add(parrot2);
        parrotList.add(parrot3);
        parrotList.add(parrot4);



        //pour respecter le polymorphisme on récupére touts les elements dans la liste
        //animalList



        //Afficher les sons des animaux:
        System.out.println("\nAnimals making sounds:");
        for (Animal animal : animalList) {
            animal.makeSound();
        }
        //Afficher le sons pour chaque type d'animaux
        //mammals
        System.out.println("\nMammals making sounds:");
        for (Mammal mammal : mammalList) {
            mammal.makeSound();
        }
        //birds
        System.out.println("\nBirds making sounds:");
        for (Bird bird : birdList) {
            bird.makeSound();
        }
        //Dogs
        System.out.println("\nDogs making sounds:");
        for (Dog dog : dogList) {
            dog.makeSound();
        }


        //Parrots
        System.out.println("\nParrots making sounds:");
        for (Parrot parrot : parrotList) {
            parrot.makeSound();
        }




        //Affichage du nombre d'instances qu'on a crée pour chaque
        //classe
        System.out.println("\nTotal number of animals: " + Animal.getNumberOfAnimals());
        System.out.println("Total number of mammals: " + Mammal.getNumberOfMammals());
        System.out.println("Total number of birds: " + Bird.getNumberOfBirds());

    }
}
