/**
 * @author Ram Saran Vuppuluri
 * <p>
 * GoogleCloudInstance class contains the common logic to create the GoogleCredentials instance. GoogleCredentials instance
 * is required to authenticate the requests while interacting with GCP.
 */
package ca.acme.ingestion.gcp;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.FileInputStream;
import java.io.IOException;

public class GoogleCloudInstance {
    private String jsonKeyFilePath;
    private String projectId;
    private GoogleCredentials credentials;

    public String getProjectId() {
        return projectId;
    }

    public GoogleCredentials getCredentials() {
        return credentials;
    }

    /**
     * This is the constructor of GoogleCloudInstance class.
     *
     * @param jsonKeyFilePathIn JSON key file path.
     * @param projectIdIn       GCP project ID.
     * @throws IOException Will be thrown if there is an IOException in reading the JSON key file.
     */
    public GoogleCloudInstance(String jsonKeyFilePathIn, String projectIdIn) throws IOException {
        this.jsonKeyFilePath = jsonKeyFilePathIn;
        this.projectId = projectIdIn;
        this.credentials = this.instantiateGoogleCredentials();
    }

    /**
     * This method will create instance of GoogleCredentials.
     *
     * @return Instance of GoogleCredentials.
     * @throws IOException Will be thrown if there is an IOException in reading the JSON key file.
     */
    private GoogleCredentials instantiateGoogleCredentials() throws IOException {
        try {
            return GoogleCredentials.fromStream(new FileInputStream(this.jsonKeyFilePath))
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        } catch (IOException ex) {
            System.err.println("Unable to load the Json key file at " + jsonKeyFilePath);
            throw ex;
        }
    }
}
