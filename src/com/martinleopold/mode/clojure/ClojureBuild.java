/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.martinleopold.mode.clojure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import processing.app.Library;
import processing.app.Sketch;
import processing.app.SketchException;
import processing.mode.java.JavaBuild;

/**
 *
 * @author mlg
 */
class ClojureBuild extends JavaBuild {
    String clojureFile = "";
    
    public ClojureBuild(Sketch sketch) {
        super(sketch);
    }
    
    @Override
    public String build(File srcFolder, File binFolder) throws SketchException {
        this.srcFolder = srcFolder;
        this.binFolder = binFolder;
	
        // escape all newlines to make single line string
        String code = sketch.getMainProgram();

        //System.out.println(srcFolder);
        //System.out.println(getClassPath());

        // write to temp clj file, thats all for the "build"
	try {
	    File cljFile = new File(binFolder.getAbsolutePath() + File.separator + sketch.getName() + ".clj");
	    cljFile.createNewFile();
	    BufferedWriter writer = new BufferedWriter(new FileWriter(cljFile));
	    writer.write(code);
	    writer.close();
	    clojureFile = cljFile.getAbsolutePath();
	    //System.out.println(cljFile.getAbsolutePath());
	} catch (Exception e) {
	   throw new SketchException(e.getMessage());
	}
	
        String classNameFound = sketch.getName();
	//sketchClassName = classNameFound;
        return classNameFound;
    }
    
    public String getClojureFile() {
	return clojureFile;
    }
	
	@Override
    public String getClassPath() {
        String classPath = binFolder.getAbsolutePath();
	
        // clojure.jar
        classPath += File.pathSeparator + sketch.getMode().getContentFile("mode/clojure-1.4.0.jar").getAbsolutePath();

        // regular classpath
        String javaClassPath = System.getProperty("java.class.path");
        
        // Remove quotes if any.. A messy (and frequent) Windows problem
        if (javaClassPath.startsWith("\"") && javaClassPath.endsWith("\"")) {
            javaClassPath = javaClassPath.substring(1, javaClassPath.length() - 1);
        }
        classPath += File.pathSeparator + javaClassPath;

        return classPath;
    }

    @Override
    public ArrayList<Library> getImportedLibraries() {
        return new ArrayList<Library>();
    }

    @Override
    public String getJavaLibraryPath() {
        return "";
    }
}
