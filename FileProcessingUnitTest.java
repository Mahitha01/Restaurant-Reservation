import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class FileProcessingUnitTest {
    @Test
    public void testFileProcessing() {
        FileProcessing f = new FileProcessing();
        String fName = "Customer1.txt";
        List<String> inputInfo = new ArrayList<String>();
        inputInfo.add("THIS IS TEST");
        inputInfo.add("THIS IS TEST");
        inputInfo.add("THIS IS TEST");
        f.writeFile(fName, inputInfo);
        List<String> outputInfo = f.readFile(fName);
        assertEquals(inputInfo, outputInfo);
    }
}
