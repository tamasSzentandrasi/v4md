package com.incquerylabs.v4md.test.runner;

import org.apache.maven.plugin.surefire.StartupReportConfiguration;
import org.apache.maven.plugin.surefire.report.DefaultReporterFactory;
import org.apache.maven.surefire.common.junit4.JUnit4RunListener;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.RunListener;
import org.apache.maven.surefire.report.SimpleReportEntry;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;

import com.incquerylabs.v4md.test.AllTests;
import com.incquerylabs.v4md.test.snapshot.ISnapshotManager;
import com.incquerylabs.v4md.test.snapshot.SnapshotAnalyzer;
import com.incquerylabs.v4md.test.snapshot.SnapshotCreator;
import com.nomagic.magicdraw.commandline.CommandLineAction;

import edu.emory.mathcs.backport.java.util.Arrays;



public class TestRunner implements CommandLineAction{
	
	public static ISnapshotManager snapshotManager = new SnapshotAnalyzer();
	
	private StartupReportConfiguration getConfiguration() {
		return StartupReportConfiguration.defaultValue();
	}

	@Override
	public byte execute(String[] args) {
		if(Arrays.asList(args).contains("generateQuerySnapshots")) {
			TestRunner.snapshotManager = new SnapshotCreator();
		}
		
		JUnitCore core = new JUnitCore();
		RunListener reporter = new DefaultReporterFactory(getConfiguration()).createReporter();
		final ReportEntry report = new SimpleReportEntry(AllTests.class.getName(), AllTests.class.getName());
		try {
			reporter.testSetStarting(report);
			core.addListener(new JUnit4RunListener(reporter));
			core.run(new Computer(), AllTests.class);
		} finally {
			// This step is responsible for creating the test logs
			reporter.testSetCompleted(report);
		}
		return 0;
	}

}
