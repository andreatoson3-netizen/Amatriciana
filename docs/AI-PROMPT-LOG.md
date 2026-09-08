# AI Prompt Log

This document provides examples of how AI tools were used by the team during the different phases of the Software Engineering process, from requirements analysis to design, implementation, testing and documentation.

## User Stories
- *Tool:* ChatGPT and Gemini
- *Purpose:* Support in the analysis and refinement of the project requirements,
  definition of User Stories, and formulation of clear and testable Acceptance
  Criteria.
- *Example prompt:* Aiutami a definire i dettagli tecnici della User Story 'Enforce simulation rules', assicurandomi che la logica di validazione verifichi correttamente la presenza di una 'Power Plant' entro il raggio richiesto prima di permettere ad una zona residenziale di contribuire alle metriche della città (gestendo lo scenario senza energia che quello con energia).    

## Design
- *Tool:* ChatGPT and Gemini
- *Purpose:* Support for architectural decision-making, application of GRASP principles, and selection of Design Patterns to ensure low coupling and high cohesion when managing simulation rules.
- *Example prompt:* Analizzando l'architettura del progetto e le responsabilità delle classi, aiutami a verificare se il Design Class Model è coerente con il codice e se rispetta i principi di basso accoppiamento e alta coesione.

## Coding
- *Tool:* Gemini
- *Purpose:* Support in implementing and reviewing Java code, understanding
  existing classes and methods, debugging, and checking the consistency
  between the implementation and the design.
- *Example prompt:* Scrivi il metodo distributeEnergy() per la classe Grid. La logica deve prima calcolare l'energia prodotta dalle PowerPlant e poi distribuirla alle strutture che ne consumano. Se non c'è abbastanza energia, metti le strutture in una blackoutQueue e imposta isOperative a false.

## Testing
- *Tool:* Gemini
- *Purpose:* Support in designing and reviewing JUnit 5 unit tests and System Tests, checking the correspondence between Acceptance Criteria and test cases, and identifying success, failure and boundary cases.
- *Example prompt:* Analizza gli Acceptance Criteria di questa User Story e suggerisci come tradurli in System Test verificabili, includendo sia i casi di successo sia i casi di errore.

## Documentation & Maintenance
- *Tool:* ChatGPT and Gemini
- *Purpose:* Support in reviewing and maintaining the README, UML diagrams, System Test documentation, AI documentation, and consistency between the project documentation and the implemented code.
- *Example prompt:* Controlla se questo Design Class Model è coerente con le classi e i metodi presenti nel codice e indicami eventuali relazioni, nomi o responsabilità non aggiornati/sistemati.
