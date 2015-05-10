package emma.londonloopapp;

/**
 * Created by Emma on 09/05/2015.
 */
public enum ClassType {
    NODE(1),
    SECTION(2),
    JOURNEY(3);

    private int value;

    private ClassType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }


}
