package cz.mendelu.busitweek_app1;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(65);
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {

        builder.addBeaconTask("1")
                .beacon(249, 61625)
                .location(49.209827, 16.614823)
                .taskDone();

        builder.addGPSTask("2")
                .location(49.209827, 16.614823)
                .radius(100)
                .taskDone();
/*
        builder.addGPSTask("3")
                .location(49.209827, 16.614823)
                .radius(100)
                .victoryPoints(10)
                .hint("Hint")
                .simplePuzzle()
                    .question("What is the best Bus IT Week?")
                    .answer("Brno")
                    .hint("Testhint")
                    .puzzleTime(3000)
                    .puzzleDone()
                .taskDone();*/

        builder.addGPSTask("4")
                .location(49.20968,16.6145)
                .radius(100)
                .imageSelectPuzzle()
                .addImage(R.drawable.banaanschil, true)
                .addImage(R.drawable.bird, true)
                .addImage(R.drawable.chocolade, false)
                .addImage(R.drawable.grass, true)
                .question("Fallen garbage can, choose something to eat")
                .puzzleDone()
                .taskDone();


    }
}
