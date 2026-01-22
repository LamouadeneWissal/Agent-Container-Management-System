# Agent Container Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

**A comprehensive Java application demonstrating the implementation of multiple Design Patterns and Aspect-Oriented Programming (AOP)**

[Features](#-features) • [Design Patterns](#-design-patterns) • [Architecture](#-architecture) • [Getting Started](#-getting-started) • [Documentation](#-documentation)

</div>

---

##  Overview

This project is a robust **Agent Container Management System** built with Spring Boot that showcases enterprise-level software architecture principles. It demonstrates the practical application of **5 Gang of Four (GoF) Design Patterns** combined with **Aspect-Oriented Programming** for cross-cutting concerns like logging, caching, and security.

The system manages agents that can:
- 💼 Handle financial transactions (sales & purchases)
- 👀 Observe and react to other agents' activities
- 🔄 Dynamically change their notification processing behavior
- 🔒 Operate within a secure, role-based access control system

---

##  Features

### Core Functionality
- **Transaction Management** - Create, track, and manage financial transactions with the Builder pattern
- **Agent Communication** - Real-time notifications between agents using the Observer pattern
- **Flexible Strategies** - Pluggable notification handling strategies (Scoring, Statistics, History, Logging)
- **Singleton Container** - Thread-safe centralized agent management
- **Display Adapters** - Universal display interface supporting multiple monitor types

### Cross-Cutting Concerns (AOP)
- 📋 **`@Log`** - Automatic method execution logging with timing metrics
- ⚡ **`@Cachable`** - Transparent result caching with TTL support
- 🔐 **`@SecuredBy`** - Declarative role-based access control

---

##  Design Patterns

| Pattern | Purpose | Implementation |
|---------|---------|----------------|
| **🔨 Builder** | Fluent object construction | `Transaction.builder().id().date().montant().type().build()` |
| **👁️ Observer** | Event-driven agent communication | `Agent` implements `Subject` & `Observer` interfaces |
| **🎯 Strategy** | Interchangeable algorithms | `NotificationStrategy` with 5 concrete implementations |
| **🔒 Singleton** | Single container instance | `AgentContainer.getInstance()` with double-checked locking |
| **🔌 Adapter** | Interface compatibility | `VGAToHDMIAdapter` bridges VGA displays to HDMI interface |

### Pattern Relationships

```
┌─────────────────────────────────────────────────────────────────────┐
│                        AgentContainer (Singleton)                   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                     manages                                   │   │
│  │    ┌─────────┐     ┌─────────┐     ┌─────────┐              │   │
│  │    │ Agent A │◄───►│ Agent B │◄───►│ Agent C │  (Observer)  │   │
│  │    └────┬────┘     └────┬────┘     └────┬────┘              │   │
│  │         │               │               │                    │   │
│  │    ┌────▼────┐     ┌────▼────┐     ┌────▼────┐              │   │
│  │    │Strategy │     │Strategy │     │Strategy │  (Strategy)  │   │
│  │    └─────────┘     └─────────┘     └─────────┘              │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                              │                                       │
│                              ▼                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              HDMIDisplay (Adapter Pattern)                    │   │
│  │    ┌────────────┐              ┌─────────────────┐           │   │
│  │    │HDMIMonitor │              │VGAToHDMIAdapter │           │   │
│  │    └────────────┘              └────────┬────────┘           │   │
│  │                                         │                     │   │
│  │                                    ┌────▼────┐               │   │
│  │                                    │VGAMonitor│               │   │
│  │                                    └─────────┘               │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

##  Architecture

### Project Structure

```
src/main/java/com/ex/gestion_conteneurs_agents/
│
├── 📦 model/
│   ├── Agent.java              # Core agent (Observer + Strategy patterns)
│   └── Transaction.java        # Financial transaction (Builder pattern)
│
├── 👁️ observer/
│   ├── Observer.java           # Observer interface
│   ├── Subject.java            # Subject interface
│   └── NotificationEvent.java  # Event payload
│
├── 🎯 strategy/
│   ├── NotificationStrategy.java  # Strategy interface
│   ├── DefaultStrategy.java       # Basic notification handling
│   ├── ScoringStrategy.java       # Balance calculation
│   ├── HistoryStrategy.java       # Event history tracking
│   ├── StatisticsStrategy.java    # Transaction statistics
│   └── LoggingStrategy.java       # File-based logging
│
├── 📦 container/
│   └── AgentContainer.java     # Singleton container
│
├── 🔌 adapter/
│   ├── HDMIDisplay.java        # Target interface
│   ├── HDMIMonitor.java        # Native HDMI implementation
│   ├── VGADisplay.java         # Adaptee interface
│   ├── VGAMonitor.java         # VGA implementation
│   └── VGAToHDMIAdapter.java   # Adapter
│
├── 🔧 aspects/
│   ├── annotations/
│   │   ├── Log.java            # Logging annotation
│   │   ├── Cachable.java       # Caching annotation
│   │   └── SecuredBy.java      # Security annotation
│   ├── LoggingAspect.java      # Execution logging
│   ├── CachingAspect.java      # Result caching
│   └── SecurityAspect.java     # Access control
│
├── 🔐 security/
│   ├── SecurityContext.java    # Authentication context
│   └── SecurityException.java  # Security errors
│
└── 📊 enums/
    └── TransactionType.java    # VENTE, ACHAT
```

---

##  Getting Started

### Prerequisites

- ☕ **Java 17** or higher
- 📦 **Maven 3.8+**
- 💻 Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/agent-container-management.git

# Navigate to project directory
cd agent-container-management

# Build the project
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run
```

---

##  Usage Examples

### Creating Transactions (Builder Pattern)

```java
Transaction transaction = Transaction.builder()
    .id("TXN-001")
    .date(LocalDateTime.now())
    .montant(1500.00)
    .type(TransactionType.VENTE)
    .build();
```

### Setting Up Agents with Observer Pattern

```java
// Create agents
Agent alice = new Agent("Alice");
Agent bob = new Agent("Bob");

// Bob observes Alice's transactions
alice.subscribe(bob);

// When Alice adds a transaction, Bob gets notified
alice.ajouterTransaction(transaction);
```

### Changing Strategies Dynamically

```java
// Switch to scoring strategy for balance tracking
agent.changerStrategie(new ScoringStrategy());

// Or use statistics strategy
agent.changerStrategie(new StatisticsStrategy());
```

### Using the Singleton Container

```java
AgentContainer container = AgentContainer.getInstance();

// Login for secured operations
SecurityContext.login("admin", "admin123");

// Add agents
container.ajouterAgent(alice);
container.ajouterAgent(bob);

// Display container state
container.afficherEtat();
```

### Connecting Different Displays (Adapter Pattern)

```java
// Use native HDMI monitor
container.connecterAfficheur(new HDMIMonitor());

// Or adapt a VGA monitor
container.connecterAfficheur(new VGAToHDMIAdapter(new VGAMonitor()));
```

---

##  Security

The system implements role-based access control with predefined users:

| Username | Password | Roles |
|----------|----------|-------|
| `admin` | `admin123` | ADMIN, MANAGER, USER |
| `manager` | `manager123` | MANAGER, USER |
| `user` | `user123` | USER |
| `guest` | `guest123` | GUEST |

### Securing Methods

```java
@SecuredBy(roles = {"ADMIN", "MANAGER"})
public boolean ajouterAgent(Agent agent) {
    // Only ADMIN and MANAGER can add agents
}
```

---

## 🧪 Testing

The project includes comprehensive unit tests covering all design patterns:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AgentTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Categories
- `TransactionTest` - Builder pattern tests
- `AgentTest` - Observer & Strategy pattern tests
- `ContainerTest` - Singleton & Adapter pattern tests
- `GestionConteneursAgentsApplicationTests` - Integration tests

---

##  UML Class Diagram

A complete PlantUML class diagram is available in [`docs/diagramme_classes.puml`](docs/diagramme_classes.puml)

To visualize:
1. Install PlantUML extension in your IDE
2. Open the `.puml` file
3. Preview the diagram

---

##  Educational Purpose

This project was developed as part of the **Design Patterns & Aspect-Oriented Programming** course for **3 GLSID** students. It demonstrates:

### SOLID Principles
- **S**ingle Responsibility - Each class has one clear purpose
- **O**pen/Closed - Extensible via interfaces (Strategy, Observer)
- **L**iskov Substitution - Implementations are interchangeable
- **I**nterface Segregation - Specific interfaces (HDMI, VGA, Strategy)
- **D**ependency Inversion - Depend on abstractions, not concretions

### Additional Patterns to Explore
The project architecture supports adding:
- **Factory Method** - Agent creation
- **Decorator** - Strategy enhancement
- **Command** - Undoable operations
- **Prototype** - Agent cloning
- **Facade** - Simplified system interface

---

##  Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

##  License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

##  Acknowledgments

- **Professor M. YOUSSFI** - Course instructor
- **Gang of Four** - Design Patterns: Elements of Reusable Object-Oriented Software
- **Spring Framework Team** - For the excellent AOP support

---

<div align="center">

**⭐ If you found this project helpful, please consider giving it a star! ⭐**

Made with ❤️ and ☕

</div>
