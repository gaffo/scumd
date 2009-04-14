package com.asolutions;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;

public class MockTestCase {

	protected Mockery context = new JUnit4Mockery();

	@Before
	public void setupMockery() {
		context.setImposteriser(ClassImposteriser.INSTANCE);
	}
	
	@After
	public void mockeryAssertIsSatisfied(){
		context.assertIsSatisfied();
	}

	protected void checking(Expectations expectations) {
		context.checking(expectations);
	}

}
