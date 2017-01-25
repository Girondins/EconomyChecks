package girondins.economycheck;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Fragmentklass för knapparna
 */
public class ButtonFragment extends Fragment {
    Button fragBtnIn;
    Button fragBtnOut;
    Button fragBtnAll;
    Button fragBtnAdd;
    Button fragBtnDe;
    Controller cont;


    public ButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        fragBtnIn = (Button)view.findViewById(R.id.incomeBtnID);
        fragBtnOut = (Button)view.findViewById(R.id.outcomeBtnID);
        fragBtnAll = (Button)view.findViewById(R.id.allBtnID);
        fragBtnAdd = (Button)view.findViewById(R.id.addID);
        fragBtnDe = (Button)view.findViewById(R.id.deID);

        fragBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.addIncome();
            }
        });

        fragBtnDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.addExpense();
            }
        });

        fragBtnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.showIncomeContent();
            }
        });

        fragBtnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.showOutcomeContent();
            }
        });

        fragBtnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cont.showAllContent();
            }
        });

        return view;
    }

    public void setController(Controller cont){
        this.cont = cont;
    }



}
