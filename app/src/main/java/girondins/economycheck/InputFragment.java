package girondins.economycheck;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * Fragmentklass för att lägga till nya inkomster/utgifter
 */
public class InputFragment extends Fragment {
    private Button confirmBtn;
    private TextView confirmTxt;
    private TextView choiceTxt;
    private EditText amount;
    private EditText product;
    private ListView categoryView;
    private String[] categories;
    private String choosenCategory = "Other";
    private String prodName = "";
    private String curDay="",curMonth="",curYear="";
    private FirstPage fp;
    private Controller cont;
    private String user;
    private String task;
    private EditText day;
    private EditText month;
    private EditText year;


    public InputFragment() {
        // Required empty public constructor
    }

    /**
     * Sätter aktivitet,kontroller,användare och vad som ska läggas till
     * @param fp aktivtet
     * @param cont kontroller
     * @param task uppgift(Inkomst/Utgift)
     * @param user användare
     */
    public void setTask(FirstPage fp,Controller cont,String task, String user, String[] categories){
        this.fp = fp;
        this.cont = cont;
        this.user = user;
        this.task = task;
        this.categories = categories;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_input, container, false);
        confirmBtn = (Button)view.findViewById(R.id.confirmTxtBtn);
        confirmTxt = (TextView)view.findViewById(R.id.confirmTextID);
        amount = (EditText)view.findViewById(R.id.amountId);
        product = (EditText)view.findViewById(R.id.prodNameID);
        choiceTxt = (TextView)view.findViewById(R.id.inputChoiceID);
        categoryView = (ListView) view.findViewById(R.id.categoryListID);
        day = (EditText)view.findViewById(R.id.dayID);
        month = (EditText)view.findViewById(R.id.monthID);
        year = (EditText)view.findViewById(R.id.yearID);
        day.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "31")});
        month.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});


        choiceTxt.setText(task);
        product.setText(prodName);
        day.setText(curDay);
        month.setText(curMonth);
        year.setText(curYear);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        amount.setFilters(new InputFilter[]{digits});
        categoryView.setAdapter(new ArrayAdapter<String>(fp,android.R.layout.simple_list_item_1,categories));
        categoryView.setOnItemClickListener(new CategoryListener());


        return view;
    }

    /**
     * OnItemclick listener som sätter ut en bekräftelse text
     */
    private class CategoryListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv = (TextView)view;
            choosenCategory = tv.getText().toString();
            confirmTxt.setText("CATEGORY: " + choosenCategory);

        }
    }

    /**
     * Filter som endast tillåter siffror
     */
    InputFilter digits = new InputFilter() {
        /**
         * Metod som skapar ett filter som endast tillåter användaren att endast använda siffror
         * @param source
         * @param start
         * @param end
         * @param dest
         * @param dstart
         * @param dend
         * @return
         */
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }

    };

    /**
     * Metod som bekräftar skapandet av inkomst/utgift
     */
    public void confirm(){
        String conAmount = amount.getText().toString();
        String date = day.getText() + "/" + month.getText() + "/" + year.getText();
        if(!product.getText().toString().equals("")){
        prodName = product.getText().toString();}
        if(task.equals("Income")){
            conAmount = " +" + conAmount;
            Toast.makeText(fp,"INCOME ADDED",Toast.LENGTH_SHORT).show();
        }if(task.equals("Expense")) {
            conAmount = " -" + conAmount;
            Toast.makeText(fp, "EXPENSE ADDED", Toast.LENGTH_SHORT).show();
        }

        Input update = new Input(user,conAmount,date,choosenCategory,prodName);
        cont.updateDB(update);

    }

    /**
     * Metod som hanterar fragment vid förändring
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(fp);
        SharedPreferences sp = fp.getSharedPreferences("fragmentSave", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("value",amount.getText().toString());
        editor.putString("confirm",confirmTxt.getText().toString());
        editor.putString("product",product.getText().toString());
        editor.putString("day",day.getText().toString());
        editor.putString("month",month.getText().toString());
        editor.putString("year",year.getText().toString());
        editor.apply();
        populateOnOrientationChange(inflater, (ViewGroup) getView());
        String amountFrag = sp.getString("value", null);
        String confirmFrag = sp.getString("confirm",null);
        String produFrag = sp.getString("product",null);
        String dayFrag = sp.getString("day",null);
        String monthFrag = sp.getString("month",null);
        String yearFrag = sp.getString("year",null);
        if(amountFrag != null){
            amount.setText(amountFrag);
            confirmTxt.setText(confirmFrag);
            product.setText(produFrag);
            day.setText(dayFrag);
            month.setText(monthFrag);
            year.setText(yearFrag);
        }

    }

    /**
     * Metod som hanterar och instansierar objekt, vid orientationsförändring
     * @param inflater
     * @param viewGroup
     */
    public void populateOnOrientationChange(LayoutInflater inflater, ViewGroup viewGroup){
        viewGroup.removeAllViewsInLayout();
        View subview = inflater.inflate(R.layout.fragment_input, viewGroup);
        confirmBtn = (Button)subview.findViewById(R.id.confirmTxtBtn);
        confirmTxt = (TextView)subview.findViewById(R.id.confirmTextID);
        amount = (EditText)subview.findViewById(R.id.amountId);
        product = (EditText)subview.findViewById(R.id.prodNameID);
        choiceTxt = (TextView)subview.findViewById(R.id.inputChoiceID);
        categoryView = (ListView) subview.findViewById(R.id.categoryListID);
        day = (EditText)subview.findViewById(R.id.dayID);
        month = (EditText)subview.findViewById(R.id.monthID);
        year = (EditText)subview.findViewById(R.id.yearID);
        day.setFilters(new InputFilter[]{new InputFilterMinMax("1", "31")});
        month.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});

        choiceTxt.setText(task);
        product.setText(prodName);
        day.setText(curDay);
        month.setText(curMonth);
        year.setText(curYear);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        amount.setFilters(new InputFilter[]{digits});
        categoryView.setAdapter(new ArrayAdapter<String>(fp, android.R.layout.simple_list_item_1, categories));
        categoryView.setOnItemClickListener(new CategoryListener());

    }

    /**
     * Metod som hanterar köp via sträckkoder
     * @param barcode sträckkod
     * @param days dag när köpet registrerades
     * @param months månad när köpet registrerades
     * @param years år när köpet registerades
     */
    public void barcodeExp(String barcode, int days, int months, int years){
        prodName = barcode;
        curDay = days+"";
        curMonth = months+"";
        curYear = years+"";
    }

    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }




}
