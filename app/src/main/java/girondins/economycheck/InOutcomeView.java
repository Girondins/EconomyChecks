package girondins.economycheck;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Girondins on 14/09/15.
 * En ärvd Textview som sätter lägger till INCOME/EXPENSE beroende på ifall det är
 * inkomst eller utgift
 */
public class InOutcomeView extends TextView {

    public InOutcomeView(Context context) {
        super(context);
    }

    public InOutcomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InOutcomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(CharSequence text, TextView.BufferType type){
        String str = text.toString();
        if(str.contains("+")){
            str = "INCOME: " + str + "\n";
        }else
            str = "EXPENSE: " + str + "\n";

        super.setText(str,type);
    }
}
