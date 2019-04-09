package us.lemin.core.player.note;

import lombok.*;

import java.text.*;
import java.util.*;

@Getter
public class Note {

    private final String author;
    private final String note;
    private final Date date;

    public Note(final String author, final String note, final Date date) {
        this.author = author;
        this.note = note;
        this.date = date;
    }
    
    public String getDataFormatted() {
        return new SimpleDateFormat("MM-dd-yyyy").format(this.date);
    }

}
