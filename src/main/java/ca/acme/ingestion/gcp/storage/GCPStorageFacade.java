/**
 * @author Ram Saran Vuppuluri
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

    public GCPStorageFacade(String jsonKeyFilePathIn, String projectIdIn) throws IOException {
        googleCloudInstance = new GoogleCloudInstance(jsonKeyFilePathIn, projectIdIn);
    }

    private Storage getStorageConfig() {
        return StorageOptions.newBuilder()
                .setCredentials(googleCloudInstance.getCredentials())
                .setProjectId(googleCloudInstance.getProjectId())
                .build().getService();
    }

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
