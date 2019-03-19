package us.lemin.core.api.scoreboardapi.api;

import us.lemin.core.api.scoreboardapi.ScoreboardUpdateEvent;

public interface ScoreboardAdapter {

    void onUpdate(ScoreboardUpdateEvent event);

    int updateRate();
}
