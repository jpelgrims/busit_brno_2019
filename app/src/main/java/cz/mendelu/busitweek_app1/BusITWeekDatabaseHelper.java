package cz.mendelu.busitweek_app1;

import cz.mendelu.busItWeek.library.StoryLineDatabaseHelper;
import cz.mendelu.busItWeek.library.builder.StoryLineBuilder;

public class BusITWeekDatabaseHelper extends StoryLineDatabaseHelper {

    public BusITWeekDatabaseHelper() {
        super(110);
    }

    @Override
    protected void onCreate(StoryLineBuilder builder) {

        // See cat
        builder.addGPSTask("1")
                .radius(10)
                .location(49.215457, 16.629973)
                .taskDone();

        // Catch or lose cat
        builder.addGPSTask("2")
                .location(49.215457, 16.629973)
                .radius(10)
                .taskDone();

        // PeeTask
        builder.addGPSTask("3")
                .location(49.215457, 16.629973)
                .radius(10)
                .taskDone();

        // DigTask
        builder.addGPSTask("4")
                .location(49.215457, 16.629973)
                .radius(10)
                .taskDone();

        builder.addGPSTask("5")
                .location(49.215457, 16.629973)
                .radius(100)
                .imageSelectPuzzle()
                .addImage(R.drawable.banaanschil, true)
                .addImage(R.drawable.bird, true)
                .addImage(R.drawable.chocolade, false)
                .addImage(R.drawable.grass, true)
                .question("You found some edible stuff! What do you eat?")
                .puzzleDone()
                .taskDone();

        //BarkTask
        builder.addGPSTask("6")
                .location(49.215457, 16.629973)
                .radius(10)
                .taskDone();


    }
}
