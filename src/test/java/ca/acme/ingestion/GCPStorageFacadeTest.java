package ca.acme.ingestion;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.junit.Test;

import java.io.File;

public class GCPStorageFacadeTest {
    @Test
    public void listTestDataFiles() {
        File dir = new File("./src/test/resources/testData/");
        File[] listFiles = dir.listFiles();

        for (File file : listFiles) {
            System.out.println(file.getName());
        }
        assert true;
    }

    @Test
    public void filterTestDataFiles() {
        File dir = new File("./src/test/resources/testData/");
        File[] listFiles = dir.listFiles();

        for (File file : listFiles) {
            if (FilenameUtils.wildcardMatch(file.getName(), "*.csv", IOCase.INSENSITIVE)) {
                System.out.println(file.getName());
            }
        }
        assert true;
    }
}
