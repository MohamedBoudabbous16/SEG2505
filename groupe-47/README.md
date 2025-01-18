# uOttawa - 2024-2025 - SEG2505A - Projet - Groupe 47

Nom du projet : PCOrderApplication
depot Github: https://github.com/uOttawa-2024-2025-seg2505-projet/groupe-47

## Membres du projet

| Prénom      | NOM         | Identifiant GitHub |
|-------------|-------------|--------------------|
|Mohamed     |Boudabbous |MohamedBoudabbous |

## Introduction

PCOrderApplication est une solution de gestion de commandes conçue pour optimiser la gestion des stocks et la communication entre différents rôles dans une entreprise technologique. Cette application centralise la gestion de l'inventaire, les commandes, et la création d'utilisateurs, tout en assurant une sécurité et une accessibilité adaptées aux besoins des divers utilisateurs.

## Clarifications sur les exigences
Cette application doit permettre aux utilisateurs de gérer l'inventaire, de créer des commandes, et de gérer les rôles. Les composants doivent être ajoutés, modifiés ou supprimés par des utilisateurs spécifiques (ex. : StoreKeeper).

### Exigences implicites proposées

    L'application doit :
    - **Vérifier les rôles** : Assurer que chaque utilisateur accède uniquement aux fonctionnalités autorisées en fonction de son rôle (Administrateur, Magasinier, Assembleur, ou Demandeur).
    - **Gérer les erreurs** : Inclure des mécanismes robustes pour détecter et afficher des messages d'erreur clairs et pertinents aux utilisateurs en cas d'anomalies ou de problèmes (exemple : authentification échouée, données invalides, ou actions interdites).
    - **Respecter la sécurité des données** : Protéger les données utilisateur et éviter tout accès non autorisé grâce à l'authentification Firebase et à des vérifications côté serveur lorsque nécessaire.

### Hypothèses

    - Chaque utilisateur possèdera **un rôle unique** parmi les suivants : `Administrator`, `StoreKeeper`, `Assembler`, ou `Requester`.
    - Les **données d'identification** (telles que l'email et le mot de passe) seront **uniques**, sécurisées, et gérées via Firebase pour garantir la confidentialité et la protection des informations.
    - Les rôles d'utilisateur détermineront les **permissions et fonctionnalités accessibles** dans l'application.
    - Toutes les actions effectuées par un utilisateur (création, modification, suppression) seront **loguées** pour garantir la traçabilité.
    - Les composants ajoutés ou modifiés par les utilisateurs seront associés à une **date de création** et une **date de modification**, gérées automatiquement par le système.
    - Les **statuts des commandes** (`Pending`, `Accepted`, `Rejected`, `Completed`) suivront un flux de transition validé pour éviter les incohérences.
    - Les données stockées dans Firebase (utilisateurs, commandes, composants) seront synchronisées avec l'application pour offrir une **expérience en temps réel**.
    - Les erreurs système ou de validation d'entrée seront gérées avec des **messages clairs et utiles** pour guider les utilisateurs.
    - Un utilisateur ne pourra accéder qu’aux **données qui lui sont pertinentes** (par exemple, un Requester ne peut voir que ses propres commandes).
    - L'interface utilisateur sera **adaptative** en fonction du rôle de l'utilisateur pour masquer les fonctionnalités non autorisées.
    - Les commandes ne pourront être créées que si elles contiennent les composants minimaux requis (par exemple, CPU, RAM, stockage pour une configuration PC).


## Modélisation
voir les photos des diagrammes dans le dossier Documentation)
Diagramme de classe
            ```plantuml
        @startuml
        ' Définir la direction du diagramme
        left to right direction

        ' Ajuster les paramètres pour une meilleure lisibilité
        skinparam classAttributeIconSize 0
        skinparam packageStyle rectangle
        skinparam defaultFontSize 11
        skinparam wrapWidth 200
        skinparam maxMessageSize 100
        skinparam linetype ortho

        ' Augmenter la résolution pour une image plus claire
        skinparam dpi 150

        ' Définir les packages et leurs couleurs
        package "com.example.pcorderapplication.model" <<Rectangle>> #DDDDDD {
            abstract class User {
                - id: int
                - firstName: String
                - lastName: String
                - email: String
                - password: String
                - creationDateTime: LocalDateTime
                - modificationDateTime: LocalDateTime
                - isLoggedIn: boolean
                + login(email: String, password: String): boolean
                + logout(): void
            }

            class Administrator {
                + createRequester(firstName: String, lastName: String, email: String, password: String): void
                + modifyRequester(email: String, newFirstName: String, newLastName: String): void
                + deleteRequester(email: String): void
                + getAllRequesters(): List<Requester>
            }

            class Assembler {
                - orders: List<Order>
                + addOrder(order: Order): void
                + acceptOrder(order: Order): void
                + rejectOrder(order: Order, message: String): void
                + completeOrder(order: Order): void
                + viewAllOrders(): List<Order>
            }

            class StoreKeeper {
                - stock: List<Component>
                + addComponent(component: Component): void
                + removeComponent(component: Component): void
                + modifyComponent(component: Component): void
                + viewStock(): List<Component>
            }

            class Requester {
                - orders: List<Order>
                + createOrder(components: List<Component>): void
                + viewOwnOrders(): List<Order>
                + deleteOrder(orderId: int): void
            }

            class Order {
                - id: int
                - components: List<Component>
                - status: OrderStatus
                - message: String
                + updateStatus(status: String): void
                + addComponent(component: Component): void
                + setMessage(message: String): void
            }

            class Component {
                - title: String
                - type: String
                - subtype: String
                - quantity: int
                - comment: String
                - creationDateTime: LocalDateTime
                - modificationDateTime: LocalDateTime
                + getDetails(): String
            }

            class OrderStatus {
                - status: String
                - updatedAt: LocalDateTime
                + setStatus(newStatus: String): void
                + getStatus(): String
            }
        }

        package "com.example.pcorderapplication.controller" <<Rectangle>> #EEEFFF {
            class AdministratorController {
                + createRequester(firstName: String, lastName: String, email: String, password: String): void
                + modifyRequester(email: String, newFirstName: String, newLastName: String): void
                + deleteRequester(email: String): void
                + getAllRequesters(): List<Requester>
            }

            class AssemblerController {
                + addOrder(order: Order): void
                + completeOrder(order: Order): void
                + rejectOrder(order: Order, message: String): void
                + viewAllOrders(): List<Order>
            }

            class StoreKeeperController {
                + addComponent(title: String, type: String, subtype: String, quantity: int, comment: String, creationDateTime: LocalDateTime): void
                + modifyComponent(component: Component): void
                + deleteComponent(title: String): void
                + viewStock(): List<Component>
            }

            class RequesterController {
                + createOrder(components: List<Component>): boolean
                + deleteOrder(orderId: int): void
                + viewOwnOrders(): List<Order>
            }
        }

        package "com.example.pcorderapplication.view" <<Rectangle>> #FFFFFF {
            class MainActivity {
                + authenticateUser(email: String, password: String): void
                + navigateToActivity(role: String): void
                + showErrorMessage(): void
            }

            class AdministratorActivity {
                + createRequester(): void
                + modifyRequester(): void
                + deleteRequester(): void
                + viewAllRequesters(): void
            }

            class AssemblerActivity {
                + addOrder(): void
                + updateOrder(): void
                + deleteOrder(): void
                + completeOrder(): void
            }

            class StoreKeeperActivity {
                + addComponent(): void
                + modifyComponent(): void
                + deleteComponent(): void
                + viewStock(): void
            }

            class RequesterActivity {
                + createOrder(): void
                + deleteOrder(): void
                + loadOrders(): void
            }
        }

        ' Définir les relations et les multiplicités
        User <|-- Administrator
        User <|-- Assembler
        User <|-- StoreKeeper
        User <|-- Requester

        Order "1" o-- "1..*" Component : contains
        Order "1" --> "1" OrderStatus : has

        Administrator "1" --> "0..*" Requester : manages
        Requester "1" --> "0..*" Order : places
        Assembler "1" --> "0..*" Order : processes
        StoreKeeper "1" --> "0..*" Component : manages

        AdministratorController "1" --> "1" Administrator
        AssemblerController "1" --> "1" Assembler
        StoreKeeperController "1" --> "1" StoreKeeper
        RequesterController "1" --> "1" Requester

        MainActivity "1" --> "1" AdministratorActivity : navigates
        MainActivity "1" --> "1" AssemblerActivity : navigates
        MainActivity "1" --> "1" StoreKeeperActivity : navigates
        MainActivity "1" --> "1" RequesterActivity : navigates

        ' Ajouter des notes explicatives
        note right of User
          Classe de base abstraite pour les utilisateurs.
          Contient les informations de connexion.
        end note

        note bottom of Order
          Représente une commande passée par un Requester.
          Gérée par un Assembler et suit un statut.
        end note

        note bottom of Component
          Représente un élément matériel ou logiciel dans la base de données.
          Géré par le StoreKeeper.
        end note

        note bottom of MainActivity
          Point d'entrée pour l'application.
          Redirige l'utilisateur en fonction de son rôle.
        end note

        @enduml
        ```plantuml
        Lien: https://urlz.fr/th8Q
### Diagrammes d'utilisation (optionnel)
        ```plantuml
        @startuml
        skinparam actorStyle "awesome"

        ' Définition des acteurs
        actor Administrator as Admin
        actor Assembler as Assembleur
        actor Requester as Demandeur
        actor StoreKeeper as GestionnaireStock
        actor System as Système

        ' Cas d'utilisation pour l'Administrator
        usecase "Créer un requester" as UC_CreateRequester
        usecase "Modifier un requester" as UC_ModifyRequester
        usecase "Supprimer un requester" as UC_DeleteRequester
        usecase "Afficher les requesters" as UC_ViewRequesters

        ' Cas d'utilisation pour l'Assembler
        usecase "Ajouter une commande" as UC_AddOrder
        usecase "Accepter une commande" as UC_AcceptOrder
        usecase "Rejeter une commande" as UC_RejectOrder
        usecase "Compléter une commande" as UC_CompleteOrder
        usecase "Afficher toutes les commandes" as UC_ViewAllOrders

        ' Cas d'utilisation pour le Requester
        usecase "Créer une commande" as UC_CreateOrder
        usecase "Afficher ses commandes" as UC_ViewOwnOrders
        usecase "Supprimer une commande" as UC_DeleteOrder

        ' Cas d'utilisation pour le StoreKeeper
        usecase "Ajouter un composant" as UC_AddComponent
        usecase "Modifier un composant" as UC_ModifyComponent
        usecase "Supprimer un composant" as UC_DeleteComponent
        usecase "Afficher le stock" as UC_ViewStock

        ' Relations entre les acteurs et les cas d'utilisation
        Admin --> UC_CreateRequester
        Admin --> UC_ModifyRequester
        Admin --> UC_DeleteRequester
        Admin --> UC_ViewRequesters

        Assembleur --> UC_AddOrder
        Assembleur --> UC_AcceptOrder
        Assembleur --> UC_RejectOrder
        Assembleur --> UC_CompleteOrder
        Assembleur --> UC_ViewAllOrders

        Demandeur --> UC_CreateOrder
        Demandeur --> UC_ViewOwnOrders
        Demandeur --> UC_DeleteOrder

        GestionnaireStock --> UC_AddComponent
        GestionnaireStock --> UC_ModifyComponent
        GestionnaireStock --> UC_DeleteComponent
        GestionnaireStock --> UC_ViewStock

        ' Système global
        Système --> UC_ViewRequesters
        Système --> UC_ViewAllOrders
        Système --> UC_ViewStock

        ' Notes explicatives pour clarifier les acteurs
        note right of Admin
          L'administrateur gère les utilisateurs
          de type Requester.
        end note

        note right of Assembleur
          L'assembleur traite les commandes
          placées par les Requesters.
        end note

        note right of Demandeur
          Le Requester peut placer, visualiser,
          et supprimer ses propres commandes.
        end note

        note right of GestionnaireStock
          Le StoreKeeper gère les composants
          dans le stock.
        end note
        @enduml
        ```plantuml
        lien: https://urlz.fr/th9b


### Diagrammes d'activités
1-pour la connexion d'un utilisateur:
        ```plantuml
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
            ```plantuml
            Lien:https://urlz.fr/th9c
2.-pour la création d'une commande par un Requester:
        
        
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
Lien:https://urlz.fr/th9d

-pour le traitement d'une commande par un assembleur :
          ```plantuml
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
            ```plantuml
            Lien: https://urlz.fr/th9e
            
-Diagramme d'activité pour la gestion des composants par le Storekeeper:
            ```plantuml
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
            ```plantuml
            Lien: https://urlz.fr/th9f


-Diagramme d'activité pour la création d'un demande par l'administrateur :
           ```plantuml
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
            ```plantuml
        Lien: https://urlz.fr/th9g


#### Accueil et authentification

1. Séquence de Connexion Utilisateur:
            ```plantuml
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
            ```plantuml
        Lien: https://urlz.fr/th9h


#### Gestion des utilisateurs
        ```plantuml
        @startuml
        actor Administrator
        Administrator -> (Logs in) : Starts session
        (Logs in) -> (Accesses the user management page) : Successful login
        (Accesses the user management page) -> (Manage users: create, modify, delete Requesters only) : User management
        @enduml
        ```plantuml
        Lien: https://urlz.fr/th9i

        
#### Gestion du stock

        ```plantuml
        @startuml
        actor StoreKeeper
        StoreKeeper -> (Logs in)
        (Logs in) -> (Accesses the inventory management interface)
        (Accesses the inventory management interface) -> (Can add, remove, or view components)
        @enduml
        ```plantuml
        Lien:https://urlz.fr/th9j

#### Passage d'une commande
        ```plantuml
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
        ```plantuml
        Lien: https://urlz.fr/th9k
#### Traitement d'une commande

            ```plantuml
            @startuml
            actor Assembler
            Assembler -> (Logs in)
            (Logs in) -> (Accesses the list of pending orders)
            (Accesses the list of pending orders) -> (Approves or rejects an order)
            @enduml

            ```plantuml
            Lien:https://urlz.fr/th9l

### Diagrammes de séquences
1. Séquence de Connexion Utilisateur:
            ```plantuml
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
            ```plantuml
            Lien: https://urlz.fr/th9m


3. Création d'une Commande par un Requester
            ```plantuml
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
            ```plantuml
        lien:https://urlz.fr/th9a

3. Traitement d'une Commande par un Assembleur
            ```plantuml
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
            ```plantuml
        Lien:https://urlz.fr/th99


4. Création d'un Demandeur par l'Administrateur
            ```plantuml
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
            ```plantuml
        Lien:https://urlz.fr/th97

5. Ajout d'un Composant par le storekeeper
            ```plantuml
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
            ```plantuml
        Lien:https://urlz.fr/th93


#### Pour l'accueil et l'authentification
                ```plantuml
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
                ```plantuml
                Lien:https://urlz.fr/th92
                
#### Pour le rôle Administrator
            ```plantuml
        @startuml
        actor Administrator

        control AdministratorActivity

        database Database

        Administrator --> AdministratorActivity : Request to create a user

        AdministratorActivity --> Database : Check if the user already exists
        alt User already exists
            AdministratorActivity --> Administrator : Display error "User already exists"
        else User does not exist
            AdministratorActivity --> Database : Add a new row to the Users table
            Database --> AdministratorActivity : Confirm user creation
            AdministratorActivity --> Administrator : Display message "User successfully created"
        end

        Administrator --> AdministratorActivity : Request the list of users
        AdministratorActivity --> Database : Retrieve the list of users
        Database --> AdministratorActivity : Return the list of users
        AdministratorActivity --> Administrator : Display the list of users

        @enduml
            ```plantuml
            Lien:https://urlz.fr/th91
            
#### Pour le rôle StoreKeeper
            ```plantuml
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
            ```plantuml
            Lien: https://urlz.fr/th90


#### Pour le rôle Assembler

            ```plantuml
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
        ```plantuml
        Lien:https://urlz.fr/th8W

#### Pour le rôle Requester

    ```plantuml
    @startuml
    state "Login State" as LoginState {
        [*] --> NotConnected : Start
        NotConnected --> Connecting : Enter credentials
        Connecting --> Connected : Authentication successful
        Connecting --> NotConnected : Authentication failed
        Connected --> NotConnected : Logout
    }

    state "Order Management" as OrderManagement {
        [*] --> OrderList : Access orders
        OrderList --> CreateOrder : Select "Create order"
        CreateOrder --> OrderSaved : Add components
        OrderSaved --> OrderList : Save order
        OrderList --> ViewOrder : Select an order
        ViewOrder --> EditOrder : Edit order details
        EditOrder --> OrderSaved : Save changes
        OrderList --> DeleteOrder : Select an order to delete
        DeleteOrder --> OrderList : Confirm deletion
    }

    state "Application State" as AppState {
        [*] --> Home : Start application
        Home --> LoginState : Authentication
        LoginState --> OrderManagement : Successful login
        OrderManagement --> LoginState : Logout
    }

    [*] --> AppState : Application launch
    AppState --> [*] : Close application
    @enduml
    ```plantuml
    Lien:https://urlz.fr/th8U

## Eléments de conception
          ```plantuml
        @startuml
        skinparam componentStyle rectangle

        package "Architecture MVC" {
            component Controller <<control>> {
                + loginUser(email: String, password: String): boolean
                + logoutUser(): void
                + manageInventory(): void
                + processOrders(): void
            }

            component Model <<entity>> {
                + User
                + Order
                + Component
                + OrderStatus
            }

            component View <<boundary>> {
                + MainActivity
                + AdministratorActivity
                + StoreKeeperActivity
                + AssemblerActivity
                + RequesterActivity
            }

            Controller --> Model : Manipulates data
            Controller --> View : Controls UI
            View -> Controller : Sends user actions
            Model -> Controller : Notifies state changes
            View <-- Model : Observes data updates
        }

        note top of Controller
            Handles the interaction logic.
            Bridges the View and Model components.
        end note

        note right of Model
            Core business logic and data entities.
            Includes validation and state transitions.
        end note

        note bottom of View
            UI components tailored for each role.
            Handles user interaction and displays data.
        end note
        @enduml
        ```plantuml
        Lien:https://urlz.fr/th8S
        
    L'architecture MVC (Modèle-Vue-Contrôleur) de l'application est organisée en   trois composants principaux :

    Le Modèle (Model) : Il contient la logique métier et les entités principales, notamment User, Order, Component, et OrderStatus. Il est chargé de la gestion des données, de leur validation, ainsi que de la mise à jour des états associés.
    Le Contrôleur (Controller) : Il agit en tant qu'intermédiaire entre le modèle et la vue. Sa mission consiste à gérer la logique applicative et les interactions des utilisateurs. Parmi ses fonctionnalités figurent : loginUser, logoutUser, manageInventory, et processOrders.
    La Vue (View) : Elle englobe les interfaces utilisateur dédiées aux différents rôles de l'application. Parmi les vues principales, on retrouve MainActivity, AdministratorActivity, StoreKeeperActivity, AssemblerActivity, et RequesterActivity.
    Les relations entre ces composants sont clairement définies pour garantir une communication fluide. Le contrôleur manipule les données du modèle et contrôle les interactions avec la vue. Les vues, de leur côté, envoient les actions des utilisateurs au contrôleur. Simultanément, le modèle informe le contrôleur en cas de modification d'état. La vue observe également les changements dans le modèle afin de mettre à jour dynamiquement l'interface utilisateur.

    Cette organisation structurée permet une séparation nette des responsabilités, ce qui améliore la maintenabilité, l'extensibilité, et la compréhension globale du système.


## Eléments d'implémentation

    ```plantuml
    @startuml
    package "com.example.pcorderapplication.model" #DDDDDD {

    class User <<entity>> {
        - id : int
        - email : String
        - password : String
        - role : String
        - firstName : String
        - lastName : String
        - creationDateTime : LocalDateTime
        - modificationDateTime : LocalDateTime
        + login(email: String, password: String): boolean
        + logout(): void
    }

    class Order <<entity>> {
        - orderId : int
        - status : String
        - creationDateTime : LocalDateTime
        - modificationDateTime : LocalDateTime
        - components : List<Component>
        + createOrder(components: List<Component>): void
        + updateOrderStatus(newStatus: String): void
        + addComponent(component: Component): void
        + removeComponent(component: Component): void
    }

    class Component <<entity>> {
        - componentId : int
        - title : String
        - type : String
        - subtype : String
        - quantity : int
        - comment : String
        - creationDateTime : LocalDateTime
        - modificationDateTime : LocalDateTime
        + addComponent(title: String, type: String, subtype: String, quantity: int): void
        + removeComponent(componentId: int): void
        + updateComponentDetails(title: String, type: String, subtype: String, quantity: int, comment: String): void
    }

    User "1" --> "0..*" Order : places
    Order "1" --> "1..*" Component : contains
    }
    @enduml
    ```plantuml
    Lien:https://urlz.fr/th8Z
        Les éléments d'implémentation décrivent la structure des principales entités utilisées dans l'application, regroupées dans le package `com.example.pcorderapplication.model`. Ces entités comprennent les classes **User**, **Order**, et **Component**, qui modélisent les différents aspects des fonctionnalités de l'application. La classe **User** représente les utilisateurs, avec des attributs tels que `id`, `email`, `password`, `role`, ainsi que des informations personnelles comme `firstName` et `lastName`. Elle offre des méthodes permettant la gestion de l'authentification, notamment `login` pour la connexion et `logout` pour la déconnexion. Les utilisateurs peuvent passer plusieurs commandes, ce qui est représenté par une relation "1 vers 0..*" avec la classe **Order**.

        La classe **Order** modélise les commandes passées par les utilisateurs. Elle inclut des attributs comme `orderId` pour identifier de manière unique une commande, `status` pour suivre son état, et une liste `components` contenant les différents composants associés à la commande. Les méthodes de cette classe permettent de créer une commande (`createOrder`), de mettre à jour son statut (`updateOrderStatus`), ainsi que d'ajouter ou de supprimer des composants à partir de la commande. Cette classe entretient une relation "1 vers 1..*" avec la classe **Component**, qui modélise les éléments individuels d'une commande.

        La classe **Component** représente des éléments spécifiques comme des pièces matérielles ou des modules logiciels, avec des attributs tels que `componentId`, `title`, `type`, `subtype`, et `quantity`. Elle propose des méthodes pour ajouter un composant (`addComponent`), le supprimer (`removeComponent`), ou modifier ses détails (`updateComponentDetails`). 

        Les relations entre les classes renforcent la logique de l'application : un utilisateur peut passer plusieurs commandes, chaque commande pouvant contenir plusieurs composants. Cette organisation assure une gestion claire et extensible des données, tout en maintenant une séparation nette des responsabilités au sein de l'application.
        Cette architecture garantit une modularité et une flexibilité dans l'évolution des fonctionnalités. Les relations entre les classes mettent en évidence les interactions logiques, comme la capacité de chaque utilisateur à passer plusieurs commandes grâce à la relation entre **User** et **Order**, ou encore l'association de multiples composants à une commande grâce à la relation entre **Order** et **Component**. 

        L'utilisation des attributs temporels `creationDateTime` et `modificationDateTime` dans chaque classe permet de tracer l'historique des modifications et de faciliter la gestion des versions des entités. Par exemple, un utilisateur peut consulter l'historique de ses commandes ou suivre les mises à jour sur les composants d'une commande donnée.

        L'implémentation des méthodes assure également une encapsulation robuste : les actions sur les entités, comme la création, la mise à jour ou la suppression, sont encapsulées dans leurs classes respectives. Cela renforce l'intégrité des données et limite les risques d'erreurs en isolant les responsabilités.

        En résumé, les éléments d'implémentation fournissent une base solide pour construire les fonctionnalités principales de l'application tout en offrant une clarté dans les relations entre les entités. Ils soutiennent un développement évolutif et une maintenance aisée de l'application grâce à une organisation bien pensée et conforme aux principes d'une conception orientée objet.





## Eléments de tests unitaires

        un test Unitaire existe pour chaque cotroller, la classe AllTests permet de tester l'integralté, mais il est oreferable de tester chacune des classe indivduellement (s dans le package com.example.pcorderapplication (test) on a:

        AdministratorControllerTest
        AssemblerControllerTest
        LoginControllerTest
        MainControllerTest
        RequesterControllerTest
        StoreKeeperControllerTest
        
        1.Diagramme de classes pour les tests unitaires
        ```plantuml
        @startuml
        package "Tests Unitaires" #DDDDDD {

            class AdministratorControllerTest <<Test>> {
                + testCreateRequester()
                + testModifyRequester()
                + testDeleteRequester()
                + testLoginAdministrator()
                + testLogoutAdministrator()
            }

            class AssemblerControllerTest <<Test>> {
                + testAddOrder()
                + testViewAllOrders()
                + testAcceptOrder()
                + testRejectOrder()
                + testCompleteOrder()
            }

            class RequesterControllerTest <<Test>> {
                + testCreateOrder()
                + testDeleteOrder()
                + testViewOwnOrders()
                + testValidateComponents()
            }

            class StoreKeeperControllerTest <<Test>> {
                + testAddComponent()
                + testFindComponentByTitle()
                + testUpdateComponent()
                + testDeleteComponent()
            }

            class LoginControllerTest <<Test>> {
                + testLogin_Success()
                + testLogin_Failure()
            }

            class MainControllerTest <<Test>> {
                + testNavigateToRoleActivity_Administrator()
                + testNavigateToRoleActivity_StoreKeeper()
                + testNavigateToRoleActivity_InvalidRole()
            }

            AdministratorControllerTest --> AdministratorController
            AssemblerControllerTest --> AssemblerController
            RequesterControllerTest --> RequesterController
            StoreKeeperControllerTest --> StoreKeeperController
            LoginControllerTest --> LoginController
            MainControllerTest --> MainController
        }
        @enduml
        ```plantuml
        Lien:https://urlz.fr/th96
        
        2.Diagramme d'interaction pour les tests
        
            ```plantuml
            @startuml
            actor Tester
            participant "AdministratorControllerTest" as ACT
            participant "AdministratorController" as AC
            participant "Administrator (Mock)" as MockAdmin

            Tester -> ACT: Exécuter testCreateRequester()
            ACT -> AC: createRequester("John", "Doe", ...)
            AC -> MockAdmin: createRequester("John", "Doe", ...)
            MockAdmin --> AC: Confirme la création
            AC --> ACT: Vérification réussie
            ACT --> Tester: Test Passed
            @enduml
            ```plantuml
            Lien:https://urlz.fr/th94            
 ## Eléments de tests instrumentalisés
    
    Les tests instrumentalisés vérifient les interactions utilisateur et le comportement des écrans dans un environnement d'exécution proche du réel.

###Diagrammes des Scénarios de Test
    RequesterActivity – Gestion des commandes par le demandeur
    Ce diagramme décrit les interactions pour créer, consulter et supprimer des commandes.
                ```plantuml
                @startuml
                state "Order Management" as OrderManagement {
                    [*] --> OrderList : Access orders
                    OrderList --> CreateOrder : Select "Create order"
                    CreateOrder --> OrderSaved : Add components
                    OrderSaved --> OrderList : Save order
                    OrderList --> ViewOrder : Select an order
                    ViewOrder --> EditOrder : Modify order details
                    EditOrder --> OrderSaved : Save changes
                    OrderList --> DeleteOrder : Select an order to delete
                    DeleteOrder --> OrderList : Confirm deletion
                }

                @enduml
                ```plantuml
                Lien:https://urlz.fr/th9o
                
    StoreKeeperActivity – Gestion des stocks par le magasinier
    Ce diagramme illustre les actions du magasinier pour gérer les composants.
        ```plantuml
        @startuml
        |StoreKeeper|
        start
            :Access components;
            :Select "Add component";
            :Add details;
            :Save component;
            :Edit an existing component;
            :Save changes;
            :Delete a component;
            :Confirm deletion;
        stop
        @enduml

        ```plantuml
        Lien:https://urlz.fr/th9p
    RegisterActivity – Inscription d'un utilisateur
    Ce diagramme montre le processus d'inscription des utilisateurs.
        ```plantuml
        @startuml
        [*] --> RegistrationProcess

        state "Registration Process" as RegistrationProcess {
            [*] --> InputDetails : Start registration
            InputDetails --> Validation : Submit details
            Validation --> Success : Details valid
            Validation --> Failure : Details invalid
            Failure --> InputDetails : Correct details
            Success --> [*] : Registration complete
        }
        @enduml
        ```plantuml
        Lien:https://urlz.fr/th9q

    MainActivity – Authentification et navigation
    Ce diagramme décrit les actions de connexion et de navigation après authentification.
                ```plantuml
                @startuml
                [*] --> LoginState

                state "Authentication" as LoginState {
                    [*] --> InputCredentials : Enter email and password
                    InputCredentials --> Verification : Submit
                    Verification --> LoginSuccessful : Authentication validated
                    Verification --> LoginFailed : Show error
                    LoginSuccessful --> Navigation : Redirect based on role
                }
                @enduml


                ```plantuml
        Lien:https://urlz.fr/th9r
        
    AdministratorActivity – Gestion des utilisateurs par l'administrateur
    Ce diagramme représente les actions possibles pour gérer les utilisateurs.
                        ```plantuml
        @startuml
        [*] --> UserManagement

        state "User Management" as UserManagement {
            [*] --> UserList : Access the list of users
            UserList --> CreateUser : Add a user
            CreateUser --> UserCreated : Save details
            UserCreated --> UserList : Return to the list
            UserList --> EditUser : Edit a user
            EditUser --> UserEdited : Save changes
            UserList --> DeleteUser : Delete a user
            DeleteUser --> UserList : Confirm deletion
        }
        @enduml
                        ```plantuml
        Lien:https://urlz.fr/th9v

    AssemblerActivity – Gestion des commandes par l'assembleur
    Ce diagramme décrit les étapes de traitement des commandes.
                    ```plantuml
        @startuml
        actor Assembler

        state "Order Processing" as AssemblerOrderManagement {
            [*] --> PendingOrders : Display pending orders
            PendingOrders --> AcceptOrder : Accept an order
            AcceptOrder --> OrderProcessed : Process order
            PendingOrders --> RejectOrder : Reject an order
            RejectOrder --> OrderRejected : Add rejection message
            PendingOrders --> ViewOrder : View order details
        }
        @enduml
                        ```plantuml
        Lien:https://urlz.fr/th9w

## Comment reconstruire la solution
        ```plantuml
        @startuml
        participant "Utilisateur" as User
        participant "GitHub" as GitHub
        participant "Android Studio" as IDE
        
        User -> GitHub : Cloner le dépôt
        User -> IDE : Ouvrir le projet dans Android Studio
        IDE -> User : Exécuter la configuration de build
        @enduml
        ```plantuml
        Lien:https://urlz.fr/th9x


## Comment installer et utiliser la solution

    ```plantuml
    @startuml
    participant "Utilisateur" as User
    participant "Emulateur" as Emulator

    User -> IDE : Compiler et exécuter le projet
    IDE -> Emulator : Lancer l'application
    Emulator -> User : Tester l'application
    @enduml

    ```plantuml
    Lien:https://urlz.fr/th9z
    
## Eléments de démonstration

```plantuml
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
```plantuml
Lien: https://urlz.fr/th9B
### Scénario ("storyboard") suggéré

    Dans ce scénario, nous illustrons comment différents acteurs interagissent avec l'application pour accomplir leurs tâches respectives. Voici les étapes principales décrites à travers un diagramme UML :

    ```plantuml
    @startuml
    actor Requester
    actor StoreKeeper
    actor Assembler
    actor Administrator

    package "Processus de gestion des commandes" {
        Requester -> RequesterActivity : Créer une commande
        RequesterActivity -> StoreKeeperActivity : Valider les composants nécessaires
        StoreKeeper -> StoreKeeperActivity : Gérer les stocks
        StoreKeeperActivity -> AssemblerActivity : Notifier des commandes prêtes à assembler
        Assembler -> AssemblerActivity : Traiter et finaliser une commande
        AssemblerActivity -> AdministratorActivity : Notifier l'Administrator des commandes complétées
        Administrator -> AdministratorActivity : Superviser le processus
    }
    @enduml
    ```plantuml
    Lien:https://urlz.fr/th9C

    Ce scénario illustre le processus collaboratif entre les différents rôles de l'application pour gérer les commandes de manière structurée. Le Requester crée une commande en sélectionnant les composants nécessaires, qui sont ensuite validés par le StoreKeeper en fonction de la disponibilité dans l'inventaire. Une fois validée, la commande est envoyée à l'Assembler, qui la traite et finalise l'assemblage. Enfin, l'Administrator supervise l'ensemble du processus pour garantir son bon déroulement et notifier les utilisateurs en cas de besoin. Ce flux assure une gestion efficace et coordonnée des commandes.




### Valeurs de test

Pour assurer la fiabilité et la robustesse des fonctionnalités, les tests instrumentalisés et unitaires ont été réalisés avec des cas représentatifs. Voici un résumé des valeurs utilisées pour chaque scénario de test, tirées des cas déjà fournis :

#### Tests instrumentalisés

- **MainActivityTest** :
  - **Scénario de connexion réussie** :
    - Email : `assembler@example.com`
    - Mot de passe : `assembler123`
  - **Scénario de connexion échouée** :
    - Email : `invalid@example.com`
    - Mot de passe : `wrongpassword`
  - **Navigation** :
    - Cliquer sur le lien **"Register"** pour vérifier l'accès à l'écran d'inscription.

- **RegisterActivityTest** :
  - **Validation réussie** :
    - Nom : `John`
    - Prénom : `Doe`
    - Email : `johndoe@example.com`
    - Mot de passe : `Password123`
  - **Validation échouée** :
    - Champs vides, déclenchant des erreurs : `Please enter a first name`.

- **RequesterActivityTest** :
  - **Création d'une commande** :
    - Titre du composant : `Intel i7`
    - Quantité : `10`
  - **Suppression d'une commande** :
    - ID de commande : `1` (commande préalablement créée).

- **StoreKeeperActivityTest** :
  - **Inventaire** :
    - **Ajout de composants** :
      - `Intel i7` (Type : `Hardware`, Sous-type : `CPU`, Quantité : `100`)
      - `Corsair 16GB RAM` (Type : `Hardware`, Sous-type : `Memory`, Quantité : `50`)
    - **Suppression de composants** :
      - Titre : `Intel i7`.

#### Tests unitaires

- **AdministratorControllerTest** :
  - **Création d'un utilisateur Requester** :
    - Prénom : `John`, Nom : `Doe`, Email : `john.doe@example.com`, Mot de passe : `password123`.
  - **Modification** :
    - Email existant : `jane.doe@example.com`, Nouveau nom : `Jane Smith`.
  - **Suppression** :
    - Email : `jake.doe@example.com`.

- **AssemblerControllerTest** :
  - **Traitement d'une commande** :
    - Rejet avec message : `Invalid order`.
    - Finalisation : Mise à jour du statut à `Completed`.

Ces valeurs couvrent une large gamme de scénarios, allant des cas normaux aux erreurs, pour garantir que chaque composant et interaction se comporte correctement dans des situations réelles et imprévues.


#### Utilisateurs

        | Rôle           | Identifiant de connexion | Mot de passe |
        |----------------|--------------------------|--------------|
        | Administrateur |admin@gmail.com           | admin123     |
        | StoreKeeper    |storekeeper@gmail.com>    | store123r    |
        | Assembler      |  assembler@gmail.com     |assembler123  |

#### Fichier de données exemple

<com.example.pcorderapplication.view.MainActivity

###Fichier APK
Le fichier APK a ete genere sans problemes:
| **Section**            | **Taille**         | **Description**                                                                     |
|------------------------|--------------------|-------------------------------------------------------------------------------------|
| **Classes.dex**        | 27,5 MB (96,7%)    | Contient le code Java/Dalvik, dépendances volumineuses comme Firebase/Play Services |
| **res**                | 758,2 KB (7,2%)    | Ressources XML et graphiques légères, bien proportionnées.                          |
| **lib (natives)**      | ~2,4 MB            | Bibliothèques natives `.so` pour plusieurs architectures.                           |
| **Firebase / Protobuf**| Quelques KB        | Protocol Buffers pour la communication serveur-client via Firestore.                |
| **AndroidManifest.xml**| 3,9 KB             | Métadonnées pour l'application, permissions, déclarations Firebase/Play Services.   |

L'APK de l'application est principalement dominé par les fichiers Dex, qui contiennent le code Java/Dalvik et les dépendances telles que Firebase et Google Play Services, représentant 96,7% de la taille totale (27,5 MB). Les ressources XML et graphiques (res) occupent 758,2 KB, tandis que les bibliothèques natives (.so) pour plusieurs architectures contribuent à environ 2,4 MB. Firebase et Protobuf ajoutent quelques KB pour la communication serveur-client via Firestore. Enfin, le fichier AndroidManifest.xml de 3,9 KB inclut les métadonnées, permissions, et déclarations essentielles pour l'application.


## Limites et problèmes connus


 
## Information destinées aux correcteurs

        ce projet a été concu sur Android Studio Ladybug | 2024.2.1 Canary 8  compiler l'application sur un e version antérieure peut causer des problémes des erreurs de compatibilité. La version du gradle utilisée est le 8.9
