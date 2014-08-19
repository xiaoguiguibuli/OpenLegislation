package gov.nysenate.openleg.model.daybreak;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * The DaybreakFile class contains a reference to a local daybreak file along with some of the file's metadata
 * DaybreakFiles are broken up and stored as DaybreakFragments or PageFileEntries depending on the file's
 * DaybreakFileType
 *
 * @see gov.nysenate.openleg.model.daybreak.DaybreakFragment
 * @see gov.nysenate.openleg.model.daybreak.PageFileEntry
 */
public class DaybreakFile implements DaybreakDocument{

    /** A reference to the actual daybreak file */
    private File file;

    /** Designates the type of the daybreak file */
    private DaybreakDocType daybreakDocType;

    /** The associated report date */
    private LocalDate reportDate;

    /** The time that the file was staged */
    private LocalDateTime staged;

    /** True iff the file has been archived */
    private boolean archived;

    /** --- Constructors --- */

    public DaybreakFile(File daybreakFile) throws IOException, IllegalArgumentException {
        if (daybreakFile.exists()) {
            this.file = daybreakFile;
            this.daybreakDocType = DaybreakDocType.getFileDocType(this.getFileName());
            if(this.daybreakDocType==null){
                String message = "File " + daybreakFile + " does not match a daybreak file type";
                throw new IllegalArgumentException(message);
            }
            this.reportDate = this.getReportDateFromFileName();
            this.archived = false;
        }
        else {
            throw new FileNotFoundException(daybreakFile.getAbsolutePath());
        }
    }

    public DaybreakFile(File daybreakFile, LocalDateTime staged, boolean archived) throws IOException,
                                                                                          IllegalArgumentException {
        this(daybreakFile);
        this.staged = staged;
        this.archived = archived;
    }

    /** --- Helper functions --- */

    private LocalDate getReportDateFromFileName() {
        try {
            return LocalDateTime.ofInstant(
                    DateUtils.parseDateStrictly(getFileName(), getDaybreakReportDatePattern()).toInstant(),
                    ZoneId.systemDefault()).toLocalDate();
        }
        catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String getDaybreakReportDatePattern() {
        return "(\\d+)"+ daybreakDocType.getLocalFileExt();
    }

    /** --- Functional getters/setters --- */

    public String getFileName() { return this.file.getName(); }

    @JsonIgnore
    public String getText() {
        try {
            return FileUtils.readFileToString(file);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to read text from Daybreak file:" + this.toString());
        }
    }

    /** --- Override Methods --- */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("reportDate", reportDate)
                .add("daybreakDocType", daybreakDocType)
                .add("file", file)
                .add("stagedDateTime", staged)
                .add("archived", archived)
                .toString();
    }

    @Override
    public DaybreakDocType getDayBreakDocType() {
        return daybreakDocType;
    }

    @Override
    public LocalDateTime getReportDateTime() {
        return reportDate.atStartOfDay();
    }

    /** --- Gettser/Setters --- */

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setDayBreakDocType(DaybreakDocType fileType) {
        this.daybreakDocType = fileType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getStaged() {
        return staged;
    }

    public void setStaged(LocalDateTime staged) {
        this.staged = staged;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
