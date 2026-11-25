import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MeetingPlanner {

    // Pairs a date with the number of calendars where it is available
    private static class DayScore {
        private final Day day;
        private int availableCount;

        DayScore(Day day) {
            this.day = day;
            this.availableCount = 0;
        }
    }

    // Returns a ranked string of the best days for the group to meet
    public static List<String> rankBestDays(List<Calender> calendars) {
        List<String> results = new ArrayList<>();

        if (calendars == null || calendars.isEmpty()) {
            results.add("No calendars were provided.");
            return results;
        }

        ArrayList<DayScore> scores = new ArrayList<>();

        for (Calender cal : calendars) {
            for (Day d : cal.getDays()) {
                if (findScore(scores, d) == null) {
                    scores.add(new DayScore(d));
                }
            }
        }

        for (DayScore score : scores) {
            for (Calender cal : calendars) {
                if (isAvailableIn(cal, score.day)) {
                    score.availableCount++;
                }
            }
        }

        scores.removeIf(s -> s.availableCount == 0);

        if (scores.isEmpty()) {
            results.add("No day is available for anyone in the group.");
            return results;
        }

        Collections.sort(scores, (a, b) -> {
            if (a.availableCount != b.availableCount) {
                return b.availableCount - a.availableCount;
            }
            return compareDates(a.day, b.day);
        });

        int total = calendars.size();
        results.add("Best days to meet (" + total + " people):");

        for (int i = 0; i < scores.size(); i++) {
            DayScore s = scores.get(i);
            results.add((i + 1) + ". "
                    + s.day.getMonth() + "/"
                    + s.day.getDay() + "/"
                    + s.day.getYear()
                    + " - " + s.availableCount + " of " + total
                    + " available");
        }

        return results;
    }


    // Returns the DayScore matching the date, or null if none
    private static DayScore findScore(List<DayScore> scores, Day date) {
        for (DayScore s : scores) {
            if (s.day.equals(date)) {
                return s;
            }
        }
        return null;
    }

    // True if the calendar contains this date and it is marked available
    private static boolean isAvailableIn(Calender cal, Day date) {
        for (Day d : cal.getDays()) {
            if (d.equals(date)) {
                return d.isAvailable();
            }
        }
        return false;
    }

    // Negative if a comes before b, positive if after, zero if the same
    private static int compareDates(Day a, Day b) {
        if (a.getYear() != b.getYear()) {
            return a.getYear() - b.getYear();
        }
        if (a.getMonth() != b.getMonth()) {
            return a.getMonth() - b.getMonth();
        }
        return a.getDay() - b.getDay();
    }
}
