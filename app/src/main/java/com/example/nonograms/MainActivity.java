package com.example.nonograms;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int life = 3;
    private TextView lifeTextView;
    private boolean toggleXMode = false;
    private Cell[][] cells = new Cell[5][5];
    private List<List<TextView>> rowHints = new ArrayList<>();
    private List<List<TextView>> colHints = new ArrayList<>();

    private int hintCellsize = 60;
    private int gameCellsize = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        lifeTextView = findViewById(R.id.lifeTextView);
        updateLifeText();

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> toggleXMode = isChecked);

        for (int i = 0; i < 8; i++) {
            TableRow tableRow = new TableRow(this);

            if (i < 3) {//열 힌트 생성
                for (int j = 0; j < 8; j++) {
                    if (j >= 3) {
                        if (colHints.size() <= j - 3) {
                            colHints.add(new ArrayList<>());
                        }
                        TextView hintCell = new TextView(this);
                        hintCell.setGravity(Gravity.CENTER);
                        hintCell.setTextSize(18);
                        hintCell.setTextColor(getResources().getColor(android.R.color.black));
                        TableRow.LayoutParams params = new TableRow.LayoutParams(hintCellsize, hintCellsize);
                        params.setMargins(1, 1, 1, 1);
                        hintCell.setLayoutParams(params);
                        hintCell.setText("");
                        colHints.get(j - 3).add(0, hintCell);
                        tableRow.addView(hintCell);
                    } else {
                        TextView empty = new TextView(this);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(hintCellsize, hintCellsize);
                        params.setMargins(1, 1, 1, 1);
                        empty.setLayoutParams(params);
                        tableRow.addView(empty);
                    }
                }
            } else {// 행 힌트 + 게임 셀 생성
                for (int j = 0; j < 8; j++) {
                    if (j < 3) {
                        if (rowHints.size() <= i - 3) {
                            rowHints.add(new ArrayList<>());
                        }
                        TextView hintCell = new TextView(this);
                        hintCell.setGravity(Gravity.CENTER);
                        hintCell.setTextSize(18);
                        hintCell.setTextColor(getResources().getColor(android.R.color.black));
                        TableRow.LayoutParams params = new TableRow.LayoutParams(hintCellsize, hintCellsize);
                        params.setMargins(1, 1, 1, 1);
                        hintCell.setLayoutParams(params);
                        hintCell.setText("");
                        rowHints.get(i - 3).add(0, hintCell);
                        tableRow.addView(hintCell);
                    } else {// 게임 셀 생성
                        cells[i - 3][j - 3] = new Cell(this, gameCellsize);
                        final int row = i - 3;
                        final int col = j - 3;
                        cells[row][col].setOnClickListener(v -> onCellClick(row, col));
                        TableRow.LayoutParams params = new TableRow.LayoutParams(gameCellsize, gameCellsize);
                        params.setMargins(1, 1, 1, 1);
                        cells[i - 3][j - 3].setLayoutParams(params);
                        tableRow.addView(cells[i - 3][j - 3]);
                    }
                }
            }

            tableLayout.addView(tableRow);
        }
        updateHints();
    }

    private void updateHints() {// 행 힌트
        for (int i = 0; i < 5; i++) {
            int count = 0;
            List<Integer> hintNumbers = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                if (cells[i][j].isBlackSquare()) {
                    count++;
                } else if (count > 0) {
                    hintNumbers.add(count);
                    count = 0;
                }
            }
            if (count > 0) {
                hintNumbers.add(count);
            }

            List<TextView> rowHintCells = rowHints.get(i);
            for (int k = 0; k < rowHintCells.size(); k++) {
                if (k < hintNumbers.size()) {
                    rowHintCells.get(k).setText(String.valueOf(hintNumbers.get(hintNumbers.size() - 1 - k)));
                } else {
                    rowHintCells.get(k).setText("");
                }
            }
        }
        for (int j = 0; j < 5; j++) {// 열 힌트
            int count = 0;
            List<Integer> hintNumbers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                if (cells[i][j].isBlackSquare()) {
                    count++;
                } else if (count > 0) {
                    hintNumbers.add(count);
                    count = 0;
                }
            }
            if (count > 0) {
                hintNumbers.add(count);
            }

            List<TextView> colHintCells = colHints.get(j);
            for (int k = 0; k < colHintCells.size(); k++) {
                if (k < hintNumbers.size()) {
                    colHintCells.get(k).setText(String.valueOf(hintNumbers.get(hintNumbers.size() - 1 - k)));
                } else {
                    colHintCells.get(k).setText("");
                }
            }
        }
    }


    private void onCellClick(int row, int col) {
        Cell cell = cells[row][col];
        if (toggleXMode) {
            cell.toggleX();
        } else {
            boolean isBlack = cell.markBlackSquare();
            if (!isBlack) {
                life--;
                updateLifeText();
                if (life <= 0) {
                    endGame(false);
                }
            } else if (Cell.getNumBlackSquares() == 0) {
                endGame(true);
            }
        }
    }

    private void updateLifeText() {
        lifeTextView.setText("Life: " + life);
    }

    private void endGame(boolean isWin) {
        String message = isWin ? "You Win!" : "Game Over!";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        for (Cell[] row : cells) {
            for (Cell cell : row) {
                cell.setEnabled(false);
            }
        }
    }
}