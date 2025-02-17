package com.example.nonograms;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Random;

public class Cell extends AppCompatButton {

    private boolean blackSquare = false;
    private boolean checked = false;
    private static int numBlackSquares = 0;

    public Cell(@NonNull Context context, int size) {
        super(context);

        TableRow.LayoutParams params = new TableRow.LayoutParams(size, size);
        params.setMargins(2, 2, 2, 2);
        setLayoutParams(params);

        setBackgroundResource(R.drawable.cell_selector);
        setText("");
        setEnabled(true);
        setClickable(true);

        Random random = new Random();
        blackSquare = random.nextBoolean();
        if (blackSquare) {
            numBlackSquares++;
        }
    }

    public boolean isBlackSquare() {
        return blackSquare;
    }

    public static int getNumBlackSquares() {
        return numBlackSquares;
    }

    public boolean markBlackSquare() {
        if (checked) return false;

        if (blackSquare) {
            setBackgroundColor(Color.BLACK);
            setEnabled(false);
            numBlackSquares--;
            return true;
        } else {
            toggleX();
            return false;
        }
    }

    public void toggleX() {
        if (checked) {
            setText("");
            checked = false;
        } else {
            setText("X");
            setTextColor(Color.RED);
            checked = true;
        }
    }
}
