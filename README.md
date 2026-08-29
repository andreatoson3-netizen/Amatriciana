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
- A Java IDE such as IntelliJ, Eclipse or Visual Studio Code can be used to open and run the project (**Git**).

The project does not require a database or an external server.

### 2.2 Clone the Repository

Clone the project repository using Git:

```bash
git clone <repository-url>


