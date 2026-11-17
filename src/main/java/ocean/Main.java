package ocean;

import ocean.database.DatabaseConnection;
import ocean.service.*;
import ocean.ui.UserInterface;

import javax.swing.*;

/** Starts the app, prepares its services, and adds demo bottles once. */
public class Main {
    private static final String[][] SAMPLE_MESSAGES = {
            {"I hope you know that your quiet kindness mattered more than you could see.", "Unspoken Words"},
            {"Somewhere, someone is learning to be proud of the life they made it through.", "Secret Thoughts"},
            {"Dear future me: I hope you still make time to look at the moon.", "Random Thoughts"},
            {"I still remember the small things you did when nobody else noticed.", "Things I Never Said"},
            {"Today I forgave myself for not knowing then what I know now.", "Confessions"},
            {"I wish I could tell you that I am doing better, slowly and honestly.", "Unspoken Words"},
            {"Some endings are quiet doors opening somewhere else.", "Secret Thoughts"},
            {"To the person reading this: you have made it through every hard day so far.", "Random Thoughts"},
            {"I miss the version of us that laughed without checking the time.", "Things I Never Said"},
            {"One day I want to remember this ordinary afternoon as a happy one.", "Letters to Future Self"},
            {"I was braver than I gave myself credit for.", "Confessions"},
            {"May you find a place where you can finally exhale.", "Secret Thoughts"},
            {"The sky looked like a promise on my walk home today.", "Random Thoughts"},
            {"I hope you kept making things, even when nobody was watching.", "Letters to Future Self"},
            {"There are words I never said because your happiness mattered more than my need to be heard.", "Things I Never Said"}
    };

    public static void main(String[] args) {
        try {
            DatabaseConnection.initialize();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Could not initialize the local ocean database:\n" + e.getMessage(),
                    "Startup error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                MessageService messages = new MessageService();
                FutureLetterService letters = new FutureLetterService();
                seedSamples(messages);

                BottleService bottles = new BottleService(messages);
                StatisticsService statistics = new StatisticsService(messages, letters);
                AccountService accounts = new AccountService();
                new UserInterface(messages, bottles, letters, statistics, accounts).show();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "The ocean could not open: " + e.getMessage(),
                        "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /** Add only missing demo text so relaunches never create duplicate bottles. */
    private static void seedSamples(MessageService messages) throws Exception {
        for (String[] sample : SAMPLE_MESSAGES) {
            if (!messages.containsText(sample[0])) {
                messages.release(sample[0], sample[1]);
            }
        }
    }
}
