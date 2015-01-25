package emma.londonloop;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainEditText = (EditText) findViewById(R.id.main_edittext);

        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener((android.view.View.OnClickListener) this);

        // 3. Access the EditText defined in layout XML


    }

    @Override
    public void onClick(View v) {
        mainTextView.setText(mainEditText.getText().toString()
                + " is learning Android development!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
