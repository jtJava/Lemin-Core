package us.lemin.core.player;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import us.lemin.core.*;
import us.lemin.core.player.rank.CustomColorPair;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.time.TimeUtil;
import us.lemin.core.utils.timer.Timer;
import us.lemin.core.utils.timer.impl.DoubleTimer;
import us.lemin.core.utils.timer.impl.IntegerTimer;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class CoreProfile extends PlayerProfile {
    private final List<UUID> ignored = new ArrayList<>();
    private final List<String> knownAddresses = new ArrayList<>();
    private final List<Object> notes = new ArrayList<>();
    private final String currentAddress;
    private final String name;
    private final UUID id;
    private final Timer commandCooldownTimer = new DoubleTimer(1);
    private final Timer reportCooldownTimer = new IntegerTimer(TimeUnit.SECONDS, 60);
    private Timer chatCooldownTimer;
    private Rank rank = Rank.MEMBER;
    private CustomColorPair colorPair = new CustomColorPair();
    private UUID converser;
    private String reportingPlayerName;
    private long muteExpiryTime = -2;
    private boolean playingSounds = true;
    private boolean messaging = true;
    private boolean globalChatEnabled = true;
    private boolean inStaffChat;
    private boolean vanished;
    private long lastChatTime;
    private Location lastLocation;

    // TODO: optimize loading and saving
    @SuppressWarnings("unchecked")
    public CoreProfile(String name, UUID id, String address) {
        super(id, "players");
        this.name = name;
        this.id = id;
        this.currentAddress = address;
        this.knownAddresses.add(address);
        load();
    }

    @Override
    public void deserialize(Document document) {
        this.inStaffChat = document.getBoolean("staff_chat_enabled", inStaffChat);
        this.messaging = document.getBoolean("messaging_enabled", messaging);
        this.playingSounds = document.getBoolean("playing_sounds", playingSounds);

        final String rankName = document.get("rank_name", rank.getName());
        final Rank rank = Rank.getByName(rankName);

        if (rank != null) {
            this.rank = rank;
        }

        final String colorPrimaryName = document.getString("color_primary");
        final String colorSecondaryName = document.getString("color_secondary");

        if (colorPrimaryName != null) {
            this.colorPair.setPrimary(ChatColor.valueOf(colorPrimaryName));
        }

        if (colorSecondaryName != null) {
            this.colorPair.setSecondary(ChatColor.valueOf(colorSecondaryName));
        }

        final List<UUID> ignored = (List<UUID>) document.get("ignored_ids");

        if (ignored != null) {
            this.ignored.addAll(ignored);
        }

        final List<String> knownAddresses = (List<String>) document.get("known_addresses");

        if (knownAddresses != null) {
            this.knownAddresses.addAll(knownAddresses);
        }

        final List<Object> notes = (List<Object>) document.get("notes");

        if (notes != null) {
            this.notes.addAll(notes);
        }

        Document punishDoc = Init.getInstance().getMongoStorage().getDocument("punished_ids", id);

        if (!loadMuteData(punishDoc)) {
            for (final String knownAddress : Objects.requireNonNull(knownAddresses)) {
                punishDoc = Init.getInstance().getMongoStorage().getDocument("punished_addresses", knownAddress);
                loadMuteData(punishDoc);
            }
        }

        Init.getInstance().getMongoStorage().getOrCreateDocument("alts", currentAddress, (doc, exists) -> {
                final List<String> knownPlayers = doc.get("known_players", Arrays.asList(name));
                if (!knownPlayers.contains(name)) knownPlayers.add(name);
                final MongoRequest request = MongoRequest.newRequest("alts", currentAddress);
                request.put("known_players", knownPlayers);
            save(false);
        });
    }

    @Override
    public MongoRequest serialize() {
        return MongoRequest.newRequest("players", id)
                .put("name", name)
                .put("lowername", name.toLowerCase())
                .put("staff_chat_enabled", inStaffChat)
                .put("messaging_enabled", messaging)
                .put("playing_sounds", playingSounds)
                .put("rank_name", rank.getName())
                .put("ignored_ids", ignored)
                .put("known_addresses", knownAddresses)
                .put("color_primary", colorPair.getPrimary() == null ? null : colorPair.getPrimary().name())
                .put("color_secondary", colorPair.getSecondary() == null ? null : colorPair.getSecondary().name());
    }

    private boolean loadMuteData(Document document) {
        if (document != null) {
            final Boolean muted = document.getBoolean("muted");

            if (muted == null) {
                return false;
            }
            final long muteExpiryTime = document.getLong("mute_expiry");

            if (muted && (muteExpiryTime == -1 || System.currentTimeMillis() < muteExpiryTime)) {
                this.muteExpiryTime = muteExpiryTime;
                return true;
            }
        }

        return false;
    }

    public Timer getChatCooldownTimer() {
        if (chatCooldownTimer == null) {
            chatCooldownTimer = hasDonor() ? new DoubleTimer(1) : new DoubleTimer(3);
        }

        return chatCooldownTimer;
    }

    public String getChatFormat() {
        final String rankColor = rank.getColor();
        final String primary = colorPair.getPrimary() == null ? rankColor : colorPair.getPrimary().toString();
        final String secondary = colorPair.getSecondary() == null ? rankColor : colorPair.getSecondary().toString();

        return String.format(rank.getRawFormat(), primary, secondary) + name;
    }

    public void updateLastChatTime() {
        lastChatTime = System.currentTimeMillis();
    }

    public boolean isMuted() {
        return isTemporarilyMuted() || isPermanentlyMuted();
    }

    public boolean isTemporarilyMuted() {
        return System.currentTimeMillis() < muteExpiryTime;
    }

    public boolean isPermanentlyMuted() {
        return muteExpiryTime == -1;
    }

    public String getTimeMuted() {
        return TimeUtil.formatTimeMillis(muteExpiryTime - System.currentTimeMillis());
    }

    public boolean hasRank(Rank requiredRank) {
        return rank.ordinal() <= requiredRank.ordinal();
    }

    public boolean hasStaff() {
        return hasRank(Rank.TRIAL_MOD);
    }

    public boolean hasDonor() {
        return hasRank(Rank.PREMIUM);
    }

    public boolean isDonor() {
        return rank == Rank.PREMIUM;
    }

    public void ignore(UUID id) {
        ignored.add(id);
    }

    public void unignore(UUID id) {
        ignored.remove(id);
    }

    public boolean hasPlayerIgnored(UUID id) {
        return ignored.contains(id);
    }
}
