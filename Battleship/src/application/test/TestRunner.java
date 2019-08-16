package application.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AIValidationTest.class, AttackTest.class, CorrectStartUpTest.class, ShipPlacementTest.class,
		TestCorrectHumanPlay.class, WinnerTest.class })

public class TestRunner {

}