package practise.com.samplemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button fragment,activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment=findViewById(R.id.button_fragmap);
        activity=findViewById(R.id.button_actimap);
        activity.setOnClickListener(this);
        fragment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_fragmap:
                getSupportFragmentManager().beginTransaction().replace(R.id.Linear_main,new MapPracticeFragment()).addToBackStack(null).commit();
                break;
            case R.id.button_actimap:
                Intent i=new Intent(this,MapsActivity.class);
                startActivity(i);
                break;
        }

    }
}
