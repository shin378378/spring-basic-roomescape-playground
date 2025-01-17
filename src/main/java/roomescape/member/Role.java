package roomescape.member;

public enum Role {
    ADMINISTRATION("ADMIN"),
    USER("USER");

    final String roleType;

    Role(String roleType) {
        this.roleType = roleType;
    }
}
