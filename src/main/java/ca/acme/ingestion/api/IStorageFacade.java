/**
 * @author Ram Saran Vuppuluri
 * <p>
 * IStorageFacade interface contains the method API signature that need to be implemented by any concrete child class that is acting as facade for cloud storage.
 */
package ca.acme.ingestion.api;

import java.io.IOException;

public interface IStorageFacade {
    /**
     * This method is the abstract signature of the method to upload files from local file system to cloud.
     *
     * @param bucketName           Cloud Storage bucket name.
     * @param localDirectoryPath   Local directory from which files should be copied.
     * @param fileExtensionPattern File name extension pattern.
     * @param bucketDirectoryPath  Relative path in the bucket where the files should be stored.
     * @throws IOException Will be thrown if any IOException is encountered while reading files from local file system or writing files to cloud storage bucket.
     */
    void uploadObjectsFromLocal(String bucketName, String localDirectoryPath, String fileExtensionPattern, String bucketDirectoryPath) throws IOException;
}
