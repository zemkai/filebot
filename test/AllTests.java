import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import net.sourceforge.filebot.FileBotTestSuite;
import net.sourceforge.tuned.TunedTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses( { FileBotTestSuite.class, TunedTestSuite.class })
public class AllTests {
	
	public static Test suite() {
		return new JUnit4TestAdapter(AllTests.class);
	}
	
}
