package TestFrame.Buffer;

import java.util.stream.Stream;



import model.modeling.script.TestFrame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class bufferTest
{
    @TestFactory
    @DisplayName("Test Case Generator")
    Stream<DynamicTest> getTestCases()
    {
        return TestFrame.getTestCases(bufferFrame.class, false);
    }
}
