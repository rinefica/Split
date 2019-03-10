package split;

import java.security.Permission;

public class DisallowExitSecurityManager extends SecurityManager {
    private final SecurityManager delegatedSecurityManager;
    private Integer firstExitStatusCode;

    public DisallowExitSecurityManager(final SecurityManager originalSecurityManager) {
        this.delegatedSecurityManager = originalSecurityManager;
    }

    @Override
    public void checkExit(final int statusCode) {
        if (firstExitStatusCode == null) {
            this.firstExitStatusCode = statusCode;
        }
        throw new SystemExitPreventedException();
    }

    public Integer getFirstExitStatusCode() {
        return firstExitStatusCode;
    }

    @Override
    public void checkPermission(Permission perm) {
        if (delegatedSecurityManager != null) {
            delegatedSecurityManager.checkPermission(perm);
        }
    }

    class SystemExitPreventedException extends SecurityException {

    }
}
