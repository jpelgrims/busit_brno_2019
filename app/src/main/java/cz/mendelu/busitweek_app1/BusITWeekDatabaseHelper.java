package cz.mendelu.busitweek_app1;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(19);
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {
        builder.addGPSTask("first")
                .location(49.210094, 16.620026)
                .radius(100)
                .victoryPoints(10)
                .hint("Hint")
                .simplePuzzle()
                    .question("What is the best Bus IT Week?")
                    .answer("Brno")
                    .hint("Testhint")
                    .puzzleTime(3000)
                    .puzzleDone()
                .taskDone();

        builder.addGPSTask("second")
                .location(3.0, 2.0)
                .radius(100)
                .choicePuzzle()
                    .addChoice("Text", false)
                    .addChoice("text 2", true)
                    .addChoice("sfsfs", false)
                    .addChoice("dsfsdfs", false)
                    .question("question")
                    .puzzleDone()
                .taskDone();

        builder.addBeaconTask("third")
                .beacon(1, 1)
                .imageSelectPuzzle()
                    .addImage(R.drawable.img1, false)
                    .addImage(R.drawable.img2, true)
                    .addImage(R.drawable.img3, false)
                    .addImage(R.drawable.img4, false)
                    .question("Best drink ever")
                    .puzzleDone()
                .location(1.0, 1.0)
                .taskDone();



    }
}
