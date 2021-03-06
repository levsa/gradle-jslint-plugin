/*
 * Copyright (c) 2010.
 * Author Kelly Robinson.
 */

package org.kar.jslint.gradle.plugin

import org.gradle.api.Project

/**
 * @author Kelly Robinson
 */
class JSLintPluginConvention
{
    private static final String TXT = 'txt'
    private static final String XML = 'xml'
    private static final String CHECKSTYLE = 'checkstyle'
    private static final String REPORTS_DIR_PROPERTY = 'baseDir'
    private static final String REPORTS_DIR_NAME = 'reports'
    private static final String PLAIN = 'plain'
    private static final String HTML = 'html'
    public static final ANT_JAR = 'com.googlecode.jslint4java:jslint4java-ant:2.0.3'

    public static final String TASK_NAME = 'com.googlecode.jslint4java.ant.JSLintTask'
    List<String> inputDirs = ['.']
    String includes = '**/*.js'
    String excludes = ''
    String formatterType = 'plain'
    String destFilename = 'jslint'
    boolean haltOnFailure = true
    String options = ''
    String jslintPath = ''
    String destDir
    String destFile

    public JSLintPluginConvention(Project project)
    {
        if (!project.reporting.hasProperty(REPORTS_DIR_PROPERTY))
        {
            project.reporting.setProperty(REPORTS_DIR_PROPERTY, "$project.buildDir/$REPORTS_DIR_NAME")
        }
        destDir = "${project.reporting.baseDir}"
        destFile = "$destDir/$destFilename"
    }

    /**
     * Perform custom configuration of the plugin using the provided closure.
     * @param closure
     */
    def jslint(Closure closure)
    {
        closure.delegate = this
        closure()
    }

    /**
     * @return the appropriate file name based on formatterType
     */
    String createOutputFileName()
    {
        switch (formatterType)
        {
            case (PLAIN):
                return createOutputFileName(TXT)
            case ([XML, HTML, CHECKSTYLE]):
                return createOutputFileName(XML)
        }
    }

    /**
     * @param extension
     * @return name for the output file appended with the provided extension
     */
    String createOutputFileName(String extension)
    {
        "${destFile}.$extension"
    }

    /**
     * The addition of an html output type requires the ant task to produce xml first so
     * the parameter to ant for formatterType might be different than the one set on the plugin.
     *
     * @return which formatterType should be set on the underlying ant task
     */
    String decideFormat()
    {
        switch (formatterType)
        {
            case (HTML):
                return XML
            default:
                return formatterType
        }
    }

    /**
     * @return map of properties to pass to the jslint ant task
     */
    Map mapTaskProperties()
    {
        Map taskProperties = [haltOnFailure: haltOnFailure]
        if (options)
        {
            taskProperties['options'] = options
        }
        if (jslintPath)
        {
            taskProperties['jslint'] = jslintPath
        }
        return taskProperties
    }
}
