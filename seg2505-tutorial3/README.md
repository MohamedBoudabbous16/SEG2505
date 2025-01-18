### Exigences implicites proposées

L'application doit vérifier les rôles pour garantir des accès sécurisés aux fonctionnalités. Elle doit également inclure une gestion des erreurs pour informer les utilisateurs des éventuelles anomalies lors de l’utilisation.

### Hypothèses

Les utilisateurs auront un rôle unique (Administrator, StoreKeeper, Assembler, Requester).
Les données d'identification seront uniques et sécurisées.

## Modélisation
voir les photos des diagrammes dans le fichier 5Diagrammes et livrable 3 et copie de APK)

        ### Diagramme de classes
        
        @startuml
        
        ' Interfaces
        interface Authenticable {
            + login()
            + logout()
        }
        
        interface Manageable {
            + create()
            + modify()
            + delete()
        }
        
        ' Classe abstraite User
        abstract class User {
            - String firstName
            - String lastName
            - String email
            - String password
            - LocalDateTime creationDateTime
            - LocalDateTime modificationDateTime
            - boolean isLoggedIn
            - int id
            + login(String email, String password): boolean
            + logout()
            + getFirstName(): String
            + getLastName(): String
            + getEmail(): String
            + getPassword(): String
            + getId(): int
            + isLoggedIn(): boolean
            + setFirstName(String)
            + setLastName(String)
            + setEmail(String)
            + setPassword(String)
            + setId(int)
        }
        
        User ..|> Authenticable
        
        ' Classes dérivées de User
        class Administrator {
            + createRequester(String firstName, String lastName, String email, String password)
            + modifyRequester(String newFirstName, String newLastName, String newEmail)
            + deleteRequester(String firstName, String lastName, String email)
        }
        
        class StoreKeeper {
            + addComponent(Component component)
            + removeComponent(Component component)
            + viewStock(): List<Component>
        }
        
        class Assembler {
            + acceptOrder(Order order)
            + rejectOrder(Order order, String message)
            + completeOrder(Order order)
            + viewAllOrders(): List<Order>
            + addOrder(Order order)
        }
        
        class Requester {
            - List<Order> orders 
            + createOrder(List<Component> components) 
            + viewOwnOrders(): List<Order> 
            + deleteOrder(int orderId) 
        }
        
        User <|-- Administrator
        User <|-- StoreKeeper
        User <|-- Assembler
        User <|-- Requester
        
        ' Updated Controllers
        class LoginController { 
            - AccessLocal accessLocal 
            - UserRepository userRepository 
            - UserInfo currentUser 
            + login(String email, String password): boolean 
            + logout() 
            + isUserLoggedIn(): boolean
            + getCurrentUser(): UserInfo 
        }
        
        class MainController { 
            - static MainController instance
            - Context appContext
            - LoginController loginController
            + getInstance(Context context): MainController
            + loginUser(String email, String password): boolean 
            + logoutUser() 
            + navigateToRoleActivity() 
        }
        
        class RequesterController { 
            - static RequesterController instance 
            - static Requester requester 
            - static AccessLocal accessLocal 
            + getInstance(Context, String, String, String, String): RequesterController 
            + createOrder(List<Component> components): boolean
            + deleteOrder(int orderId) 
            + viewOwnOrders(): List<Order> 
            + loginRequester()
            + logoutRequester() 
            + requestComponent(String title): List<Component> 
        }
        
        ' Classes de modèle
        class Component {
            - String type
            - String subtype
            - String title
            - int quantity
            - String comment
            - String creation_Date
            - String modification_date
            + getters et setters
        }
        
        class Order {
            - int id
            - OrderStatus status
            - String message
            - LocalDateTime creationDateTime
            - LocalDateTime modificationDateTime
            - Requester requester
            - List<Component> components
            + addComponent(Component component)
            + removeComponent(Component component)
            + updateStatus(String newStatus)
            + getId(): int
            + getStatus(): OrderStatus
            + getMessage(): String
            + setMessage(String)
            + getCreationDateTime(): LocalDateTime
            + getUserId(): int
        }
        
        class OrderStatus {
            - String status
            - LocalDateTime updatedAt
            + setStatus(String newStatus)
            + validateTransition(String newStatus): boolean
            + getStatus(): String
            + getUpdatedAt(): LocalDateTime
        }
        
        class UserInfo {
            - int id
            - String firstName
            - String lastName
            - String email
            - String password
            - String role
            - LocalDateTime createdAt
            - LocalDateTime modifiedAt
            + getters et setters
        }
        
        ' Repositories
        class UserRepository {
            - DatabaseSQLite dbHelper
            + insertUser(UserInfo user)
            + findUserByEmail(String email): UserInfo
            + updateUser(UserInfo user)
            + getAllUsers(): List<UserInfo>
        }
        
        class OrderRepository {
            - DatabaseSQLite dbHelper
            + insertOrder(Order order)
            + updateOrder(Order order)
            + deleteOrder(Order order)
            + getAllOrders(): List<Order>
        }
        
        class ComponentRepository {
            - List<Component> componentDatabase
            + updateComponent(Component component)
            + deleteComponent(Component component)
            + getAllComponents(): List<Component>
        }
        
        ' Classes de base de données
        class DatabaseSQLite {
            - String DATABASE_NAME
            - int DATABASE_VERSION
            + onCreate(SQLiteDatabase db)
            + onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
            + getBd(): SQLiteDatabase
        }
        
        class AccessLocal {
            - SQLiteDatabase bd
            - DatabaseSQLite databaseSQLite
            + addComponent(Component component): long
            + findComponentByTitle(String title): Component
            + getAllComponents(): List<Component>
            + upload(): ArrayList<String>
        }
        
        class AppDatabase {
            - UserRepository userRepository
            - OrderRepository orderRepository
            - ComponentRepository componentRepository
            - DatabaseSQLite dbHelper
            + getInstance(Context context): AppDatabase
            + getDbHelper(): DatabaseSQLite
            + getUserRepository(): UserRepository
            + getOrderRepository(): OrderRepository
            + getComponentRepository(): ComponentRepository
        }
        
        ' Updated Views
        class MainActivity {
            - ActivityMainBinding binding
            + onCreate(Bundle savedInstanceState)
            + authenticateUser(String email, String password)
            + handleUserRole(String role)
            + showErrorMessage()
        }
        
        class RequesterActivity {
            - Requester requester
            - TextView ordersListTextView
            + onCreate(Bundle savedInstanceState)
            + createOrder()
            + displayOrders()
            + deleteOrder()
        }
        
        ' Relations entre les classes avec multiplicités
        UserRepository --> "1" DatabaseSQLite
        OrderRepository --> "1" DatabaseSQLite
        ComponentRepository --> "1" AccessLocal
        AccessLocal --> "1" DatabaseSQLite
        
        AppDatabase --> "1" UserRepository
        AppDatabase --> "1" OrderRepository
        AppDatabase --> "1" ComponentRepository
        
        UserRepository --> "0..*" UserInfo
        OrderRepository --> "0..*" Order
        ComponentRepository --> "0..*" Component
        
        Order "1" --> "1" Requester
        Requester "1" --> "0..*" Order
        
        Order "1" *-- "1" OrderStatus
        Order "1" --> "0..*" Component
        Component "1" --> "0..*" Order
        
        MainController --> LoginController 
        LoginController --> UserRepository
        RequesterController --> "1" Requester
        RequesterActivity --> RequesterController
        
        @enduml



### Diagrammes d'utilisation (optionnel)

        @startuml
        package "CustomDesktopService" #DDDDDD {
            actor Administrator
            actor StoreKeeper
            actor Assembler
            actor Requester
        
            "Register a requester" as (registerRequester)
            "Unegister a requester" as (unregisterRequester)
        
            Administrator --> (registerRequester)
            Administrator --> (unregisterRequester)
        @enduml

### Diagrammes d'activités
1-pour la connexion d'un utilisateur:

        @startuml
        start
        :User opens the application;
        :User enters email and password;
        if (Are email and password valid?) then (Yes)
        	:Authenticate user via MainActivity;
        	if (User's role?) then
            	-> [Administrator]
            	:Initialize AdministratorController;
            	:Redirect to AdministratorActivity;
            	stop
            	-> [StoreKeeper]
            	:Initialize StoreKeeperController;
            	:Redirect to StoreKeeperActivity;
            	stop
            	-> [Assembler]
            	:Initialize AssemblerController;
            	:Redirect to AssemblerActivity;
            	stop
            	-> [Requester]
            	:Initialize RequesterController;
            	:Redirect to RequesterActivity;
            	stop
        	endif
        else (No)
        	:Display error message in MainActivity;
        endif
        stop
        @enduml
        2.
        -pour la création d'une commande par un Requester:
        
        
        @startuml
        start
        :Requester logs in via MainActivity;
        :RequesterController initializes;
        :Requester selects "Create Order";
        :Create a new Order object;
        repeat
        	:Select a Component;
        	:Add Component to Order;
        repeat while (More components to add?)
        :Submit Order via RequesterController;
        :Order saved in OrderRepository;
        stop
        @enduml

-pour le traitement d'une commande par un assembleur :
      
        @startuml
        start
        :Assembler logs in via MainActivity;
        :AssemblerController initializes;
        :Assembler views all orders using viewAllOrders();
        if (Orders available?) then (Yes)
        	:Select an Order;
        	:Examine Order details;
        	if (Accept Order?) then (Yes)
            	:Call acceptOrder(Order) in AssemblerController;
            	:Update Order status to "Accepted" in OrderRepository;
            	:Process the Order;
            	:Call completeOrder(Order) in AssemblerController;
            	:Update Order status to "Completed";
        	else (No)
            	:Call rejectOrder(Order, message) in AssemblerController;
            	:Update Order status to "Rejected";
            	:Provide rejection message;
        	endif
        else (No)
        	:No orders to process;
        endif
        stop
     
        @enduml
-Diagramme d'activité pour la gestion des composants par le Storekeeper:

        @startuml
        start
        :StoreKeeper logs in via MainActivity;
        :StoreKeeperController initializes;
        repeat
        	:Choose an action in StoreKeeperActivity;
        	if (Action == "Add Component") then
            	:Enter Component details;
            	:Call addComponent(...) in StoreKeeperController;
            	:Component added to AccessLocal (database);
        	elseif (Action == "Remove Component") then
            	:Select Component to remove;
            	:Call removeComponent(Component) in StoreKeeperController;
            	:Component removed from AccessLocal;
        	elseif (Action == "View Stock") then
            	:Call upload() in StoreKeeperController;
            	:Display list of Components;
        	endif
        repeat while (More actions?)
        stop
        @enduml



-Diagramme d'activité pour la création d'un demande par l'administrateur :
       
        @startuml
        start
        :Administrator logs in via MainActivity;
        :AdministratorController initializes;
        :Administrator selects "Create Requester";
        :Enter Requester details;
        if (Details are valid?) then (Yes)
        	:Call createRequester(...) in AdministratorController;
        	:Requester created in UserRepository;
        	:Notify success in AdministratorActivity;
        else (No)
        	:Display error messages;
        endif
        stop
        @enduml



#### Accueil et authentification

1. Séquence de Connexion Utilisateur:

        @startuml
        actor User
        
        User -> MainActivity : opens app
        User -> MainActivity : enters email and password
        MainActivity -> UserRepository : findUserByEmail(email)
        UserRepository --> MainActivity : UserInfo or null
        MainActivity -> User : invalid credentials?
        alt UserInfo exists and password matches
        	MainActivity -> UserController : initialize with UserInfo
        	UserController -> User : login()
        	UserController -> MainActivity : getUserRole()
        	MainActivity -> User : redirect to role-specific activity
        else
        	MainActivity -> User : display error message
        end
        @enduml



#### Gestion des utilisateurs

        @startuml
        actor Administrator
        
        :Administrator logs in;
        :Accesses the user management page;
        :Can create, modify, or delete users (Requesters only);
        @enduml
        
        #### Gestion du stock
        
        @@startuml
        actor StoreKeeper
        
        :StoreKeeper logs in;
        :Accesses the inventory management interface;
        :Can add, remove, or view components;
        @enduml
        l


#### Passage d'une commande
        @startuml
        actor Requester
        
        Requester -> MainActivity : logs in
        MainActivity -> RequesterController : getInstance()
        RequesterController -> Requester : loginRequester()
        Requester -> RequesterActivity : selects "Create Order"
        RequesterActivity -> RequesterController : createOrder(orderDetails)
        RequesterController -> Order : new Order(orderDetails)
        Order -> OrderRepository : save Order
        OrderRepository -> DatabaseSQLite : insertOrder(order)
        DatabaseSQLite --> OrderRepository : success/failure
        OrderRepository --> RequesterController : confirmation
        RequesterController --> RequesterActivity : order created
        RequesterActivity -> Requester : display success message
        @enduml





#### Traitement d'une commande

        @startuml
        actor Assembler
        
        :Assembler logs in;
        :Accesses the list of pending orders;
        :Approves or rejects an order;
        @enduml


### Diagrammes de séquences
1. Séquence de Connexion Utilisateur:

        @startuml
        actor User
        
        User -> MainActivity : opens app
        User -> MainActivity : enters email and password
        MainActivity -> UserRepository : findUserByEmail(email)
        UserRepository --> MainActivity : UserInfo or null
        MainActivity -> User : invalid credentials?
        alt UserInfo exists and password matches
        	MainActivity -> UserController : initialize with UserInfo
        	UserController -> User : login()
        	UserController -> MainActivity : getUserRole()
        	MainActivity -> User : redirect to role-specific activity
        else
        	MainActivity -> User : display error message
        end
        @enduml


3. Création d'une Commande par un Demandeur

        @startuml
        actor Requester
        
        Requester -> MainActivity : logs in
        MainActivity -> RequesterController : getInstance()
        RequesterController -> Requester : loginRequester()
        Requester -> RequesterActivity : selects "Create Order"
        RequesterActivity -> RequesterController : createOrder(orderDetails)
        RequesterController -> Order : new Order(orderDetails)
        Order -> OrderRepository : save Order
        OrderRepository -> DatabaseSQLite : insertOrder(order)
        DatabaseSQLite --> OrderRepository : success/failure
        OrderRepository --> RequesterController : confirmation
        RequesterController --> RequesterActivity : order created
        RequesterActivity -> Requester : display success message
        @enduml


3. Traitement d'une Commande par un Assembleur

        @startuml
        actor Assembler
        
        Assembler -> MainActivity : logs in
        MainActivity -> AssemblerController : getInstance()
        AssemblerController -> Assembler : loginAssembler()
        Assembler -> AssemblerActivity : views all orders
        AssemblerActivity -> AssemblerController : viewAllOrders()
        AssemblerController -> OrderRepository : getAllOrders()
        OrderRepository --> AssemblerController : List<Order>
        AssemblerController --> AssemblerActivity : List<Order>
        AssemblerActivity -> Assembler : displays orders
        Assembler -> AssemblerActivity : selects an order
        AssemblerActivity -> AssemblerController : acceptOrder(order)
        AssemblerController -> Order : updateStatus("Accepted")
        Order -> OrderRepository : updateOrder(order)
        OrderRepository -> DatabaseSQLite : updateOrder(order)
        DatabaseSQLite --> OrderRepository : success/failure
        OrderRepository --> AssemblerController : confirmation
        AssemblerController --> AssemblerActivity : order accepted
        AssemblerActivity -> Assembler : display success message
        @enduml


4. Création d'un Demandeur par l'Administrateur

        @startuml
        actor Administrator
        
        Administrator -> MainActivity : logs in
        MainActivity -> AdministratorController : getInstance()
        AdministratorController -> Administrator : loginAdministrator()
        Administrator -> AdministratorActivity : selects "Create Requester"
        AdministratorActivity -> AdministratorController : createRequester(firstName, lastName, email, password)
        AdministratorController -> UserRepository : insertUser(new UserInfo)
        UserRepository -> DatabaseSQLite : insert into users table
        DatabaseSQLite --> UserRepository : success/failure
        UserRepository --> AdministratorController : confirmation
        AdministratorController --> AdministratorActivity : requester created
        AdministratorActivity -> Administrator : display success message
        @enduml


5. Ajout d'un Composant par le Magasinier (storekeeper)

        @startuml
        actor StoreKeeper
        
        StoreKeeper -> MainActivity : logs in
        MainActivity -> StoreKeeperController : getInstance()
        StoreKeeperController -> StoreKeeper : login()
        StoreKeeper -> StoreKeeperActivity : selects "Add Component"
        StoreKeeperActivity -> StoreKeeperController : addComponent(title, type, subtype, quantity, comment)
        StoreKeeperController -> AccessLocal : findComponentByTitle(title)
        AccessLocal --> StoreKeeperController : null (if not exists)
        StoreKeeperController -> Component : new Component(...)
        StoreKeeperController -> AccessLocal : addComponent(component)
        AccessLocal -> DatabaseSQLite : insertComponent(component)
        DatabaseSQLite --> AccessLocal : success/failure
        AccessLocal --> StoreKeeperController : confirmation
        StoreKeeperController --> StoreKeeperActivity : component added
        StoreKeeperActivity -> StoreKeeper : display success message
        @enduml


#### Pour l'accueil et l'authentification

           @startuml
            actor Inconnu
        
            control MainActivity
            control AdministratorActivity
            control StoreKeeperActivity
            control AssemblerActivity
            control RequesterActivity
        
            Inconnu --> MainActivity : Demande d'authentification\n(avec identifiant et mot de passe)
        
            MainActivity <--> Database : Rechercher un utilisateur\navec un identifiant et un mot de passe
        
            alt L'utilisateur existe
                MainActivity <--> Database : Obtenir des informations sur l'utilisateur\n(dont son rôle) 
                
                alt Le rôle de l'utilisateur est Administror
                    MainActivity --> AdministratorActivity
                alt Le rôle de l'utilisateur est StoreKeeper
                    MainActivity --> StoreKeeperActivity
                alt Le rôle de l'utilisateur est Assembler
                    MainActivity --> AssemblerActivity
                alt Le rôle de l'utilisateur est Requester
                    MainActivity --> RequesterActivity
                else Rôle inconnu 
                    MainActivity --> Inconnu : Afficher une erreur de conception
                end
            else Sinon
                MainActivity --> Inconnu : Afficher une erreur d'authentification
            end
        
            database Database
        @enduml

#### Pour le rôle Administrator

        @startuml
            actor Administrator
        
            control AdministratorActivity
            
            database Database
        
            Administrator --> AdministratorActivity : Créer un utilisateur
        
            AdministratorActivity <--> Database : Obtenir la liste des utilisateurs
        
            alt L'utilisateur existe déjà
                AdministratorActivity --> Database : Ajouter une ligne à la table Users
            else Sinon
                AdministratorActivity --> Administrator: Afficher une erreur
            end
        @enduml

#### Pour le rôle StoreKeeper

        @startuml
            actor StoreKeeper
        
            control StoreKeeperActivity
        
            database Database
        
            StoreKeeper --> StoreKeeperActivity : Access inventory management
        
            StoreKeeperActivity <--> Database : Retrieve current inventory
        
            alt StoreKeeper wants to add a component
                StoreKeeperActivity --> Database : Add new component to inventory
                Database --> StoreKeeperActivity : Confirm addition
            else StoreKeeper wants to remove a component
                StoreKeeperActivity --> Database : Remove component from inventory
                alt Component exists
                    Database --> StoreKeeperActivity : Confirm removal
                else Component does not exist
                    StoreKeeperActivity --> StoreKeeper : Display error (component not found)
                end
            else StoreKeeper wants to view components
                StoreKeeperActivity --> Database : Fetch and display list of components
            end
        @enduml


#### Pour le rôle Assembler

        @startuml
            actor Assembler
        
            control AssemblerActivity
            
            database Database
        
            Assembler --> AssemblerActivity : Access pending orders
        
            AssemblerActivity <--> Database : Retrieve list of pending orders
        
            alt Assembler accepts an order
                AssemblerActivity --> Database : Update order status to "Accepted"
                Database --> AssemblerActivity : Confirm status update
                AssemblerActivity --> Assembler : Display confirmation (Order Accepted)
            else Assembler rejects an order
                AssemblerActivity --> Database : Update order status to "Rejected"
                AssemblerActivity --> Assembler : Enter rejection reason
                Database --> AssemblerActivity : Confirm status update
                AssemblerActivity --> Assembler : Display confirmation (Order Rejected)
            else Assembler marks an order as complete
                AssemblerActivity --> Database : Update order status to "Completed"
                Database --> AssemblerActivity : Confirm status update
                AssemblerActivity --> Assembler : Display confirmation (Order Completed)
            end
        @enduml


#### Pour le rôle Requester

        @startuml
        state "État de connexion" as LoginState {
            [*] --> NonConnecté : Démarrage
            NonConnecté --> Connexion : Entrer identifiants
            Connexion --> Connecté : Authentification réussie
            Connexion --> NonConnecté : Authentification échouée
            Connecté --> NonConnecté : Déconnexion
        }
        
        state "Gestion de commandes" as OrderManagement {
            [*] --> ListeCommandes : Accéder aux commandes
            ListeCommandes --> CréationCommande : Sélectionner "Créer commande"
            CréationCommande --> CommandeEnregistrée : Ajouter des composants
            CommandeEnregistrée --> ListeCommandes : Enregistrer commande
            ListeCommandes --> ConsultationCommande : Sélectionner une commande
            ConsultationCommande --> ModificationCommande : Modifier les détails de la commande
            ModificationCommande --> CommandeEnregistrée : Enregistrer les modifications
            ListeCommandes --> SuppressionCommande : Sélectionner une commande à supprimer
            SuppressionCommande --> ListeCommandes : Confirmer suppression
        }
        
        state "État de l'application" as AppState {
            [*] --> Accueil : Démarrage de l'application
            Accueil --> LoginState : Authentification
            LoginState --> OrderManagement : Connexion réussie
            OrderManagement --> LoginState : Déconnexion
        }
        
        [*] --> AppState : Lancement de l'application
        AppState --> [*] : Fermeture de l'application
        @enduml



## Eléments de conception
        @startuml
        package "Architecture MVC" {
            class Controller <<control>> {
                + loginUser()
                + logoutUser()
                + manageInventory()
                + processOrders()
            }
        
            class Model <<entity>> {
                + User
                + Order
                + Component
            }
        
            class View <<boundary>> {
                + MainActivity
                + AdministratorActivity
                + StoreKeeperActivity
                + AssemblerActivity
                + RequesterActivity
            }
        
            Controller --> Model : Manipulation des données
            Controller --> View : Contrôle de l'interface utilisateur
        }
        @enduml


## Eléments d'implémentation

        @startuml
        package "Composants principaux" {
            class User <<entity>> {
                - email : String
                - password : String
                - role : String
                + login()
                + logout()
            }
        
            class Order <<entity>> {
                - orderId : int
                - status : String
                + createOrder()
                + updateOrderStatus()
            }
        
            class Component <<entity>> {
                - title : String
                - type : String
                + addComponent()
                + removeComponent()
            }
        }
        @enduml

## Eléments de tests unitaires

pour le moment il n'ya pas de test unitaires

## Comment reconstruire la solution
        @startuml
        participant "Utilisateur" as User
        participant "GitHub" as GitHub
        participant "Android Studio" as IDE
        
        User -> GitHub : Cloner le dépôt
        User -> IDE : Ouvrir le projet dans Android Studio
        IDE -> User : Exécuter la configuration de build
        @enduml


## Comment installer et utiliser la solution

        @startuml
        participant "Utilisateur" as User
        participant "Emulateur" as Emulator
        
        User -> IDE : Compiler et exécuter le projet
        IDE -> Emulator : Lancer l'application
        Emulator -> User : Tester l'application
        @enduml
        
        ## Eléments de démonstration
        @startuml
        actor Requester
        actor StoreKeeper
        actor Assembler
        
        package "Scénario suggéré" {
            Requester -> RequesterActivity : Créer une commande
            StoreKeeper -> StoreKeeperActivity : Ajouter un composant
            Assembler -> AssemblerActivity : Traiter une commande
        }
        @enduml

### Scénario ("storyboard") suggéré

<à compléter (optionnel)>

### Valeurs de test

#### Utilisateurs

| Rôle           | Identifiant de connexion | Mot de passe |
|----------------|--------------------------|--------------|
| Administrateur |admin@example.com         | admin123     |
| StoreKeeper    |storekeeper@example.com>  | store123r    |
| Assembler      |  assembler@example.com   |assembler123  |

#### Fichier de données exemple

<com.example.pcorderapplication.view.MainActivity

## Limites et problèmes connus

        je n'arrive pas à voir la liste des composant que le rquester a demandé. La logique de l'authentification a été implémenté mais je n'arrvive pas pour le moment à créer un nouvel utilisateur
 
## Information destinées aux correcteurs

        ce projet a été concu sur Android Studio Ladybug | 2024.2.1 Canary 8  compiler l'application sur un e version antérieure peut causer des problémes des erreurs de compatibilité. La version du gradle utilisée est le 8.9
