import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            TestListenerAdapter tla = new TestListenerAdapter();
            List<String> suites = new ArrayList<>();
            suites.add("D:\\Programming\\Telerik\\FINAL PROJECT\\WEare-API-UI\\TestNG.xml"); //path of .xml file to be run-provide complete path

            TestNG tng = new TestNG();
            tng.setTestSuites(suites);

            tng.run(); //run test suite
        }

    }
}
