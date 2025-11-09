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
        String[] possiblePaths = {
            "target/webapp",
            "src/main/webapp",
            "webapp",
            "src/main/webapp/"
        };
        
        for (String path : possiblePaths) {
            Path webappPath = Paths.get(path);
            if (Files.exists(webappPath) && Files.isDirectory(webappPath)) {
                webappDirLocation = path + (path.endsWith("/") ? "" : "/");
                System.out.println("Using webapp directory: " + webappDirLocation);
                break;
            }
        }
        
        if (webappDirLocation == null) {
            System.err.println("ERROR: Could not find webapp directory!");
            System.err.println("Tried paths: " + String.join(", ", possiblePaths));
            System.exit(1);
        }
        
        // Configure context
        Context ctx = tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());
        
        // Add classes to classpath
        File classesDir = new File("target/classes");
        if (classesDir.exists()) {
            StandardRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                    classesDir.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        }
        
        // Enable JSP support
        ctx.setParentClassLoader(EmbeddedTomcatServer.class.getClassLoader());
        
        // Start server
        tomcat.start();
        System.out.println("Server started on port " + port);
        tomcat.getServer().await();
    }
}

