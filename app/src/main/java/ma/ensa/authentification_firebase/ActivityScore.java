package ma.ensa.authentification_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityScore extends AppCompatActivity {

    TextView msg , score;
    float res =0.0f;
    ImageView icon;

    @SuppressLint("Missing InflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        msg = findViewById(R.id.msg);
        score =findViewById(R.id.score);
        icon=findViewById(R.id.imageView);

        icon.setImageResource(R.drawable.good);
        Intent it = getIntent();
        if(it != null)
        {
            res = it.getFloatExtra("score",0);
            if(res<5)
            {
                icon.setImageResource(R.drawable.bad);
                msg.setText("Attention !!!!");
            }

            score.setText("Votre Score est : "+res+"/10 ");
        }

    }
}