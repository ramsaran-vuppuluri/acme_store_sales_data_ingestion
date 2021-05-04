package ca.acme.ingestion;

import ca.acme.ingestion.gcp.storage.GCPStorageFacade;
import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;
import com.google.devtools.common.options.OptionsParser;

import java.io.IOException;

public class Driver {
    public static void main(String... args) throws IOException {
        OptionsParser optionsParser = OptionsParser.newOptionsParser(DriverCommandLineUtilityOptions.class);
        optionsParser.parseAndExitUponError(args);

        DriverCommandLineUtilityOptions gcpStorageUtilityOptions = optionsParser.getOptions(DriverCommandLineUtilityOptions.class);

        GCPStorageFacade gcpStorageFacade = new GCPStorageFacade(gcpStorageUtilityOptions.jsonKeyFilePath, gcpStorageUtilityOptions.projectId);

        gcpStorageFacade.uploadObjectsFromLocal(gcpStorageUtilityOptions.bucketName, gcpStorageUtilityOptions.localDirectoryPath,
                gcpStorageUtilityOptions.fileExtensionPattern, gcpStorageUtilityOptions.bucketDirectoryPath);
    }

    /**
     * java -jar acme_store_sales_data_ingestion-1.0-SNAPSHOT.jar -j /Users/saha/Downloads/key.json -p "playground-s-11-335dc781" -b "playground-s-11-335dc781"
     */
    public static class DriverCommandLineUtilityOptions extends OptionsBase {
        @Option(name = "jsonKeyFilePath",
                abbrev = 'j',
                help = "File path for the JSON Key generated for the Service Account",
                defaultValue = "./../src/main/resources/key.json")
        public String jsonKeyFilePath;

        @Option(name = "projectId",
                abbrev = 'p',
                help = "GCP Project ID",
                defaultValue = "")
        public String projectId;

        @Option(name = "bucketName",
                abbrev = 'b',
                help = "Google Cloud Storage Bucket Name",
                defaultValue = "")
        public String bucketName;

        @Option(name = "localDirectoryPath",
                abbrev = 'l',
                help = "Path of directory in local machine from where the files need to be uploaded",
                defaultValue = "./../src/test/resources/testData/")
        public String localDirectoryPath;

        @Option(name = "fileExtensionPattern",
                abbrev = 'e',
                help = "File extension pattern",
                defaultValue = "*.csv")
        public String fileExtensionPattern;

        @Option(name = "bucketDirectoryPath",
                abbrev = 'd',
                help = "Relative directory path in bucket where files should be copied to.",
                defaultValue = "landing/")
        public String bucketDirectoryPath;
    }
}
