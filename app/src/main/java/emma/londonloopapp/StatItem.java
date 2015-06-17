package emma.londonloopapp;

/**
 * Created by Emma on 16/06/2015.
 */
public class StatItem {

    private long id;
    private long time;
    private int completed;
    private double miles;

    public StatItem(){

    }

    public StatItem(long id, long time, int completed, double miles){
        this.id = id;
        this.time = time;
        this.completed = completed;
        this.miles = miles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }



}
