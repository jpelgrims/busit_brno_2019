package cz.mendelu.busitweek_app1;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(29);
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {
        /*
        builder.addGPSTask("1")
                .location(49.20968,16.6145)
                .radius(1000)
                .victoryPoints(10)
                .hint("Hint")
                .simplePuzzle()
                    .question("What is the best Bus IT Week?")
                    .answer("Brno")
                    .hint("Testhint")
                    .puzzleTime(3000)
                    .puzzleDone()
                .taskDone();
*/
        builder.addGPSTask("2")
                .location(49.20968,16.6145)
                .radius(100)
                .imageSelectPuzzle()
                    .addImage(R.drawable.banaanschil, true)
                    .addImage(R.drawable.bird, true)
                    .addImage(R.drawable.chocolade, false)
                    .addImage(R.drawable.grass, true)
                    .question("As if your day couldn't get any better... The wind knocked over a garbage can and the ground is covered with mouth-watering snacks.What will you eat?")
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
