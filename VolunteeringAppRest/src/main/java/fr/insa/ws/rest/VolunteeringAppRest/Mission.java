package fr.insa.ws.rest.VolunteeringAppRest;

import java.util.UUID;

public class Mission {
    private String description;
    private String id;
    private int status; // 0: en attente, 1: validée, 2: réalisée

    public Mission(String description) {
        this.description = description;
        this.id = UUID.randomUUID().toString();
        this.status = 0; // Par défaut, la mission est en attente lors de la création
    }

    public String getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
