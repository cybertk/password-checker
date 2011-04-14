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
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CheckerActivity extends Activity {
    
    private static final String TAG = "Checker";
    
    private TextView mResultView;
    
    private static final double ONE_SECOND = 1;
    private static final double ONE_MINUTE = ONE_SECOND * 60;
    private static final double ONE_HOUR = ONE_MINUTE * 60;
    private static final double ONE_DAY = ONE_HOUR * 24;
    private static final double ONE_YEAR = ONE_DAY * 365.25;
    
    // exasecond, @see http://en.wikipedia.org/wiki/Time
    private static final double AGE_OF_UNIVERSE = ONE_YEAR * 2E9;
    
    // FAQ clickable text
    private SpannableString mFaqSpan;
    
    // token used to locate a spanable part of string
    private String mSpanToken = "`";
    
    // quick-response text watcher
    private TextWatcher mInputTextWatcher = new TextWatcher() {
        
        public void afterTextChanged(Editable arg0) {
            
            String password = arg0.toString();
            
            // show about when no password input
            if (TextUtils.isEmpty(password)) {
                showFaqInfo();
            }
            // calculate password strength
            else {
                mResultView.setText(getReadablePasswordStrength(password));
            }
        }
        
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
            // @see afterTextChanged()
        }
        
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
            // @see afterTextChanged()
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        ((EditText) findViewById(R.id.input))
                .addTextChangedListener(mInputTextWatcher);
        
        mResultView = (TextView) findViewById(R.id.result);
        
        linkfyFaqInfo();
        showFaqInfo();
    }
    
    // private int processSpanToken(String s) {
    //
    // int index = s.indexOf(mSpanToken);
    //
    // s.replace(mSpanToken, replacement)
    // return 1;
    // }
    
    /**
     * make the FAQ information clickable
     */
    private void linkfyFaqInfo() {
        // ClickableSpan c = new
        
        String faq = getString(R.string.cc);
        mFaqSpan = new SpannableString(faq);
        
        int start = 0;
        int end = 0;
        
        String[] links = { FaqActivity.ACTION_SAFE,
                FaqActivity.ACTION_ACCURATE, FaqActivity.ACTION_HOWTO,
                FaqActivity.ACTION_SECURE_PASSWORD };
        for (String s : links) {
            start = end;
            end = faq.indexOf("\n", start) + 1;
            mFaqSpan.setSpan(new FaqSpan(s), start, end - 1, 0);
        }
    }
    
    /**
     * display FAQ information at mResultView
     */
    private void showFaqInfo() {
        mResultView.setText(mFaqSpan);
        // make our URLSpans work
        mResultView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    private SpannableString getReadablePasswordStrength(String password) {
        
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
            // we use '`' as a tag for locating
            str = getString(
                    R.string.result_common_fmt,
                    String.format("`%9f` ", strength)
                            + getResources().getQuantityString(
                                    R.plurals.second, 1));
        }
        // more than the age of universe
        else if (strength > AGE_OF_UNIVERSE) {
            str = getString(R.string.result_never_fmt);
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
            // normal condition, between 1 and age_of_universe years
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
            
            // quality, we use '`' as a tag for locating
            fmt += mSpanToken + getReadableInteger(s) + mSpanToken;
            
            // dimension
            fmt += " "
                    + getResources().getQuantityString(resDimension, (int) s);
            str = getString(R.string.result_common_fmt, fmt);
        }
        
        Log.d(TAG, "fmt: " + str);
        
        int start = str.indexOf('`');
        // because we will remove '`'
        int end = str.lastIndexOf('`') - 1;
        SpannableString spanable = new SpannableString(str.replace(mSpanToken,
                ""));
        
        spanable.setSpan(new RelativeSizeSpan(1.6f), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        
        return spanable;
    }
    
    /**
     * split a long integer with ","
     * 
     * @param l
     *            integer be split
     * @return "," separated string
     */
    private static String getReadableInteger(long l) {
        
        String integer = Long.toString(l);
        int sz = integer.length();
        int i = sz % 3;
        
        String str = integer.substring(0, (i == 0) ? 3 : i);
        while (i < sz) {
            
            str += "," + integer.substring(i, i + 3);
            i += 3;
        }
        
        return str;
    }
    
    class FaqSpan extends ClickableSpan {
        
        private String mAction;
        
        public FaqSpan(String action) {
            mAction = action;
        }
        
        @Override
        public void onClick(View widget) {
            final Intent intent = new Intent(getApplicationContext(),
                    FaqActivity.class);
            intent.setAction(mAction);
            
            startActivity(intent);
        }
        
    }
}