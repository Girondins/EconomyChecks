package girondins.economycheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Huvudaktivitet som startas med appen
 */
public class MainActivity extends Activity {
    private Button loginBtn;
    private EditText enterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = (Button) findViewById(R.id.loginBtnID);
        enterUser = (EditText) findViewById(R.id.enterNameID);
        loginBtn.setOnClickListener(new loginListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Knapp som startar Firstpage aktivteten och lagrar ditt användarnamn
     */
    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String userName = enterUser.getText().toString();
            Intent login = new Intent(MainActivity.this,FirstPage.class);
            SharedPreferences sp = getSharedPreferences("FirstPage", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user", userName);
            editor.apply();
            startActivity(login);
        }
    }
}
