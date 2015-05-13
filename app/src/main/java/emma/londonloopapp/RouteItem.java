package emma.londonloopapp;

/**
 * Created by Emma on 13/05/2015.
 */
public class RouteItem {

    private String duration;
    private RoutePart[] routeParts;

    public RouteItem(String duration, RoutePart[] routeParts){

        this.duration = duration;
        this.routeParts = routeParts;

    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public RoutePart[] getRouteParts() {
        return routeParts;
    }

    public void setRouteParts(RoutePart[] routeParts) {
        this.routeParts = routeParts;
    }
}
