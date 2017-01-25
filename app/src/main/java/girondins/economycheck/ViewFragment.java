package girondins.economycheck;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;


/**
 * Fragmentklass som kan visa inkomster/utgifter/båda
 */
public class ViewFragment extends Fragment {
    private ListView itemList;
    private FirstPage fp;
    private Input[] inputs;
    private Controller cont;
    private EditText day;
    private EditText month;
    private EditText year;
    private Button conBut;
    private String task;
    private String showInfo = "";


    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Sätter aktivtet,information och controller
     * @param fp aktivtet
     * @param inputs inkomst/utgift/båda
     * @param cont controller
     */
    public void setFP (FirstPage fp, Input[] inputs,Controller cont,String task){
        this.fp = fp;
        this.inputs = inputs;
        this.cont = cont;
        this.task = task;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        itemList = (ListView) view.findViewById(R.id.listItemID);
        itemList.setAdapter(new ItemAdapter(fp,inputs));
        itemList.setOnItemClickListener(new itemListener());
        day = (EditText)view.findViewById(R.id.checkDayID);
        month = (EditText)view.findViewById(R.id.checkMonthID);
        year = (EditText)view.findViewById(R.id.checkYearID);
        conBut = (Button)view.findViewById(R.id.checkButtonID);
        conBut.setOnClickListener(new conListener());
        return view;
    }

    /**
     * OnItemClicklistener som bytar fragment vid trcyk och visar information
     */
    private class itemListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(position);
            showInfo = inputs[position].getCategory() + ", " + inputs[position].getProduct() + ", " + inputs[position].getInput() + ", " + inputs[position].getDate();
            cont.showInfo(showInfo);

        }
    }

    /**
     * Metod som hanterar händelse vid orientationsförändring
     * @param inflater
     * @param viewGroup
     */
    public void populateOnOrientationChange(LayoutInflater inflater, ViewGroup viewGroup) {
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_view, viewGroup);
        itemList = (ListView) subview.findViewById(R.id.listItemID);
        itemList.setAdapter(new ItemAdapter(fp,inputs));
        itemList.setOnItemClickListener(new itemListener());
        day = (EditText)subview.findViewById(R.id.checkDayID);
        month = (EditText)subview.findViewById(R.id.checkMonthID);
        year = (EditText)subview.findViewById(R.id.checkYearID);
        conBut = (Button)subview.findViewById(R.id.checkButtonID);
        conBut.setOnClickListener(new conListener());

    }

    /**
     * Metod som hanterar händelse vid förändring i fragment
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(fp);
        SharedPreferences sp = fp.getSharedPreferences("fragSave", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("day",day.getText().toString());
        editor.putString("month",month.getText().toString());
        editor.putString("year",year.getText().toString());
        editor.apply();
        populateOnOrientationChange(inflater, (ViewGroup) getView());
        String dayFrag = sp.getString("day",null);
        String monthFrag = sp.getString("month",null);
        String yearFrag = sp.getString("year",null);
        if(yearFrag != null){
            day.setText(dayFrag);
            month.setText(monthFrag);
            year.setText(yearFrag);
        }

    }

    /**
     * Metod som tillåter att visa dag/månad/år
     */
    private class conListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String dayDate = day.getText().toString();
            String monthDate = month.getText().toString();
            String yearDate = year.getText().toString();
            if(!dayDate.equals("") && !monthDate.equals("") && !yearDate.equals("")){
                cont.showDate(yearDate, monthDate, dayDate, task);
            }
            else if(!monthDate.equals("") && !yearDate.equals("")){
                cont.showDate(yearDate,monthDate,task);
            }
            else if(!yearDate.equals("")){
                cont.showDate(yearDate,task);
            }
        }
    }
}
