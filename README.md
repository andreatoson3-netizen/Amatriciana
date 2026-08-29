# Welcome to CityLogic

## 1. Project Description

### CityLogic is a Java-based city simulation system developed as part of the Software Engineering course during the academic year 2025/2026. In the system, the user plays the role of the City Mayor and manages the development of a virtual city.

The city is represented by a 20x20 grid, where the Mayor can place different types of buildings and infrastructures. Each entity affects the global state of the city through parameters such as money, pollution, happiness, population and energy.

The simulation progresses through discrete time steps called ticks. At each tick, the system evaluates the current city configuration, manages energy distribution, calculates the resulting statistics and applies the active city policy, if present.

The Mayor can construct and demolish entities, activate different city policies, monitor the current state of the city and advance the simulation. The city can also be saved to and loaded from a JSON file.
The main objective is to manage the available resources and develop the city while respecting the simulation rules. If the city's money becomes negative, the simulation reaches a Game Over state due to bankruptcy.

---

## 2. Installation and Execution

### 2.1 Prerequisites

The project requires:

- **JDK 18+**
- **Apache Maven**
- **Git**, to clone the repository

A Java IDE such as IntelliJ, Eclipse or Visual Studio Code can be used to open and run the project (**Git**).

The project does not require a database or an external server.

### 2.2 Installation

The project is available in the GitHub repository.

First, open a terminal and clone the repository:

```bash
git clone https://github.com/andreatoson3-netizen/Amatriciana.git
```

Then, navigate to the newly created project folder:

```bash
cd Amatriciana
```

### 2.3 Build and Test

To compile the project and run the automated tests, execute:

```bash
mvn clean package
```

If the build is completed successfully, Maven displays: BUILD SUCCESS

### 2.4 Execution

To launch the CityLogic application, execute:

```bash
mvn exec:java
```
The command starts the application and opens the CityLogic graphical dashboard.

---

### 3. Execution Environment and Constraints

The project is built and managed using **Apache Maven**. The required Java dependencies are specified in the `pom.xml` file and are automatically downloaded by Maven during the build process.

The application is a Java desktop application based on **Swing** and does not require an external application server.

Maven commands must be executed from the project root directory, where the `pom.xml` file is located.

To verify that Java and Maven are correctly installed, run:

```bash
java -version
mvn -version
```
---

### 4. Main Functions Reused from Existing Libraries

The project uses the **Jackson Databind** library for JSON serialization and deserialization.

In particular, the `ObjectMapper` class is used to:

- serialize the `City` object and save the current city state to a JSON file using `writeValue()`;
- deserialize a JSON file and reconstruct a `City` object using `readValue()`.

The project also uses **JUnit 5** for automated unit testing. It provides the testing framework and assertion methods used to verify the behaviour of the main components.

No other significant non-standard library functions are directly reused.

---

### 5. Main External APIs Used

The main external API used by the project is the **Jackson Databind API**, included as a Maven dependency.

It is used by the persistence component to manage the conversion between Java objects and JSON data during game saving and loading.

No external web APIs, database APIs, or cloud services are used by the project.

---

### 6. AI Tools Used

AI tools were used throughout the development of the project as support during the different phases of the Software Engineering process. The main AI tools used were **ChatGPT**, **Google Gemini**, and **Google NotebookLM**.

In particular, AI was used to:

- understand the project requirements and obtain a general overview of the system;
- support the definition and refinement of User Stories and Acceptance Criteria;
- assist in the preparation and review of design documentation, including the Design Class Model and other design documents;
- understand and analyse existing code and identify the main components and their responsibilities;
- support the implementation and debugging of Java code;
- assist in the definition and review of System Tests and other testing activities;
- support the preparation and review of project documentation, including the user manual.

AI was used as a support and learning tool. The generated suggestions and code were reviewed, adapted and integrated by the project team according to the project requirements and design decisions.
