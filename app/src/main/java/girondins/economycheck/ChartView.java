package girondins.economycheck;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;


/**
 * Activiyklass som visar diagram
 */
public class ChartView extends Activity {
    private BarChart barChartAll,barChartIncome,barChartExpense;
    private RelativeLayout all;
    private String[] allName = {"Income","Expense"}
                    ,incName = {"Salary","Other"}
                    ,expName = {"Home","Ent","Food","Other"};
    private int[]   incCost = new int[2],
                    expCost = new int[4];
    private Input[] incomeIn;
    private Input[] outcomeIn;
    private Button backBtn;
    private Button showAll;
    private Button showInc;
    private Button showExp;
    private TextView txt;
    private int incomeSum = 0;
    private int expenseSum = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        all = (RelativeLayout)findViewById(R.id.allChartID);
        showAll = (Button)findViewById(R.id.allBtnID);
        showInc = (Button)findViewById(R.id.incButID);
        showExp = (Button)findViewById(R.id.expBtnID);
        backBtn = (Button)findViewById(R.id.backButtonID);
        txt = (TextView)findViewById(R.id.taskID);
        showAll.setOnClickListener(new allList());
        showInc.setOnClickListener(new incList());
        showExp.setOnClickListener(new expList());
        backBtn.setOnClickListener(new backListener());
        Intent i = getIntent();
        incomeIn = (Input[])i.getSerializableExtra("inIn");
        outcomeIn = (Input[])i.getSerializableExtra("outIn");
        txt.setText("ALL");

        barChartIncome = new BarChart(this);
        barChartExpense = new BarChart(this);
        barChartAll = new BarChart(this);

        initIncData(incomeIn);
        initExpData(outcomeIn);
        initAllData();

    }

    public void initIncData(Input[] incomein){
        int salarySum = 0, otherSum = 0;
        String input ="";
        for(int i = 0; i<incomein.length; i++){
            input = incomein[i].getInput().substring(1);
            Integer.parseInt(input);
            if(incomein[i].getCategory().equals("Salary")){
                salarySum = salarySum + Integer.parseInt(input);
            }
            if(incomein[i].getCategory().equals("Other")){
                otherSum = otherSum + Integer.parseInt(input);
            }
        }
        incCost[0] = salarySum;
        incCost[1] = otherSum;

        incomeSum = salarySum + otherSum;

        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i = 0 ; i<incCost.length; i++)
            values.add(new BarEntry(incCost[i],i));

        ArrayList<String> valuesName = new ArrayList<String>();
        for(int i = 0 ; i<incName.length; i++)
            valuesName.add(incName[i]);

        BarDataSet dataset = new BarDataSet(values,"");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(valuesName,dataset);
        barChartIncome.setData(data);


    }

    public void initExpData(Input[] outcomein){
        int homeSum = 0,entSum = 0,otherSum = 0,foodSum = 0;
        String input ="";
        for(int i = 0; i<outcomein.length; i++){
            input = outcomein[i].getInput().substring(2);
            Integer.parseInt(input);
            if(outcomein[i].getCategory().equals("Home")){
                homeSum = homeSum + Integer.parseInt(input);
            }

            if(outcomein[i].getCategory().equals("Entertainment")){
                entSum = entSum + Integer.parseInt(input);
            }

            if(outcomein[i].getCategory().equals("Food")){
                foodSum = foodSum + Integer.parseInt(input);
            }

            if(outcomein[i].getCategory().equals("Other")){
                otherSum = otherSum + Integer.parseInt(input);
            }

        }
        expCost[0] = homeSum;
        expCost[1] = entSum;
        expCost[2] = foodSum;
        expCost[3] = otherSum;

        expenseSum = homeSum + entSum + foodSum + otherSum;

        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i = 0 ; i<expCost.length; i++)
            values.add(new BarEntry(expCost[i],i));

        ArrayList<String> valuesName = new ArrayList<String>();
        for(int i = 0 ; i<expName.length; i++)
            valuesName.add(expName[i]);

        BarDataSet dataset = new BarDataSet(values,"");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(valuesName,dataset);
        barChartExpense.setData(data);


    }

    public void initAllData(){
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(incomeSum,0));
        values.add(new BarEntry(expenseSum,1));
        ArrayList<String> valuesName = new ArrayList<String>();
        for(int i = 0 ; i<allName.length; i++)
            valuesName.add(allName[i]);

        BarDataSet dataset = new BarDataSet(values,"");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(valuesName,dataset);
        barChartAll.setData(data);
    }

    /**
     * Inre klass som startar första sidan dvs. går tillbaka
     */
    private class backListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent FirstPage = new Intent(ChartView.this, girondins.economycheck.FirstPage.class);
            startActivity(FirstPage);
        }
    }

    private class allList implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            all.removeAllViews();
            all.addView(barChartAll);
            txt.setText("ALL");
        }
    }

    private class incList implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            all.removeAllViews();
            all.addView(barChartIncome);
            txt.setText("INCOME");
        }
    }

    private class expList implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            all.removeAllViews();
            all.addView(barChartExpense);
            txt.setText("EXPENSE");
        }
    }
}
