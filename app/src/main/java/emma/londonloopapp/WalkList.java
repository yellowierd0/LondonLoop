package emma.londonloopapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emma on 14/04/2015.
 */
public class WalkList {

    private List<SectionItem> walks; // WalkItem items list

    private NodeList nl;

    public WalkList(Resources resources){
        walks = new ArrayList<>();
        setWalks(resources);

    }



    private void setWalks(Resources resources){



        /*walks.add(new WalkItem(1, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop1),
                resources.getString(R.string.loop1_description), 8.5, 51.483144, 0.177975));
        walks.add(new WalkItem(2, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop2),
                resources.getString(R.string.loop2_description), 7, 51.441233, 0.148956));
        walks.add(new WalkItem(3, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop3),
                resources.getString(R.string.loop3_description), 9, 51.393209,0.069081));
        walks.add(new WalkItem(4, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop4),
                resources.getString(R.string.loop4_description), 10, 51.370944,0.004860));
        walks.add(new WalkItem(5, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop5),
                resources.getString(R.string.loop5_description), 6, 51.319035,-0.063420));
        walks.add(new WalkItem(6, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop6),
                resources.getString(R.string.loop6_description), 4.5, 51.315728,-0.136744));
        walks.add(new WalkItem(7, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop7),
                resources.getString(R.string.loop7_description), 3.5, 51.332148,-0.209290));
        walks.add(new WalkItem(8, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop8),
                resources.getString(R.string.loop8_description), 7.3, 51.351650,-0.250176));
        walks.add(new WalkItem(9, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop9),
                resources.getString(R.string.loop9_description), 8.5, 51.411854,-0.308274));
        walks.add(new WalkItem(10, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop10),
                resources.getString(R.string.loop10_description), 3.5, 51.469927,-0.409793));
        walks.add(new WalkItem(11, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop11),
                resources.getString(R.string.loop11_description), 7.5, 51.505117,-0.418654));
        walks.add(new WalkItem(12, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop12),
                resources.getString(R.string.loop12_description), 4.5, 51.550933,-0.483414));
        walks.add(new WalkItem(13, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop13),
                resources.getString(R.string.loop13_description), 5, 51.610477,-0.498761));
        walks.add(new WalkItem(14, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop14),
                resources.getString(R.string.loop14_description), 3.8, 51.623932,-0.427529));
        walks.add(new WalkItem(15, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop15),
                resources.getString(R.string.loop15_description), 10, 51.610702,-0.380326));
        walks.add(new WalkItem(16, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop16),
                resources.getString(R.string.loop16_description), 10, 51.653365,-0.281950));
        walks.add(new WalkItem(17, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop17),
                resources.getString(R.string.loop17_description), 9.5, 51.652244,-0.148998));
        walks.add(new WalkItem(18, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop18),
                resources.getString(R.string.loop18_description), 4, 51.668264,-0.028316));
        walks.add(new WalkItem(19, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop19),
                resources.getString(R.string.loop19_description), 4, 51.634306,0.012118));
        walks.add(new WalkItem(20, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop20),
                resources.getString(R.string.loop20_description), 6, 51.621468,0.078004));
        walks.add(new WalkItem(21, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop21),
                resources.getString(R.string.loop21_description), 4.3, 51.616860,0.183245));
        walks.add(new WalkItem(22, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop22),
                resources.getString(R.string.loop22_description), 4, 51.593421,0.234098));
        walks.add(new WalkItem(23, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop23),
                resources.getString(R.string.loop23_description), 4, 51.559197,0.236748));
        walks.add(new WalkItem(24, resources.getDrawable(R.drawable.dummy), resources.getString(R.string.loop24),
                resources.getString(R.string.loop24_description), 5, 51.516886,0.191433));*/

    }

    public List<SectionItem> getWalks(){
        return walks;
    }

    public SectionItem getWalk(int i){
        return walks.get(i);
    }

    public int getIdFromTitle(String title){
        for (int i = 0; i < walks.size(); i++){
            /*if (getWalk(i).getStartNode().equals(title)){
                return i;
            }*/
        }
        return 0;
    }

    public int getSize(){
        return walks.size();
    }


}


