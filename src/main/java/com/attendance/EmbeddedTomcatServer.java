package com.attendance;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EmbeddedTomcatServer {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        
        // Set port from environment variable or default to 8080
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }
        tomcat.setPort(Integer.parseInt(port));
        
        // Determine webapp directory - try multiple locations
        String webappDirLocation = null;
        String currentDir = System.getProperty("user.dir");
        String[] possiblePaths = {
            currentDir + "/webapp",
            currentDir + "/target/webapp",
            currentDir + "/src/main/webapp",
            "webapp",
            "target/webapp",
            "src/main/webapp",
            "./webapp",
            "./target/webapp"
        };
        
        for (String path : possiblePaths) {
            Path webappPath = Paths.get(path);
            if (Files.exists(webappPath) && Files.isDirectory(webappPath)) {
                webappDirLocation = path;
                if (!webappDirLocation.endsWith("/") && !webappDirLocation.endsWith(File.separator)) {
                    webappDirLocation += File.separator;
                }
                System.out.println("Using webapp directory: " + webappDirLocation);
                break;
            }
        }
        
        if (webappDirLocation == null) {
            System.err.println("ERROR: Could not find webapp directory!");
            System.err.println("Current directory: " + currentDir);
            System.err.println("Tried paths: " + String.join(", ", possiblePaths));
            // List current directory contents for debugging
            try {
                File currentDirFile = new File(currentDir);
                System.err.println("Current directory contents: " + java.util.Arrays.toString(currentDirFile.list()));
            } catch (Exception e) {
                System.err.println("Could not list directory: " + e.getMessage());
            }
            System.exit(1);
        }
        
        // Configure context
        Context ctx = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        
        // Add classes to classpath
        String[] classPaths = {
            currentDir + "/classes",
            currentDir + "/target/classes",
            "classes",
            "target/classes",
            "./classes"
        };
        
        File classesDir = null;
        for (String classPath : classPaths) {
            File testDir = new File(classPath);
            if (testDir.exists() && testDir.isDirectory()) {
                classesDir = testDir;
                System.out.println("Using classes directory: " + classesDir.getAbsolutePath());
                break;
            }
        }
        
        if (classesDir != null && classesDir.exists()) {
            StandardRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    classesDir.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        } else {
            System.out.println("Warning: Classes directory not found, using JAR classpath");
        }
        
        // Enable JSP support
        ctx.setParentClassLoader(EmbeddedTomcatServer.class.getClassLoader());
        
        // Start server
        tomcat.start();
        System.out.println("Server started on port " + port);
        tomcat.getServer().await();
    }
}

