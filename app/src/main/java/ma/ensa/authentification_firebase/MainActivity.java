package ma.ensa.authentification_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView textView;
    Button button;
    DatabaseReference ref;

    TextView num_output, quest_output;
    Button ch1, ch2, ch3, suivant,ajouter;

    List<Question> liste=new ArrayList<>();
    int indice =0;
    float score =0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    textView = findViewById(R.id.user_details);
    button = findViewById(R.id.logout);
    ////Quiz Start Code here ...
    num_output = findViewById(R.id.number_qst_output);
    quest_output=findViewById(R.id.quest_output);
    ch1=findViewById(R.id.ch1);
    ch2=findViewById(R.id.ch2);
    ch3=findViewById(R.id.ch3);
    suivant=findViewById(R.id.suivant);
      ajouter=findViewById(R.id.ajouter);

   // rempliriListe();
       getAll();
    chargerQuestion();
    chargerNumerQuestion();

    ajouter.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AjouterQuestion();
        }
    });


        ch1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verfierReponse(indice,1);
            if(indice < (liste.size() -1))
            {
                indice++;
                chargerQuestion();
                chargerNumerQuestion();
            }else
            {
                Toast.makeText(getApplicationContext(),"Fin de test Score = "+score,Toast.LENGTH_LONG).show();
                Intent it = new Intent(MainActivity.this,ActivityScore.class);
                it.putExtra("score",score);
                startActivity(it);
            }
        }
    });
        ch2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verfierReponse(indice,2);
            if(indice < (liste.size() -1))
            {
                indice++;
                chargerQuestion();
                chargerNumerQuestion();
            }else
            {
                Toast.makeText(getApplicationContext(),"Fin de test Score = "+score,Toast.LENGTH_LONG).show();
                Intent it = new Intent(MainActivity.this,ActivityScore.class);
                it.putExtra("score",score);
                startActivity(it);
            }
        }
    });
        ch3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verfierReponse(indice,3);
            if(indice < (liste.size() -1))
            {
                indice++;
                chargerQuestion();
                chargerNumerQuestion();
            }else
            {
                Toast.makeText(getApplicationContext(),"Fin de test Score = "+score,Toast.LENGTH_LONG).show();
                Intent it = new Intent(MainActivity.this,ActivityScore.class);
                it.putExtra("score",score);
                startActivity(it);
            }
        }
    });

        suivant.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            verfierReponse(indice,-1);
            if(indice < (liste.size() -1))
            {
                indice++;
                chargerQuestion();
                chargerNumerQuestion();
            }else
            {
                Toast.makeText(getApplicationContext(),"Fin de test Score = "+score,Toast.LENGTH_LONG).show();
                Intent it = new Intent(MainActivity.this,ActivityScore.class);
                it.putExtra("score",score);
                startActivity(it);
            }
        }
    });


    ////Quiz End Code here ...
        if(user == null)
    {
        Intent intent = new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        finish();
    }
        else {
        textView.setText(user.getEmail());
    }


        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(),login.class);
            startActivity(intent);
            finish();
        }
    });


}

    void rempliriListe()
    {
        liste.add(new Question("php ?","langage web","methode de conception","programmation mobile",1));
        liste.add(new Question("Android ?","langage web","OS et telephone","programmation mobile",2));
        liste.add(new Question("JAVA ?","langage de programmation OO","methode de conception","systeme d'expoitation",1));
        liste.add(new Question("BDD ?","langage web","methode de conception","ensemble de donnees structure",3));
        liste.add(new Question("Spring ?","Framework JEE","methode de conception","programmation mobile",1));

    }


    void chargerQuestion(){
        if(liste.size()>0){
            quest_output.setText(liste.get(indice).getQues());
            ch1.setText(liste.get(indice).getChoix1());
            ch2.setText(liste.get(indice).getChoix2());
            ch3.setText(liste.get(indice).getChoix3());}
    }

    void chargerNumerQuestion()
    {
        num_output.setText("Question "+ (indice+1)+" / "+liste.size());
    }

    void verfierReponse(int ind,int choixuser)
    {
        Question q = liste.get(ind);
        if(q != null)
            if(q.getReponse() == choixuser)
                score =score+2;
    }

    void AjouterQuestion(){
        int i=1;
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://authentification-firebas-ccd16-default-rtdb.firebaseio.com/");
        for(Question q:liste){
            ref.child("Questions/"+i).setValue(q);
            Toast.makeText(MainActivity.this,"Insertion ok",Toast.LENGTH_SHORT).show();
        i++;
        }
    }


    public void getAll(){
        ref= FirebaseDatabase.getInstance().getReferenceFromUrl("https://authentification-firebas-ccd16-default-rtdb.firebaseio.com/Questions/");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    String choix1 = questionSnapshot.child("choix1").getValue(String.class);
                    String choix2 = questionSnapshot.child("choix2").getValue(String.class);
                    String choix3 = questionSnapshot.child("choix3").getValue(String.class);
                    String ques = questionSnapshot.child("ques").getValue(String.class);
                    int reponse = questionSnapshot.child("reponse").getValue(int.class);

                    liste.add(new Question(ques,choix1,choix2,choix3,reponse));

                }
                chargerQuestion();
                chargerNumerQuestion();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}