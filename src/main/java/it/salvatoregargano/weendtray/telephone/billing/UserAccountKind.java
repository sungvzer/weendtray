package it.salvatoregargano.weendtray.telephone.billing;

public class UserAccountKind {
    private String id;

    private String name;

    public UserAccountKind(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return id;
    }

    public static UserAccountKind of(String id) {
        if (id.equals(FIXED_COST.getId())) {
            return FIXED_COST;
        } else if (id.equals(PAY_AS_YOU_GO.getId())) {
            return PAY_AS_YOU_GO;
        } else {
            throw new IllegalArgumentException("Invalid UserAccountKind id: " + id);
        }
    }

    public static UserAccountKind valueOf(String name) {
        if (name.equals(FIXED_COST.getName())) {
            return FIXED_COST;
        } else if (name.equals(PAY_AS_YOU_GO.getName())) {
            return PAY_AS_YOU_GO;
        } else {
            throw new IllegalArgumentException("Invalid UserAccountKind name: " + name);
        }
    }

    public static UserAccountKind[] values() {
        return new UserAccountKind[] { FIXED_COST, PAY_AS_YOU_GO };
    }

    public static final UserAccountKind FIXED_COST = new UserAccountKind("FIXED_COST", "Conto fisso");
    public static final UserAccountKind PAY_AS_YOU_GO = new UserAccountKind("PAY_AS_YOU_GO", "Conto ricaricabile");
}
