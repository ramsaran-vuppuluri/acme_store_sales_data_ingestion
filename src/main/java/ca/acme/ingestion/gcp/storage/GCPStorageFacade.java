/**
 * @author Ram Saran Vuppuluri
 * <p>
 * This class is an implementation of IStorageFacade. All the GCP GCS storage IO operations are performed by this method.
 */
package ca.acme.ingestion.gcp.storage;

import ca.acme.ingestion.api.IStorageFacade;
import ca.acme.ingestion.gcp.GoogleCloudInstance;
import com.google.cloud.storage.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

import java.io.File;
import java.io.IOException;

public class GCPStorageFacade implements IStorageFacade {
    GoogleCloudInstance googleCloudInstance;

    /**
     * GCPStorageFacade constructor.
     *
     * @param jsonKeyFilePathIn JSON key file path.
     * @param projectIdIn       GCP project ID.
     * @throws IOException Will be thrown if there is an IOException in reading the JSON key file.
     */
    public GCPStorageFacade(String jsonKeyFilePathIn, String projectIdIn) throws IOException {
        googleCloudInstance = new GoogleCloudInstance(jsonKeyFilePathIn, projectIdIn);
    }

    /**
     * This method will return the Storage instance.
     *
     * @return Storage instance.
     */
    private Storage getStorageConfig() {
        return StorageOptions.newBuilder()
                .setCredentials(googleCloudInstance.getCredentials())
                .setProjectId(googleCloudInstance.getProjectId())
                .build().getService();
    }

    /**
     * This method is the implementaton of the abstract method from IStorageFacade to upload files from local file system to cloud.
     * <p>
     * By default if the file is already present in the GCS path it will be overwritten.
     *
     * @param bucketName           Cloud Storage bucket name.
     * @param localDirectoryPath   Local directory from which files should be copied.
     * @param fileExtensionPattern File name extension pattern.
     * @param bucketDirectoryPath  Relative path in the bucket where the files should be stored.
     * @throws IOException
     */
    public void uploadObjectsFromLocal(String bucketName, String localDirectoryPath, String fileExtensionPattern,
                                       String bucketDirectoryPath) throws IOException {
        Storage storage = this.getStorageConfig();

        File[] listFiles = new File(localDirectoryPath).listFiles();

        if (listFiles == null || listFiles.length == 0) {
            System.err.println("No files matching the filter criteria are found at " + localDirectoryPath);

            return;
        }

        for (File file : listFiles) {
            if (FilenameUtils.wildcardMatch(file.getName(), fileExtensionPattern, IOCase.INSENSITIVE)) {

                String objectName = bucketDirectoryPath + file.getName();
                BlobId blobId = BlobId.of(bucketName, objectName);
                BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
                storage.create(blobInfo, FileUtils.readFileToByteArray(file));

                System.out.println(
                        "File " + file.getAbsolutePath() + " uploaded to bucket " + bucketName + " as " + objectName);
            }
        }
    }
}
