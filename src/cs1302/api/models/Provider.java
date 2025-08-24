package cs1302.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a streaming provider from the TMDB API response.
 * Contains information about a streaming service including its name
 * and logo image path.
 */
public class Provider {

    /** Name of the streaming service provider. */
    @SerializedName("provider_name")
    private String providerName;

    /** Path to the provider's logo image. */
    @SerializedName("logo_path")
    private String logoPath;

    /**
     * Gets the name of the streaming provider.
     *
     * @return the provider name
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * Sets the name of the streaming provider.
     *
     * @param providerName the provider name to set
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    /**
     * Gets the path to the provider's logo image.
     *
     * @return the logo path
     */
    public String getLogoPath() {
        return logoPath;
    }

    /**
     * Sets the path to the provider's logo image.
     *
     * @param logoPath the logo path to set
     */
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    @Override
    public String toString() {
        return providerName;
    }

} // Provider