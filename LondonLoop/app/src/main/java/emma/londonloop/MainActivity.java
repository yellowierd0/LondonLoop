package emma.londonloop;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;


public class MainActivity extends Activity {

    AlertDialog alertDialogStores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showList();

    }

    public void showList(){

        //add items, will later move to database

        SectionItem[] SectionItemData = new SectionItem[24];

        SectionItemData[0] = new SectionItem(1, "Erith", "Old Bexley");
        SectionItemData[1] = new SectionItem(2, "Old Bexley", "Petts Wood");
        SectionItemData[2] = new SectionItem(3, "Petts Wood", "West Wickham Common");
        SectionItemData[3] = new SectionItem(4, "West Wickham", "Croydon");
        SectionItemData[4] = new SectionItem(5, "Hamsey Green", "Couldson South");
        SectionItemData[5] = new SectionItem(6, "Couldson South", "Banstead Downs");
        SectionItemData[6] = new SectionItem(7, "Banstead Downs", "Ewell");
        SectionItemData[7] = new SectionItem(8, "Ewell", "Kingston Bridge");
        SectionItemData[8] = new SectionItem(9, "Kingston Bridge", "Hatton Cross");
        SectionItemData[9] = new SectionItem(10, "Hatton Cross", "Hayes and Harlington");
        SectionItemData[10] = new SectionItem(11, "Hayes and Harlington", "Uxbridge");
        SectionItemData[11] = new SectionItem(12, "Uxbridge", "Harefield West");
        SectionItemData[12] = new SectionItem(13, "Harefield West", "Moor Park");
        SectionItemData[13] = new SectionItem(14, "Moor Park", "Hatch End");
        SectionItemData[14] = new SectionItem(15, "Hatch End", "Elstree");
        SectionItemData[15] = new SectionItem(16, "Elstree", "Cockfosters");
        SectionItemData[16] = new SectionItem(17, "Cockfosters", "Enfield Lock");
        SectionItemData[17] = new SectionItem(18, "Enfield Lock", "Chingford");
        SectionItemData[18] = new SectionItem(19, "Chingford", "Chigwell");
        SectionItemData[19] = new SectionItem(20 , "Chigwell", "Havering-atte Bower");
        SectionItemData[20] = new SectionItem(21 , "Havering-atte Bower", "Harold Wood");
        SectionItemData[21] = new SectionItem(22 , "Harold Wood", "Upminster Bridge");
        SectionItemData[22] = new SectionItem(23 , "Upminster Bridge", "Rainham");
        SectionItemData[23] = new SectionItem(24 , "Rainham", "Purfleet");


        // our adapter instance
        ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.list_view_row_item, SectionItemData);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView)findViewById(R.id.listSectionItems);
        listViewItems.setAdapter(adapter);
        listViewItems.setOnItemClickListener(new OnItemClickListenerListViewItem());
    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
