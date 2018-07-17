package us.lemin.core.callback;

import org.bson.Document;

public interface DocumentCallback {
    void call(Document document, boolean found);
}
