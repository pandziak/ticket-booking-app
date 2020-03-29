package pl.pandziak.ticketbookingapp.domain.screening;

import java.util.Comparator;

final class ScreeningByTitleAndTimeComparator implements Comparator<Screening> {

    @Override
    public int compare(Screening s1, Screening s2) {
        int movieTitleCompare = s1.getMovie().compareToIgnoreCase(s2.getMovie());
        if (movieTitleCompare != 0) {
            return movieTitleCompare;
        } else {
            return s1.getScreeningTime().compareTo(s2.getScreeningTime());
        }
    }

}
