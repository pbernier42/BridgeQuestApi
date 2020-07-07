package LaFalaise.BridgeQuest.entity;

public enum Role {
    HUMAIN("Humain"),
    ESPRIT("Esprit");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String toString(){
        return this.role;
    }
}
