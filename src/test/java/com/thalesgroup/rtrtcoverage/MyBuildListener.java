package com.thalesgroup.rtrtcoverage;

import hudson.console.ConsoleNote;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Cause;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Sebastien Barbier
 * @version 1.0
 */
public class MyBuildListener implements BuildListener {

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.BuildListener#finished(hudson.model.Result)
     */
    public void finished(final Result arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.BuildListener#started(java.util.List)
     */
    public void started(final List<Cause> arg0) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#annotate(hudson.console.ConsoleNote)
     */
    public void annotate(final ConsoleNote arg0) throws IOException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#error(java.lang.String)
     */
    public PrintWriter error(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#error(java.lang.String,
     * java.lang.Object[])
     */
    public PrintWriter error(final String arg0, final Object... arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#fatalError(java.lang.String)
     */
    public PrintWriter fatalError(final String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#fatalError(java.lang.String,
     * java.lang.Object[])
     */
    public PrintWriter fatalError(final String arg0, final Object... arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#getLogger()
     */
    public PrintStream getLogger() {
        return new PrintStream(new OutputStream() {
            @Override
            public void write(final int b) {
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see hudson.model.TaskListener#hyperlink(java.lang.String,
     * java.lang.String)
     */
    public void hyperlink(final String arg0, final String arg1)
            throws IOException {
        // TODO Auto-generated method stub

    }

}
