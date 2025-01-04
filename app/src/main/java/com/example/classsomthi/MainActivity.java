package com.example.classsomthi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore;
    private LinearLayout llOptions;
    private Button btnChangeOptions;

    private int correctAnswer; // התשובה הנכונה
    private int numOptions = 4; // ברירת מחדל של כמות האפשרויות
    private int score = 0; // מס תשובות נכונות
    private int totalQuestions = 0; // סך כל השאלות
    private Random random = new Random(); // אובייקט רנדומליות

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // אתחול רכיבים מה-XML
        tvQuestion = findViewById(R.id.tv_question);
        tvScore = findViewById(R.id.tv_score);
        llOptions = findViewById(R.id.ll_options);
        btnChangeOptions = findViewById(R.id.btn_change_options);

        // התחלת השאלה הראשונה
        startNewQuestion();

        // כפתור לשינוי כמות האפשרויות
        btnChangeOptions.setOnClickListener(v -> showOptionsDialog());
    }

    // פונקציה שמתחילה שאלה חדשה
    private void startNewQuestion() {
        llOptions.removeAllViews(); // מנקה את האפשרויות הקודמות

        // יצירת שאלה רנדומלית בהתאם לפעולה
        int operation = random.nextInt(6); // פעולה רנדומלית (0-5)
        int a, b;

        switch (operation) {
            case 0: // חיבור
                a = random.nextInt(100) + 1;
                b = random.nextInt(100) + 1;
                correctAnswer = a + b;
                tvQuestion.setText(a + " + " + b);
                break;
            case 1: // חיסור
                a = random.nextInt(100) + 1;
                b = random.nextInt(a) + 1; // להבטיח שהתוצאה לא שלילית
                correctAnswer = a - b;
                tvQuestion.setText(a + " - " + b);
                break;
            case 2: // כפל
                a = random.nextInt(20) + 1;
                b = random.nextInt(10) + 1;
                correctAnswer = a * b;
                tvQuestion.setText(a + " * " + b);
                break;
            case 3: // חילוק
                b = random.nextInt(9) + 1; // מחלק (1-9)
                correctAnswer = (random.nextInt(10) + 1) * b;
                a = correctAnswer; // מחושב כך שהתוצאה תהיה שלמה
                tvQuestion.setText(a + " / " + b);
                break;
            case 4: // חזקה
                a = random.nextInt(10) + 1; // בסיס (1-10)
                b = random.nextInt(3) + 1;  // מעריך (1-3)
                correctAnswer = (int) Math.pow(a, b);
                tvQuestion.setText(a + " ^ " + b);
                break;
            case 5: // שורש ריבועי
                correctAnswer = random.nextInt(20) + 1; // שורש שלם (1-20)
                a = correctAnswer * correctAnswer;
                tvQuestion.setText("√" + a);
                break;
        }

        // יצירת אפשרויות תשובה
        List<Integer> options = generateOptions(correctAnswer, numOptions);
        for (int option : options) {
            Button btnOption = new Button(this);
            btnOption.setText(String.valueOf(option));
            btnOption.setOnClickListener(v -> checkAnswer(option)); // מאזין ללחיצה
            llOptions.addView(btnOption); // הוספת הכפתור ל-LinearLayout
        }
    }

    // פונקציה שיוצרת רשימה של אפשרויות תשובה עם אחת נכונה
    private List<Integer> generateOptions(int correctAnswer, int numOptions) {
        List<Integer> options = new ArrayList<>();
        options.add(correctAnswer); // מוסיף את התשובה הנכונה

        while (options.size() < numOptions) {
            int fakeAnswer = random.nextInt(500) + 1; // מספר רנדומלי (1-500)
            if (!options.contains(fakeAnswer)) {
                options.add(fakeAnswer); // מוסיף רק אם לא קיים
            }
        }
        Collections.shuffle(options); // מערבב את הרשימה
        return options;
    }

    // פונקציה לבדיקת תשובה בלחיצה על כפתור
    private void checkAnswer(int userAnswer) {
        totalQuestions++; // עדכון סך כל השאלות
        if (userAnswer == correctAnswer) {
            score++; // עדכון ניקוד אם התשובה נכונה
            Toast.makeText(this, "תשובה נכונה!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "תשובה שגויה! התשובה הנכונה היא: " + correctAnswer, Toast.LENGTH_LONG).show();
        }

        // עדכון צג הניקוד
        tvScore.setText("ניקוד: " + score + "/" + totalQuestions);

        // מעבר לשאלה הבאה
        startNewQuestion();
    }

    // דיאלוג לשינוי מספר האפשרויות
    private void showOptionsDialog() {
        String[] options = {"3", "4", "5", "6", "7"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר כמות אפשרויות תשובה:");
        builder.setItems(options, (dialog, which) -> {
            numOptions = Integer.parseInt(options[which]);
            startNewQuestion(); // אתחול מחדש עם מספר אפשרויות חדש
        });
        builder.show();
    }
}
