# 🎓 Examen Design Patterns et Programmation Orientée Aspect

## 📋 Informations
- **Durée:** 3H
- **Classe:** 3 GLSID
- **Professeur:** Pr. M.YOUSSFI

---

## 📊 1. Diagramme de Classes

Le diagramme de classes est disponible dans le fichier `docs/diagramme_classes.puml`.

### Design Patterns Utilisés:

| Pattern | Utilisation | Classes Concernées |
|---------|-------------|-------------------|
| **Builder** | Création de transactions | `Transaction`, `TransactionBuilder` |
| **Observer** | Notification entre agents | `Subject`, `Observer`, `Agent`, `NotificationEvent` |
| **Strategy** | Traitement des notifications | `NotificationStrategy`, `DefaultStrategy`, `ScoringStrategy`, `HistoryStrategy`, etc. |
| **Singleton** | Instance unique du conteneur | `AgentContainer` |
| **Adapter** | Compatibilité HDMI/VGA | `HDMIDisplay`, `VGADisplay`, `VGAToHDMIAdapter` |

---

## 📦 2. Classe Transaction (Pattern Builder)

### Description
La classe `Transaction` utilise le **Pattern Builder** pour permettre la création flexible et lisible d'objets Transaction.

### Avantages du Builder:
- Construction étape par étape
- Validation à la création
- Code plus lisible et maintenable
- Immutabilité des objets créés

### Code clé:
```java
Transaction transaction = Transaction.builder()
    .id("TXN-001")
    .date(LocalDateTime.now())
    .montant(1500.00)
    .type(TransactionType.VENTE)
    .build();
```

### Tests: `TransactionTest.java`

---

## 👤 3. Classe Agent (Patterns Observer + Strategy)

### Description
La classe `Agent` implémente deux patterns:
- **Observer**: Pour permettre aux agents de s'observer mutuellement
- **Strategy**: Pour permettre de changer dynamiquement le comportement de traitement

### Pattern Observer:
- Un agent peut **souscrire** aux notifications d'autres agents
- Lors de l'ajout d'une transaction, tous les observateurs sont **notifiés**
- La notification transmet un `NotificationEvent` contenant le nom de l'agent et la transaction

### Pattern Strategy:
- Chaque agent utilise une stratégie de traitement des notifications
- Par défaut: `DefaultStrategy`
- Stratégies disponibles:
  - `ScoringStrategy`: Calcule un solde (ventes - achats)
  - `HistoryStrategy`: Conserve l'historique des notifications
  - `StatisticsStrategy`: Calcule des statistiques
  - `LoggingStrategy`: Journalise dans un fichier

### Tests: `AgentTest.java`

---

## 📦 4. Classe Container (Patterns Singleton + Adapter)

### Description
Le `AgentContainer` combine deux patterns:
- **Singleton**: Instance unique garantie dans l'application
- **Adapter**: Permet de connecter différents types d'afficheurs

### Pattern Singleton:
- Constructeur privé
- Méthode statique `getInstance()`
- Thread-safe avec double-checked locking

### Pattern Adapter:
- Interface standard: `HDMIDisplay`
- Afficheur natif: `HDMIMonitor`
- Adaptateur: `VGAToHDMIAdapter` pour les afficheurs VGA

### Tests: `ContainerTest.java`

---

## 💡 5. Patterns Supplémentaires Proposés

### 5.1 Factory Method / Abstract Factory
**Utilisation:** Création d'agents ou de stratégies
```java
public interface AgentFactory {
    Agent createAgent(String nom);
}

public class DefaultAgentFactory implements AgentFactory {
    public Agent createAgent(String nom) {
        Agent agent = new Agent(nom);
        agent.setStrategy(new DefaultStrategy());
        return agent;
    }
}
```

### 5.2 Decorator
**Utilisation:** Ajouter des fonctionnalités aux stratégies
```java
public class TimestampedStrategy implements NotificationStrategy {
    private NotificationStrategy wrapped;
    
    public void handleNotification(NotificationEvent event) {
        System.out.println("[" + LocalDateTime.now() + "]");
        wrapped.handleNotification(event);
    }
}
```

### 5.3 Command
**Utilisation:** Historique des opérations sur le conteneur
```java
public interface Command {
    void execute();
    void undo();
}

public class AddAgentCommand implements Command {
    private AgentContainer container;
    private Agent agent;
    
    public void execute() { container.ajouterAgent(agent); }
    public void undo() { container.supprimerAgent(agent.getNom()); }
}
```

### 5.4 Prototype
**Utilisation:** Clonage d'agents avec leurs configurations
```java
public interface Cloneable<T> {
    T clone();
}

public class Agent implements Cloneable<Agent> {
    public Agent clone() {
        Agent clone = new Agent(this.nom + "-copy");
        clone.setStrategy(this.strategy);
        return clone;
    }
}
```

### 5.5 Facade
**Utilisation:** Simplifier l'interface du système
```java
public class AgentSystemFacade {
    private AgentContainer container;
    
    public void createAndRegisterAgent(String nom, NotificationStrategy strategy) {
        Agent agent = new Agent(nom);
        agent.setStrategy(strategy);
        container.ajouterAgent(agent);
    }
    
    public void subscribeAgents(String observerName, String subjectName) {
        container.rechercherAgent(observerName)
            .ifPresent(observer -> 
                container.rechercherAgent(subjectName)
                    .ifPresent(subject -> subject.subscribe(observer)));
    }
}
```

---

## 🔧 6. Aspects Techniques (AOP)

### 6.a Aspect de Journalisation (@Log)

**Annotation:**
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    String message() default "";
    String level() default "INFO";
}
```

**Aspect:**
- Intercepte les méthodes annotées avec `@Log`
- Mesure et affiche la durée d'exécution
- Affiche les arguments et le résultat

**Utilisation:**
```java
@Log
public void ajouterTransaction(Transaction transaction) {
    // Code métier
}
```

### 6.b Aspect de Cache (@Cachable)

**Annotation:**
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cachable {
    String key() default "";
    long ttl() default 300;
}
```

**Aspect:**
- Stocke les résultats en cache
- Gère automatiquement l'expiration (TTL)
- Évite les recalculs coûteux

**Utilisation:**
```java
@Cachable
public Optional<Transaction> getTransactionMaxMontant() {
    return transactions.stream()
        .max(Comparator.comparingDouble(Transaction::getMontant));
}
```

### 6.c Aspect de Sécurité (@SecuredBy)

**Annotation:**
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredBy {
    String[] roles();
}
```

**Aspect:**
- Vérifie l'authentification
- Contrôle les rôles de l'utilisateur
- Bloque l'accès si non autorisé

**Utilisation:**
```java
@SecuredBy(roles = {"ADMIN", "MANAGER"})
public boolean ajouterAgent(Agent agent) {
    // Code métier
}
```

**Utilisateurs disponibles:**
| Username | Password | Rôles |
|----------|----------|-------|
| admin | admin123 | ADMIN, MANAGER, USER |
| manager | manager123 | MANAGER, USER |
| user | user123 | USER |
| guest | guest123 | GUEST |

---

## 🚀 Exécution du Projet

### Prérequis
- Java 17+
- Maven 3.8+

### Commandes
```bash
# Compilation
mvn clean compile

# Tests
mvn test

# Exécution
mvn spring-boot:run
```

---

## 📁 Structure du Projet

```
src/main/java/com/ex/gestion_conteneurs_agents/
├── adapter/
│   ├── HDMIDisplay.java          # Interface standard
│   ├── HDMIMonitor.java          # Implémentation HDMI
│   ├── VGADisplay.java           # Interface VGA
│   ├── VGAMonitor.java           # Implémentation VGA
│   └── VGAToHDMIAdapter.java     # Adaptateur VGA→HDMI
├── aspects/
│   ├── annotations/
│   │   ├── Log.java              # Annotation journalisation
│   │   ├── Cachable.java         # Annotation cache
│   │   └── SecuredBy.java        # Annotation sécurité
│   ├── LoggingAspect.java        # Aspect journalisation
│   ├── CachingAspect.java        # Aspect cache
│   └── SecurityAspect.java       # Aspect sécurité
├── container/
│   └── AgentContainer.java       # Singleton conteneur
├── enums/
│   └── TransactionType.java      # Types de transaction
├── model/
│   ├── Agent.java                # Agent (Observer + Strategy)
│   └── Transaction.java          # Transaction (Builder)
├── observer/
│   ├── NotificationEvent.java    # Événement de notification
│   ├── Observer.java             # Interface Observer
│   └── Subject.java              # Interface Subject
├── security/
│   ├── SecurityContext.java      # Contexte de sécurité
│   └── SecurityException.java    # Exception sécurité
├── strategy/
│   ├── NotificationStrategy.java # Interface Strategy
│   ├── DefaultStrategy.java      # Stratégie par défaut
│   ├── ScoringStrategy.java      # Stratégie scoring
│   ├── HistoryStrategy.java      # Stratégie historique
│   ├── LoggingStrategy.java      # Stratégie logging
│   └── StatisticsStrategy.java   # Stratégie statistiques
└── GestionConteneursAgentsApplication.java  # Application principale
```

---

## 📝 Conclusion

Ce projet démontre l'application pratique de 5 Design Patterns (Builder, Observer, Strategy, Singleton, Adapter) combinés avec la Programmation Orientée Aspect pour la journalisation, la mise en cache et la sécurisation.

L'architecture est extensible et respecte les principes SOLID:
- **S**ingle Responsibility: Chaque classe a une responsabilité unique
- **O**pen/Closed: Extensible via les interfaces (Strategy, Observer)
- **L**iskov Substitution: Les implémentations sont interchangeables
- **I**nterface Segregation: Interfaces spécifiques (HDMI, VGA, Strategy)
- **D**ependency Inversion: Dépendance sur les abstractions
