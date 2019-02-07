package cz.mendelu.busitweek_app1;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(46);
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
                .taskDone();

        /*builder.addGPSTask("2")
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

        /*builder.addBeaconTask("3")
                .beacon(1, 1)
                .imageSelectPuzzle()
                    .addImage(R.drawable.img1, false)
                    .addImage(R.drawable.img2, true)
                    .addImage(R.drawable.img3, false)
                    .addImage(R.drawable.img4, false)
                    .question("Best drink ever")
                    .puzzleDone()
                .location(1.0, 1.0)
                .taskDone();*/



    }
}
