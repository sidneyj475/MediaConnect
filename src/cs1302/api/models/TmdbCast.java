package cs1302.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a cast member from the TMDB API response.
 * Contains information about both the actor and their role in the content.
 */
public class TmdbCast {

    /** The real name of the actor. */
    private String name;

    /** The character name played by the actor. */
    private String character;

    /** Path to actor's profile image, if available. */
    @SerializedName("profile_path")
    private String profilePath;

    /**
     * Gets the name of the actor.
     *
     * @return the actor's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the actor.
     *
     * @param name the actor's name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the character name played by the actor.
     *
     * @return the character name
     */
    public String getCharacter() {
        return character;
    }

    /**
     * Sets the character name played by the actor.
     *
     * @param character the character name to set
     */
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * Gets the path to the actor's profile image.
     *
     * @return the profile image path
     */
    public String getProfilePath() {
        return profilePath;
    }

    /**
     * Sets the path to the actor's profile image.
     *
     * @param profilePath the profile image path to set
     */
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return String.format("%s as %s", name, character);
    }

} // TmdbCast