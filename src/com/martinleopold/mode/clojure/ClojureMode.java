package com.martinleopold.mode.clojure;

import java.io.*;
import processing.app.*;
import processing.mode.java.JavaMode;
import processing.mode.java.runner.Runner;


/**
 * Mode Template for extending Java mode in Processing IDE 2.0a5 or later.
 *
 */
public class ClojureMode extends JavaMode {
    String template = "";

    public ClojureMode(Base base, File folder) {
        super(base, folder);

	// load tempate
	try {
	    File templateFile = new File(getContentFile("template.clj").getAbsolutePath());
	    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(templateFile), "UTF-8"));
	    int ch;
	    while ((ch = reader.read()) != -1) {
		template += (char)ch;
	    }
	    reader.close();
	} catch (Exception e) {
	    System.err.println(e.getMessage());
	    template = "";
	}
    }

    /**
     * Called by PDE
     */
    @Override
    public String getTitle() {
        return "Clojure";
    }

    @Override
    public Runner handleRun(Sketch sketch, RunnerListener listener) throws SketchException {
        //System.out.println("ClojureMode.handleRun()...");

        String code = sketch.getMainProgram();

        /*
         * launch a vm that runs:
	 * java -cp clojure.jar clojure.main -e expression #  evaluate expression
         * java -cp clojure.jar clojure.main - #  pipe in script over std.in
         * java -cp clojure.jar clojure.main /path/file  & give a script file
         *
         */

        // Could not locate clojure/core__init.class or clojure/core.clj on classpath
        //https://groups.google.com/forum/?fromgroups#!topic/clojure/Aa04E9aJRog
        //Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

	//eval string expression
        //clojure.main.main(new String[] {"-e", code});


	// create a file from code and run it
	// this runs in the same process/vm and kills the pde when the sketch window is closed
	/*
	try {
	    File tempFile = new File(sketch.makeTempFolder() + File.separator + "code.clj");
	    tempFile.createNewFile();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
	    writer.write(code);
	    writer.close();
	    System.out.println(tempFile.getAbsolutePath());
	    clojure.main.main(new String[] {tempFile.getAbsolutePath()});
	} catch (Exception e) {
	   throw new SketchException(e.getMessage());
	}
	*/

        ClojureBuild build = new ClojureBuild(sketch);
        String appletClassName = build.build(false);
        if (appletClassName != null) {
            final Runner runtime = new ClojureRunner(build, listener);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runtime.launch(false);  // this blocks until finished // not (unfortunately)
                }
            }).start();
            return runtime;
        }
        return null;
    }


    @Override
    public Editor createEditor(Base base, String path, EditorState state) {
        Editor editor = super.createEditor(base, path, state);
	editor.setText(template);
	editor.setSelection(0, 0);
	return editor;
    }
}