package girondins.economycheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Girondins on 14/09/15.
 * Kontroll klass som sköter det logiska
 */
public class Controller {
    private ButtonFragment buttonFrag;
    private FirstPage fp;
    private String user;
    private UserDBHelper db;
    private Input[] inputs;
    private LinkedList<Input> extract;
    private Input[] incomeIn;
    private Input[] outcomeIn;
    private int difference;
    private String[] incomeCat = {"Salary", "Other"};
    private String[] expenseCat = {"Home", "Entertainment" , "Food", "Other"};
    private Calendar cal = Calendar.getInstance();
    private String currentDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);


    /**
     * Konstruktor för kontrollklassen, här sätts även Startfragmentet
     * @param fp activity/context som används
     * @param buttonFrag knappfragment som ska användas
     */
    public Controller(FirstPage fp,ButtonFragment buttonFrag){
        this.fp = fp;
        this.buttonFrag = buttonFrag;
        SharedPreferences sp = fp.getSharedPreferences("FirstPage", Activity.MODE_PRIVATE);
        user = sp.getString("user",null);
        db = new UserDBHelper(fp);
        buttonFrag.setController(this);
        StartFragment sf = new StartFragment();
        sf.showStart("CHOOSE A TASK!");
        fp.setFragment(sf, true);
        retriveUserInputs();

    }




    /**
     * Metod som hanterar scanningen och lagrar informationen som har hämtats
     * @param code streckkod
     */
    public void scanComplete(String code){
        retriveUserInputs();
        extractExpenses();

        if(outcomeIn.length!=0) {
            for (int i = 0; i < outcomeIn.length; i++) {
                if (outcomeIn[i].getProduct().equals(code)) {
                    Input input = outcomeIn[i];
                    input.setDate(currentDate);
                    updateDB(input);
                    i = outcomeIn.length;
                    Toast.makeText(fp, "Product Exist, Added Expense", Toast.LENGTH_LONG).show();
                }else
                    addExpense(code);
            }
        }else
            addExpense(code);
    }

    /**
     * Metod som startar fragment visar inkomster
     */
    public void showIncomeContent(){
        retriveUserInputs();
        extractIncome();
        ViewFragment vf = new ViewFragment();
        vf.setFP(fp, incomeIn, this,"Income");
        fp.setFragment(vf, false);

    }

    /**
     * Metod som startar fragment som visar inkomster/utgifter
     */
    public void showAllContent(){
        retriveUserInputs();
        ViewFragment vf = new ViewFragment();
        vf.setFP(fp, inputs, this, "All");
        fp.setFragment(vf, false);

    }

    /**
     * Metod som startar fragment som visar utgifter
     */
    public void showOutcomeContent(){
        retriveUserInputs();
        extractExpenses();
        ViewFragment vf = new ViewFragment();
        vf.setFP(fp, outcomeIn, this, "Expense");
        fp.setFragment(vf, false);

    }

    /**
     * Metod som startar fragment som tillåter en att lägga till inkomster
     */
    public void addIncome(){
        InputFragment inputFrag = new InputFragment();
        inputFrag.setTask(fp, this, "Income", user, incomeCat);
        fp.setFragment(inputFrag, false);

    }

    /**
     * Metod som startar fragment som tillåter en att lägga till utgifter
     */
    public void addExpense(){
        InputFragment inputFrag = new InputFragment();
        inputFrag.setTask(fp, this, "Expense", user, expenseCat);
        fp.setFragment(inputFrag, false);

    }
    public void addExpense(String barcode){
        InputFragment inputFrag = new InputFragment();
        inputFrag.setTask(fp, this, "Expense", user, expenseCat);
        inputFrag.barcodeExp(barcode,cal.get(Calendar.DATE),cal.get(Calendar.MONTH)+1,cal.get(Calendar.YEAR));
        fp.setFragment(inputFrag, false);

    }

    /**
     * Metod som startar fragment som visar en texthändelse
     * @param showTxt
     */
    public void showInfo(String showTxt){
        StartFragment sf = new StartFragment();
        sf.showText(showTxt);
        fp.setFragment(sf, false);
    }

    /**
     * Metod som beräknar överskott/underskott av ekonomin
     */
    public void calculateEcoStatus(){
        String value;
        int calc = 0;
        for(int y=0; y<inputs.length;y++){
            if(inputs[y].getInput().contains("+")) {
                value = inputs[y].getInput().substring(2, inputs[y].getInput().length());
                calc = calc + Integer.parseInt(value);
            }else if(inputs[y].getInput().contains("-")) {
                value = inputs[y].getInput().substring(1, inputs[y].getInput().length());
                calc = calc + Integer.parseInt(value);
            }
        }
        difference = calc;
    }

    /**
     * Metod som uppdaterar databasen
     * @param input information som ska läggas till
     */
    public void updateDB(Input input){
    db.addInOutcome(input);
        retriveUserInputs();
        calculateEcoStatus();
        fp.setStatus(difference);
    }

    /**
     * Metod som hämtar all information om användaren från databasen
     */
    public void retriveUserInputs(){
        this.inputs = db.getUserInputs(user);
        calculateEcoStatus();
        fp.setStatus(difference);
    }

    /**
     * Metod som hämtar ut alla utgifter
     */
    public void extractExpenses(){
        extract = new LinkedList<Input>();
        for(int i = 0 ; i<inputs.length;i++){
            if(inputs[i].getInput().contains("-")){
                extract.add(inputs[i]);
            }
        }
        outcomeIn = new Input[extract.size()];
        for(int j=0; j<extract.size() ;j++){
            outcomeIn[j] = extract.get(j);
        }
    }

    /**
     * Metod som hämtar  ut alla inkomster
     */
    public void extractIncome(){
        extract = new LinkedList<Input>();
        for(int i = 0 ; i<inputs.length;i++){
            if(inputs[i].getInput().contains("+")){
                extract.add(inputs[i]);
            }
        }
        incomeIn = new Input[extract.size()];
        for(int j=0; j<extract.size() ;j++){
            incomeIn[j] = extract.get(j);
        }

    }

    /**
     * Metod  som startar aktiviteten som visar diagram
     */
    public void showChart(){
        retriveUserInputs();
        extractIncome();
        extractExpenses();
        Intent chart = new Intent(fp,ChartView.class);
        chart.putExtra("allIn",inputs);
        chart.putExtra("outIn", outcomeIn);
        chart.putExtra("inIn", incomeIn);
        fp.startActivity(chart);
    }

    /**
     * Metod som startar streckkodsskanning
     */
    public void startScan(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(fp);
        scanIntegrator.initiateScan();
    }


    public void showDate(String year, String task){
        Input[] dateInput;
        LinkedList<Input> lengthDate = new LinkedList<Input>();

        if(task == "Income"){
            for(int i =0; i<incomeIn.length; i++){
                String[] split = incomeIn[i].getDate().split("/");
                if(split[2].equals(year)){
                    lengthDate.add(incomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Income");
            fp.setFragment(vf, false);
        }

        if(task == "Expense"){
            for(int i =0; i<outcomeIn.length; i++){
                String[] split = outcomeIn[i].getDate().split("/");
                if(split[2].equals(year)){
                    lengthDate.add(outcomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Expense");
            fp.setFragment(vf, false);

        }
        if(task == "All"){
            for(int i =0; i<inputs.length; i++){
                String[] split = inputs[i].getDate().split("/");
                if(split[2].equals(year)){
                    lengthDate.add(inputs[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"All");
            fp.setFragment(vf, false);

        }
    }

    public void showDate(String year, String month, String task){
        Input[] dateInput;
        LinkedList<Input> lengthDate = new LinkedList<Input>();

        if(task == "Income"){
            for(int i =0; i<incomeIn.length; i++){
                String[] split = incomeIn[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month)){
                    lengthDate.add(incomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Income");
            fp.setFragment(vf, false);
        }

        if(task == "Expense"){
            for(int i =0; i<outcomeIn.length; i++){
                String[] split = outcomeIn[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month)){
                    lengthDate.add(outcomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Expense");
            fp.setFragment(vf, false);

        }
        if(task == "All"){
            for(int i =0; i<inputs.length; i++){
                String[] split = inputs[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month)){
                    lengthDate.add(inputs[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"All");
            fp.setFragment(vf, false);

        }

    }

    public void showDate(String year, String month, String day, String task){

        Input[] dateInput;
        LinkedList<Input> lengthDate = new LinkedList<Input>();

        if(task == "Income"){
            for(int i =0; i<incomeIn.length; i++){
                String[] split = incomeIn[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month) && split[0].equals(day)){
                    lengthDate.add(incomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Income");
            fp.setFragment(vf, false);
        }

        if(task == "Expense"){
            for(int i =0; i<outcomeIn.length; i++){
                String[] split = outcomeIn[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month) && split[0].equals(day)){
                    lengthDate.add(outcomeIn[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"Expense");
            fp.setFragment(vf, false);

        }
        if(task == "All"){
            for(int i =0; i<inputs.length; i++){
                String[] split = inputs[i].getDate().split("/");
                if(split[2].equals(year) && split[1].equals(month) && split[0].equals(day)){
                    lengthDate.add(inputs[i]);
                }
            }
            dateInput = new Input[lengthDate.size()];
            for(int y=0; y<lengthDate.size(); y++){
                dateInput[y] = lengthDate.get(y);
            }

            ViewFragment vf = new ViewFragment();
            vf.setFP(fp, dateInput, this,"All");
            fp.setFragment(vf, false);

        }

    }

}
