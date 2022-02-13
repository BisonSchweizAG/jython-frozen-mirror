package org.python.core.util;

/**
 * Helper class for AccessibleSupport Test.
 * <p>
 * All methods return their name as String for easy reflective call testing.
 *
 */
class PackagePrivate {

    public String publicMember;
    String packagePrivateMember;
    protected String protectedMember;
    private String privateMember;
    
    public String callMePublic() {
        return "callMePublic";
    }

    String callMePackagePrivate() {
        return "callMePackagePrivate";
    }

    protected String callMeProtected() {
        return "callMeProtected";
    }

    private String callMePrivate() {
        return "callMePrivate";
    }

}
