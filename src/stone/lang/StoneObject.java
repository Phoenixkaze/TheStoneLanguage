package stone.lang;

public class StoneObject {
    public static class AccessException extends Exception {}
    protected Environment environment;

    public StoneObject(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String toString() {
        return "<object:" + hashCode() + ">";
    }

    public Object read(String member) throws AccessException, StoneException {
        return getEnvironment(member).get(member);
    }

    protected Environment getEnvironment(String member) throws AccessException, StoneException {
        Environment environment = this.environment.where(member);
        if (environment != null && environment == this.environment) {
            return environment;
        } else {
            throw new AccessException();
        }
    }

    public void write(String member, Object value) throws AccessException, StoneException {
        getEnvironment(member).putNew(member, value);
    }
}
