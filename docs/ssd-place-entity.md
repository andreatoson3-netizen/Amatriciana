sequenceDiagram
    actor Mayor as City Mayor
    participant System as City System

    Mayor->>System: Place entity(entityType, position)
    activate System

    alt Valid placement and sufficient budget
        System-->>Mayor: Entity placed
    else Cell occupied
        System-->>Mayor: Invalid placement
    else Insufficient budget
        System-->>Mayor: Insufficient budget
    end

    deactivate System
