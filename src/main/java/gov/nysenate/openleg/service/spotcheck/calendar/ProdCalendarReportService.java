package gov.nysenate.openleg.service.spotcheck.calendar;

import gov.nysenate.openleg.model.calendar.Calendar;
import gov.nysenate.openleg.model.calendar.CalendarId;
import gov.nysenate.openleg.service.calendar.data.ProdCalendarDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdCalendarReportService extends BaseCalendarReportService {

    @Autowired
    private ProdCalendarDataService dataService;

    @Override
    protected String getReportNotes() {
        return "1.9.2";
    }

    @Override
    protected void markAsChecked(CalendarId id) {
    }

    @Override
    protected List<Calendar> getReferences(LocalDateTime start, LocalDateTime end) {
        return dataService.getCalendarsByRange(start, end);
    }
}
