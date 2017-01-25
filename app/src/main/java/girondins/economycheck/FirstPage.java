package girondins.economycheck;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Girondins on 13/09/15.
 * Aktivitetklass som är första sidan
 */
public class FirstPage extends Activity{
    private String user;
    private TextView greeting;
    private TextView status;
    private Button chart;
    private Button scan;
    private Controller cont;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.firstpage);
            initiateComp();
            initiateFrag();

        }

    /**
     * Instansierar komponenter och hämtar användare
     */
        public void initiateComp(){
            SharedPreferences sp = getSharedPreferences("FirstPage", Activity.MODE_PRIVATE);
            user = sp.getString("user",null);
            greeting = (TextView) findViewById(R.id.greetUserID);
            greeting.setText(user);
            status = (TextView) findViewById(R.id.statusTxtID);
            chart = (Button) findViewById(R.id.chartID);
            scan = (Button) findViewById(R.id.scanID);
            scan.setOnClickListener(new scanClicker());
            chart.setOnClickListener(new chartClicker());
        }

    /**
     * Metod som sätter de dynamiska fragmenten i layout
     * @param frag fragmentet som ska sättas
     * @param backstack kontrollerar ifall det är det första fragmentet som visas
     */
        public void setFragment(Fragment frag, boolean backstack){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentLay, frag);
            if(backstack){
                ft.addToBackStack(null);
            }
            ft.commit();
        }

    /**
     * Metod som beskriver ekonomiskstatus
     * @param difference
     */
        public void setStatus(int difference){
            if(difference > 0){
                status.setText("GOOD JOB! YOU HAVE SAVED: " + difference);
            }else
                status.setText("OH NO BETTER START SAVING: " + difference);
        }

    /**
     * Metod som instasierar de statiska fragmenten och controller
     */
        public void initiateFrag(){
            FragmentManager fm = getFragmentManager();
            ButtonFragment bf = (ButtonFragment) fm.findFragmentById(R.id.fragmentButtons);
            cont = new Controller(this,bf);
            bf.setController(cont);

        }

    /**
     * Inre klass som startar diagram aktivitet
     */
    private class chartClicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            cont.showChart();
        }
    }

    /**
     * Inre klass som starta scanning aktivitet
     */
    private class scanClicker implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            cont.startScan();
        }
    }

    /**
     * Metod som hanterar aktivtets resultat
     * @param requestCode begärd kod för kontroll
     * @param resultCode ifall allt gick som det ska
     * @param data resultatet för aktivteten
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            cont.scanComplete(scanContent);


        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}

