package com.nezip.bizcard1;

import java.text.DecimalFormat;
 
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;
 
public class CustomTextWathcer implements TextWatcher {
    @SuppressWarnings("unused")
    private EditText mEditText;
    String strAmount = ""; // 임시저장값 (콤마)
 
    public CustomTextWathcer(EditText e) {
        mEditText = e;
    }
 
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
 
    }
 
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!s.toString().equals(strAmount)) { // StackOverflow를 막기위해,
            strAmount = makeStringComma(s.toString().replaceAll(",", ""));
            mEditText.setText(strAmount);
            Editable e = mEditText.getText();
            Selection.setSelection(e, strAmount.length());
        }
    }
 
    public static String makeStringComma(String str) {
        if (str.length() == 0)
            return "";
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(value);
    }
 
}
