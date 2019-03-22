package us.lemin.core.player;

import lombok.Getter;
import org.bson.Document;
import us.lemin.core.CorePlugin;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.TaskUtil;

import java.util.UUID;

/**
 * Extend this class for profiles to store in the Mongo database
 * Use method 'save' to save the profile to the database and 'load' to load from the database
 * <p>
 * Method 'serialize' builds a MongoRequest from the profile to save
 * Method 'deserialize' reads a Document retrieved from the database and writes values to the profile instance
 */
public abstract class PlayerProfile {

    @Getter
    private final UUID id;
    private final String collectionName;

    /**
     * Takes in the UUID of the player and the Mongo collection name to save to and load from
     *
     * @param id             - UUID of the player
     * @param collectionName - Mongo collection name
     */
    public PlayerProfile(UUID id, String collectionName) {
        this.id = id;
        this.collectionName = collectionName;
    }

    /**
     * Calls the deserialize function in the subclass to deserialize the specified document
     */
    public final void load() {
        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument(collectionName, id, (document, exists) -> {
            if (exists) {
                deserialize(document);
            }
            save(false);
        });
    }

    /**
     * Calls the serialize function in the subclass to serialize the profile and store it in Mongo
     *
     * @param async - If the method should run the MongoRequest on the main thread or a different thread
     */
    public final void save(boolean async) {
        MongoRequest request = serialize();
        if (async) {
            TaskUtil.runAsync(CorePlugin.getInstance(), request::run);
        } else {
            request.run();
        }
    }

    /**
     * Use this method to build your MongoRequest to the database
     */
    public abstract MongoRequest serialize();

    /**
     * Use this method to read the specified document to your profile
     *
     * @param serialized - The Document retrieved from mongo
     */
    public abstract void deserialize(Document serialized);
}