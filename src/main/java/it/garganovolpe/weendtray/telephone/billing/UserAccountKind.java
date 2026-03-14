package it.garganovolpe.weendtray.telephone.billing;
/*
    * A class representing the kind of user account, such as fixed cost or pay-as-you-go.
    * This class provides methods to retrieve the account kind by id or name, and to get all available kinds.
    * It also defines two static instances for the fixed cost and pay-as-you-go account types.
*/
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

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserAccountKind other = (UserAccountKind) obj;
        return id.equals(other.id);
    }

    public static final UserAccountKind FIXED_COST = new UserAccountKind("FIXED_COST", "Conto fisso");
    public static final UserAccountKind PAY_AS_YOU_GO = new UserAccountKind("PAY_AS_YOU_GO", "Conto ricaricabile");
}
