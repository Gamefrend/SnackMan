package hsrm.mi.game.messaging;

public class FrontendNachrichtEvent {

    public enum EventTyp {CREATE, UPDATE, DELETE}

    private EventTyp eventTyp;
    private long id;
    private double x;
    private double y;
    private double z;
    private double rotationX;
    private double rotationY;
    private double rotationZ;

    public FrontendNachrichtEvent(long id, EventTyp eventTyp, double x, double y, double z, double rotationX, double rotationY, double rotationZ) {
        this.id = id;
        this.eventTyp = eventTyp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public long getId() {
        return id;
    }

    public EventTyp getEventTyp() {
        return eventTyp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getRotationX() {
        return rotationX;
    }

    public double getRotationY() {
        return rotationY;
    }

    public double getRotationZ() {
        return rotationZ;
    }
}
