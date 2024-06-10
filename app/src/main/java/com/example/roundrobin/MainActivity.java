package com.example.roundrobin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<String> teams = new ArrayList<>();
    private final List<EditText> teamInputs = new ArrayList<>();
    private TextView scheduleOutput;
    private LinearLayout inputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLayout = findViewById(R.id.input_layout);

        teamInputs.add(findViewById(R.id.team1_input));
        teamInputs.add(findViewById(R.id.team2_input));
        teamInputs.add(findViewById(R.id.team3_input));
        teamInputs.add(findViewById(R.id.team4_input));
        teamInputs.add(findViewById(R.id.team5_input));
        teamInputs.add(findViewById(R.id.team6_input));

        scheduleOutput = findViewById(R.id.schedule_output);

        Button generateButton = findViewById(R.id.generate_button);
        Button additionalButton = findViewById(R.id.additional_button);
        generateButton.setOnClickListener(v -> scheduler());
        additionalButton.setOnClickListener(v -> addAdditionalInput());
    }

    private void addAdditionalInput() {
        EditText newTeamInput = new EditText(this);
        newTeamInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newTeamInput.setHint("New Team");
        newTeamInput.setTextSize(14);
        inputLayout.addView(newTeamInput);
        teamInputs.add(newTeamInput);
    }

    public void scheduler() {
        teams.clear();

        for (EditText teamInput : teamInputs) {
            String team = teamInput.getText().toString();
            if (!team.isEmpty()) {
                teams.add(team);
            }
        }

        int numberOfTeams = teams.size();
        String[] teamTable;
        if (numberOfTeams % 2 == 0) {
            teamTable = new String[numberOfTeams - 1];
            for (int j = 0; j < numberOfTeams - 1; j++) {
                teamTable[j] = teams.get(j + 1);
            }
        } else {
            teamTable = new String[numberOfTeams];
            for (int j = 0; j < numberOfTeams - 1; j++) {
                teamTable[j] = teams.get(j + 1);
            }
            teamTable[numberOfTeams - 1] = "Bye";
        }

        StringBuilder schedule = getStringBuilder(teamTable);

        scheduleOutput.setText(schedule.toString());
        scheduleOutput.setMovementMethod(new ScrollingMovementMethod());
    }

    @NonNull
    private StringBuilder getStringBuilder(String[] teamTable) {
        int teamsSize = teamTable.length;
        int numOfMatches = (teamsSize + 1) / 2;
        int count = 0;

        StringBuilder schedule = new StringBuilder();
        for (int week = teamsSize - 1; week >= 0; week--) {
            schedule.append("Week ").append(++count).append("\r\n");
            int teamIndex = week % teamsSize;
            if (!teamTable[teamIndex].equals("Bye")) {
                schedule.append(teams.get(0)).append(" vs. ").append(teamTable[teamIndex]).append("\r\n");
            }
            for (int i = 1; i < numOfMatches; i++) {
                int firstTeam = (week + i) % teamsSize;
                int secondTeam = (week + teamsSize - i) % teamsSize;
                if (!teamTable[firstTeam].equals("Bye") && !teamTable[secondTeam].equals("Bye")) {
                    schedule.append(teamTable[firstTeam]).append(" vs. ").append(teamTable[secondTeam]).append("\r\n");
                }
            }
            schedule.append("\r\n");
        }
        return schedule;
    }
}
