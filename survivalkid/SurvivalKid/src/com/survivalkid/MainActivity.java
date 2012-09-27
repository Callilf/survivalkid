package com.survivalkid;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button button0;
	TextView testView0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        button0 = (Button) findViewById(R.id.button1);
        testView0 = (TextView) findViewById(R.id.textView1);
        
        button0.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		testView0.setText("T'es un pd !");
        		testView0.setTextColor(Color.parseColor("#FF0000"));
        		testView0.setTextSize(25);
        	}
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
