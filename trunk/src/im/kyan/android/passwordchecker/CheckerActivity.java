/*
 * -- CheckerActivity.java --
 * 
 * Copyright 2011, Kyan He <kyan.ql.he@gmail.com>
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Modified:
 * Kyan He <kyan.ql.he@gmail.com> @ Apr 8, 2011
 */
package im.kyan.android.passwordchecker;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class CheckerActivity extends Activity implements TextWatcher {
    
    private static final String TAG = "Checker";
    
    private TextView mstrView;
    
    private final double ONE_SECOND = 1;
    private final double ONE_MINUTE = ONE_SECOND * 60;
    private final double ONE_HOUR = ONE_MINUTE * 60;
    private final double ONE_DAY = ONE_HOUR * 24;
    private final double ONE_YEAR = ONE_DAY * 365.25;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((EditText) findViewById(R.id.input)).addTextChangedListener(this);
        
        mstrView = (TextView) findViewById(R.id.result);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    public void afterTextChanged(Editable arg0) {
        
        String password = arg0.toString();
        
        // show about when no password input
        if (TextUtils.isEmpty(password)) {
            mstrView.setText(R.string.cc);
        }
        // calculate password strength
        else {
            mstrView.setText(getReadablePasswordStrength(password));
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
     * int, int, int)
     */
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
            int arg3) {
        // @see afterTextChanged()
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
     * int, int)
     */
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        // @see afterTextChanged()
    }
    
    public SpannableString getReadablePasswordStrength(String password) {
        
        double strength = CrackEngine.calculatePasswordStrength(password);
        
        String str = "";
        // hit dictionary
        if (strength < 0) {
            str = getString(R.string.result_dict_fmt);
        }
        // less than 1 ns
        else if (strength < 0.00001) {
            str = getString(R.string.result_instant_fmt);
        }
        // less than 1 s
        else if (strength < ONE_SECOND) {
            str = getString(
                    R.string.result_common_fmt,
                    String.format("%9f ", strength)
                            + getResources().getQuantityString(
                                    R.plurals.second, 1));
        }
        // R.string.str_common_fmt
        else {
            int resDimension;
            
            // less than 1 min
            if (strength < ONE_MINUTE) {
                resDimension = R.plurals.second;
            }
            // less than 1 hour
            else if (strength < ONE_HOUR) {
                resDimension = R.plurals.minute;
                strength /= ONE_MINUTE;
            }
            // less than 1 day
            else if (strength < ONE_DAY) {
                resDimension = R.plurals.hour;
                strength /= ONE_HOUR;
            }
            // less than 1 year
            else if (strength < ONE_YEAR) {
                resDimension = R.plurals.day;
                strength /= ONE_DAY;
            }
            // more than 1 year
            else {
                resDimension = R.plurals.year;
                strength /= ONE_YEAR;
            }
            
            long s = Math.round(strength);
            String fmt = "";
            
            // prefix
            if (s > 1) {
                fmt += getString(R.string.about) + " ";
            }
            
            // quality
            fmt += s;
            
            // dimension
            fmt += " "
                    + getResources().getQuantityString(resDimension, (int) s);
            str = getString(R.string.result_common_fmt, fmt);
        }
        
        Log.d(TAG, "fmt: " + str);
        
        int start = str.indexOf('`');
        // because we will remove '`'
        int end = str.lastIndexOf('`') - 1;
        SpannableString spanable = new SpannableString(str.replace("`", ""));
        
        spanable.setSpan(new RelativeSizeSpan(1.6f), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        return spanable;
    }
}