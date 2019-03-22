package us.lemin.core.punishment;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import org.bson.Document;
import org.bukkit.Bukkit;
import us.lemin.core.CorePlugin;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.TaskUtil;
import us.lemin.core.utils.time.TimeUtil;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Punishment {

    private static final String collectionName = "punishments";

    private UUID uuid;
    private PunishmentType type;
    private UUID targetUuid;
    private UUID addedBy;
    private String addedReason;
    private UUID removedBy;
    private String removeReason;
    private Timestamp timestamp;
    private Timestamp expiration;
    private boolean hidden;
    private boolean silent = true;

    public boolean isBan() {
        return this.type == PunishmentType.TEMPBAN || this.type == PunishmentType.BAN || this.type == PunishmentType
                .BLACKLIST;
    }

    public boolean isActive() {
        return !this.isRemoved() && (this.isPermanent() || System.currentTimeMillis() - this.expiration.getTime() < 0);
    }

    public boolean isRemoved() {
        return this.removeReason != null;
    }

    public boolean isPermanent() {
        return this.expiration == null;
    }

    public String getTimeLeft() {
        if (this.isRemoved()) {
            return "Removed";
        }

        if (this.isPermanent()) {
            return "Permanent";
        }

        if (!(this.isActive())) {
            return "Expired";
        }

        Calendar from = Calendar.getInstance();
        from.setTime(new Date(System.currentTimeMillis()));

        Calendar to = Calendar.getInstance();
        to.setTime(new Date(this.expiration.getTime()));

        return TimeUtil.formatDateDiff(from, to);
    }

    public void load() {
        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument(collectionName, uuid, (document, exists) -> {
            this.uuid = UUID.fromString(document.getString("uuid"));
            this.type = PunishmentType.valueOf(document.getString("type"));
            this.targetUuid = UUID.fromString(document.getString("target_uuid"));
            this.addedReason = document.getString("added_reason");
            this.timestamp = new Timestamp(document.getLong("timestamp"));
            this.hidden = document.getBoolean("hidden");
            this.silent = document.getBoolean("silent");

            if (document.containsKey("added_by")) {
                this.addedBy = UUID.fromString(document.getString("added_by"));
            }

            if (document.containsKey("removed_by")) {
                this.removedBy = UUID.fromString(document.getString("removed_by"));
            }

            if (document.containsKey("remove_reason")) {
                this.removeReason = document.getString("remove_reason");
            }

            if (document.containsKey("expiration")) {
                this.expiration = new Timestamp(document.getLong("expiration"));
            }
        });
    }

    public void save(boolean async) {
        Document document = new Document();

        document.put("uuid", this.uuid.toString());
        document.put("type", this.type.name());
        document.put("target_uuid", this.targetUuid.toString());
        document.put("added_reason", this.addedReason);
        document.put("timestamp", this.timestamp.getTime());
        document.put("hidden", this.hidden);
        document.put("silent", this.silent);

        if (this.addedBy != null) {
            document.put("added_by", this.addedBy.toString());
        }

        if (this.expiration != null) {
            document.put("expiration", this.expiration.getTime());
        }

        if (this.removedBy != null) {
            document.put("removed_by", this.removedBy.toString());
        }

        if (this.removeReason != null) {
            document.put("remove_reason", this.removeReason);
        }

        MongoRequest request = MongoRequest.newRequest(collectionName, document);

        if (async) {
            TaskUtil.runAsync(CorePlugin.getInstance(), request::run);
        } else {
            request.run();
        }
    }

    public void broadcast(String targetName, String staffName) {
        String toBroadcast = PunishmentMessages.BROADCAST
                .replace("{target_name}", targetName)
                .replace("{staff_name}", staffName)
                .replace("{context}", this.removeReason == null ? this.type.getContext() : this.type.getUndoContext());

        if (this.silent) {
            CorePlugin.getInstance().getStaffManager().messageStaff(ChatColor.GRAY + "[Silent] " + toBroadcast);
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(toBroadcast));
        }
    }
}
