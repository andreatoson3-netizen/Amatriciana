# System test
## User story 1 - Initialize a City

### As a City Mayor, I want to initialize a block grid, so that I have a spatial canvas to develop my city.

### Acceptance criteria:

* The system must create a 20×20 grid containing a total of 400 blocks when the game is started or a new game is created.
  - Result: OK
  - Comment: The system correctly initializes a 20×20 grid, consisting of 400 blocks.
  - Date: 28.08.2026

* All 400 blocks must have the default state "Empty".
  - Result: OK
  - Comment: All 400 cells of the 20×20 grid are initialized as Empty
  - Date: 28.08.2026

* The system must reject operations involving coordinates outside the grid boundaries.
  - Result: OK
  - Comment: The system prevents the user from selecting or placing entities outside the 20×20 grid boundaries.
  - Date: 28.08.2026
 

## User story 2 - City monitoring and interaction

### As a City Mayor, I want to monitor the global city metrics and grid state as the simulation progresses, so that I can keep track of the simulation's progress and the impact of my choices.

### Acceptance criteria:

* The system must display the city grid when the City Mayor accesses the simulation.
  - Result: OK
  - Comment: The city grid is correctly displayed when the simulation is accessed.
  - Date: 28.08.2026

* The system must display the main City State metrics, including Money, Population, Happiness, Pollution and Energy.
  - Result: OK
  - Comment: The main City State metrics are correctly displayed on the dashboard.
  - Date: 28.08.2026

* The system must display the current layout of the city when the grid contains buildings or infrastructure.
  - Result: OK
  - Comment: The grid correctly displays the current city layout and the buildings and infrastructure placed on it.
  - Date: 28.08.2026

* Each occupied cell must show the entity placed on it.
  - Result: OK
  - Comment: Each occupied cell correctly displays the corresponding entity placed on it.
  - Date: 28.08.2026

* The displayed metrics must be updated when the City State changes.
  - Result: OK
  - Comment: The displayed metrics are correctly updated to reflect changes in the City State.
  - Date: 28.08.2026

 
## User story 3 - Load a saved City

### As a City Mayor, I want to load a previously saved city so that I can continue a previous simulation.

### Acceptance criteria:

* The system must restore the city's grid from a valid saved file.
  - Result: OK
  - Comment: The city grid was correctly restored after loading the valid JSON save file "Nuovo gioco".
  - Date: 28.08.2026

* The system must restore the buildings and infrastructure stored in the save file.
  - Result: OK
  - Comment: The buildings and infrastructure stored in the JSON save file were correctly restored.
  - Date: 28.08.2026

* The system must restore the city's global metrics, including Money, Population, Pollution, Energy and Happiness.
  - Result: OK
  - Comment: The city's global metrics were correctly restored from the JSON save file.
  - Date: 28.08.2026

* The City Mayor must be able to continue the simulation from the restored state.
  - Result: OK
  - Comment: After loading the saved city, the simulation could be continued from the restored state.
  - Date: 28.08.2026
    
* The system must reject an invalid or unreadable save file.
  - Result: OK
  - Comment: The system correctly rejected a PDF file when an invalid save file was selected.
  - Date: 28.08.2026
 
* The current city state must remain unchanged if the selected save file cannot be loaded.
  - Result: OK
  - Comment: After the invalid PDF file was rejected, the current city state remained unchanged.
  - Date: 28.08.2026
 

## User story 4 - Build buildings and infrastructure

### As a City Mayor, I want to select and place different types of buildings and infrastructure on the urban grid, so that I can develop my city's layout.

### Acceptance criteria:

* The system must provide the available building and infrastructure types defined by the simulation.
  - Result: OK
  - Comment: The available building and infrastructure types are displayed and can be selected by the City Mayor.
  - Date: 28.08.2026

* The City Mayor must be able to select a building or infrastructure type to place on the grid.
  - Result: OK
  - Comment: The City Mayor can select an entity before choosing the cell where it will be placed.
  - Date: 28.08.2026

* When sufficient budget is available and an empty cell is selected, the selected entity must be placed on that cell.
  - Result: OK
  - Comment: When sufficient funds are available, the selected entity is successfully placed on an empty cell.
  - Date: 28.08.2026
    
* The construction cost of the selected entity must be deducted from the city's budget.
  - Result: OK
  - Comment: The construction cost is deducted from the city's budget after a successful placement.
  - Date: 28.08.2026

* The system must reject an attempt to place an entity on an already occupied cell.
  - Result: OK
  - Comment: Attempting to place an entity on an occupied cell is rejected and an error message is displayed indicating that the cell is already occupied or outside the grid boundaries.
  - Date: 28.08.2026

* The existing entity must remain unchanged when an occupied cell is selected.
  - Result: OK
  - Comment: The existing entity remains in the selected cell when an attempt to place another entity is rejected.
  - Date: 28.08.2026

* The construction cost must not be deducted when the placement is rejected.
  - Result: OK
  - Comment: No construction cost is deducted when the placement is rejected because the selected cell is occupied.
  - Date: 28.08.2026
    
* The system must reject an attempt to place an entity when the City Mayor has insufficient budget.
  - Result: OK
  - Comment: When the available budget is insufficient, the construction attempt is rejected and an error message informs the City Mayor that the funds are not sufficient.
  - Date: 28.08.2026

* The entity must not be placed when the budget is insufficient.
  - Result: OK
  - Comment: The selected entity is not placed on the grid when the available budget is insufficient.
  - Date: 28.08.2026

* The city's budget must remain unchanged when the placement is rejected because of insufficient funds.
  - Result: OK
  - Comment: The city's budget remains unchanged when the construction attempt is rejected due to insufficient funds.
  - Date: 28.08.2026
 

## User story 5 - Impact of City objects

### As a City Mayor, I want each object built in the city to affect the City State so that each entity has an impact on the simulation.

### Acceptance criteria:

* The relevant City State metrics must be updated according to the effects associated with a placed object after the next simulation tick.
  - Result: OK
  - Comment: After the simulation tick, the City State metrics were updated according to the effects of the placed objects.
  - Date: 28.08.2026
    
* A Park must increase the City's Happiness according to its defined effect.
  - Result: OK
  - Comment: The Park increased the City's Happiness by 25.
  - Date: 28.08.2026

* A Park must decrease the City's Pollution according to its defined effect.
  - Result: OK
  - Comment: The Park decreased Pollution by 3. With the Factory and Power Plant already present, the expected total increase was 26, while the Park resulted in the expected Pollution effect and the final value was 23.
  - Date: 28.08.2026

* A Park must decrease the City's Money according to its defined effect.
  - Result: OK
  - Comment: The Park decreased the City's Money by 10, as expected.
  - Date: 28.08.2026

* A Factory must increase the City's Money according to its defined effect.
  - Result: OK
  - Comment: The Factory increased the City's Money by 40.
  - Date: 28.08.2026

* A Factory must increase the City's Pollution according to its defined effect.
  - Result: OK
  - Comment: The Factory increased the City's Pollution by 8. Considering the presence of the factory and the power plant, the total increase was 13, 8 for the factory and 5 for the Power Plant.
  - Date: 28.08.2026

* A Factory must decrease the City's Happiness according to its defined effect.
  - Result: OK
  - Comment: The Factory decreased the City's Happiness by 8, but only on the first Tick, as expected.
  - Date: 28.08.2026
    
* A Factory must decrease the City's Energy according to its defined effect.
  - Result: OK
  - Comment: The Factory decreased the City's Energy, but only on the first Tick.
  - Date: 28.08.2026

* A Residential building must update the City's Population, Happiness, Money, Pollution and Energy according to its defined effects.
  - Result: OK
  - Comment: When a house and a PowerPlant were placed, all metrics updated (e.g., Population: 50, ecc...).
  - Date: 28.08.2026
    
* A Commercial building must update the City's Money, Happiness, Pollution and Energy according to its defined effects.
  - Result: OK
  - Comment: When a shop and a Power Plant were placed, all metrics related to it updated (e.g., Pollution: 1 (5 PP + 1 S), ecc...).
  - Date: 28.08.2026

* A Road must update the City's Money, Happiness and Pollution according to its defined effects.
  - Result: OK
  - Comment: When a road was placed, all metrics related to it updated (e.g., Pollution: 1, Happiness: 2, Money: -5)
  - Date: 28.08.2026

* A Power Plant must increase the City's Energy according to its defined effect.
  - Result: OK
  - Comment:  The Power Plant increased the City's Energy by 100.
  - Date: 28.08.2026

* A Power Plant must increase the City's Pollution according to its defined effect.
  - Result: OK
  - Comment: The Power Plant increased the City's Pollution by 5.
  - Date: 28.08.2026

* A Power Plant must decrease the City's Happiness according to its defined effect.
  - Result: OK
  - Comment: The Power Plant decreased the City's Happiness by 5.
  - Date: 28.08.2026

* A Power Plant must decrease the City's Money according to its defined effect.
  - Result: OK
  - Comment: The Power Plant decreased the City's Money by 100, as expected.
  - Date: 28.08.2026

 
## User story 6 - Advance the simulation

### As a City Mayor, I want to progress the city simulation over time so that I can observe how my city evolves.

### Acceptance criteria:

* When the City Mayor triggers a Tick, the simulation time must advance by one Tick.
  - Result: OK
  - Comment: The simulation time increases by one Tick each time the City Mayor presses the "Next Tick" button.
  - Date: 28.08.2026

* The City State must be updated according to the simulation rules after a Tick.
  - Result: OK
  - Comment: The City State metrics are updated after each Tick according to the simulation rules.
  - Date: 28.08.2026

* When multiple consecutive Ticks are triggered, the simulation time must advance by one unit for each Tick.
  - Result: OK
  - Comment: Each consecutive press of the "Next Tick" button increases the simulation time by exactly one Tick.
  - Date: 28.08.2026

* The City State must be updated after each Tick according to the simulation rules.
  - Result: OK
  - Comment: The City State metrics are updated after every Tick, reflecting the changes caused by the simulation.
  - Date: 28.08.2026
 


## User story 7 - Enforce simulation rules

### As a City Mayor, I want the entities in the city to follow the rules defined by the simulation, so that the city's behavior remains consistent with the established rules.

### Acceptance criteria:

* A residential zone without a Power Plant within the required range must not contribute to the city's metrics.
  - Result: OK
  - Comment: When a residential zone was placed without a Power Plant within the required range and the "Next Tick" button was pressed, the system displayed a warning stating that 1 building was currently without power, and the residential zone did not affect the city metrics.
  - Date: 28.08.2026

* A residential zone with a Power Plant within the required range must contribute to the city's metrics according to its defined effects.
  - Result: OK
  - Comment: When a Power Plant was placed within the required range of the residential zone, the residential zone became powered and started affecting the city metrics after the simulation tick.
  - Date: 28.08.2026
 

## User story 8 - Activate City Policies

### As a City Mayor, I want to activate and manage different City Policies, so that I can choose how to manage my city.

### Acceptance criteria:

* The system must register the selected City Policy as the active policy when the City Mayor activates it.
  - Result: OK
  - Comment: The system allows the City Mayor to select and activate one of the available policies, such as EnvironmentalTax or IndustrialExpansion.
  - Date: 28.08.2026

* The user interface must display the selected policy as active.
  - Result: OK
  - Comment: When a policy is selected, its button displays the "ACTIVE" status.
  - Date: 28.08.2026

* The system must provide at least two different City Policies that can be activated.
  - Result: OK
  - Comment: The system provides two different policies that can be activated: EnvironmentalTax and IndustrialExpansion.
  - Date: 28.08.2026

* When a different City Policy is activated, the previously active policy must no longer be active.
  - Result: OK
  - Comment: When a different policy is selected, the previously active policy changes from "ACTIVE" to "INACTIVE".
  - Date: 28.08.2026

* The newly selected City Policy must become the active policy.
  - Result: OK
  - Comment: When the other policy is selected, its status changes from "INACTIVE" to "ACTIVE", while the previously active policy becomes "INACTIVE".
  - Date: 28.08.2026

 
## User story 9 - Apply City Policy effects

### As a City Mayor, I want to see the effects of the selected City Policy on my city's metrics, so that I can understand how my policy choices affect the city.

### Acceptance criteria:

* The metrics affected by the active City Policy must be calculated according to that policy's rules.
  - Result: OK
  - Comment: When a City Policy is activated, the affected city metrics are calculated according to the rules defined by the selected policy.
  - Date: 28.08.2026

* When the City Mayor selects a different City Policy, the newly selected policy must become active.
  - Result: OK
  - Comment: When the City Mayor selects a different policy, the newly selected policy becomes active and replaces the previously active policy.
  - Date: 28.08.2026

* Subsequent calculations of the affected city metrics must follow the rules of the newly selected policy.
  - Result: OK
  - Comment: After switching to a different policy, subsequent calculations of the city metrics reflect the rules and effects of the newly selected policy.
  - Date: 28.08.2026
 

## User story 10 - Save the City

### As a City Mayor, I want to save my current city so that I can continue the simulation in the future.

### Acceptance criteria:

* The system must save the current grid state to a valid file.
  - Result: OK
  - Comment: The city was successfully saved to a JSON file.
  - Date: 28.08.2026

* The saved grid state must include building types and positions.
  - Result: OK
  - Comment: The generated JSON file contains the buildings present in the city and their positions.
  - Date: 28.08.2026

* The system must save the current City Stats.
  - Result: OK
  - Comment: The generated JSON file contains the current city metrics, including Money, Population, Pollution, Happiness and Energy.
  - Date: 28.08.2026
  - 
* The system must indicate that the save operation was successful.
  - Result: OK
  - Comment: After a successful save, the system displays the message "GAME SAVED SUCCESSFULLY".
  - Date: 28.08.2026
  - 
* The system must reject the save operation when the city cannot be written to the selected file.
  - Result: OK
  - Comment: The save operation was attempted in a non-writable system directory (`C:\Program Files`). The system correctly rejected the operation and displayed an error message.
  - Date: 2.09.2026

* The current city state must remain unchanged when the save operation fails.
  - Result: OK
  - Comment: After the save operation failed, the city state remained unchanged.
  - Date: 2.09.2026
    
* The system must inform the City Mayor that the city could not be saved.
  - Result: OK
  - Comment: When the save operation failed, the system displayed an error message "Unable to save the Game", informing the City Mayor that the game could not be saved.
  - Date: 2.09.2026
 

## User story 11 - Bankruptcy and Game Over (Additional feature)

### As a City Mayor, I want the simulation to detect when my city becomes bankrupt, so that I know when I can no longer continue the current game.

### Acceptance criteria:

* When the city does not have enough money to sustain its costs and a Tick is processed, the city must enter the bankrupt state.
  - Result: OK
  - Comment: After several Ticks, when the city's Money became negative, the system automatically entered the bankruptcy state.
  - Date: 28.08.2026

* The system must notify the City Mayor that the game is over.
  - Result: OK
  - Comment: When the city became bankrupt, the system displayed the message "GAME OVER-BANCAROTTA!".
  - Date: 28.08.2026

* When the city is bankrupt, the simulation controls must be disabled.
  - Result: OK
  - Comment: After bankruptcy, the simulation controls and the other game actions were disabled, while New Game, Save Game and Load Game remained available.
  - Date: 28.08.2026

* The City Mayor must be able to start a new game after bankruptcy.
  - Result: OK
  - Comment: After the bankruptcy state was reached, the New Game option remained available and could be selected.
  - Date: 28.08.2026

* The City Mayor must be able to load a saved game after bankruptcy.
  - Result: OK
  - Comment: After the bankruptcy state was reached, the Load Game option remained available and could be selected.
  - Date: 28.08.2026
 

## User story 12 - Building and infrastructure demolition (Additional feature)

### As a City Mayor, I want to demolish a building or infrastructure from the city grid, so that I can modify my city's layout.

### Acceptance criteria:

* When the City Mayor selects an occupied cell for demolition, the entity must be removed from the selected cell.
  - Result: OK
  - Comment: When an occupied cell is selected and the "Demolish" option is used, the building is successfully removed from the grid.
  - Date: 28.08.2026

* The selected cell must become empty after a successful demolition.
  - Result: OK
  - Comment: After the building is demolished, the selected cell becomes empty and the construction cost is refunded.
  - Date: 28.08.2026

* When the City Mayor attempts to demolish an empty cell, no demolition operation must be performed.
  - Result: OK
  - Comment: When the "Demolish" option is used on an empty cell, no demolition operation is performed and the grid remains unchanged.
  - Date: 28.08.2026

* The city grid must remain unchanged when an empty cell is selected for demolition.
  - Result: OK
  - Comment: Demolishing an empty cell does not modify the city grid or the city metrics.
  - Date: 28.08.2026
