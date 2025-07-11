package com.stopsnearme.app.ws.ui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// package org.eclipse.jakarta.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.stopsnearme.app.ws.ui.model.JobRepository;
// import org.eclipse.jakarta.model.JobRepository;
import com.stopsnearme.app.ws.ui.model.entity.Job;
import com.stopsnearme.app.ws.ui.model.entity.types.Status;
import com.stopsnearme.app.ws.ui.model.entity.types.Type;

// import jakarta.inject.Inject;
/* 
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
*/

@RestController
@RequestMapping("/fetchstatics")
public class FetchStaticsController {

    private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    // @Inject
    private Job job;

    private static final Lock lock = new ReentrantLock();
    // private final static Object lock = new Object();
    // private static final Semaphore available = new Semaphore(2, true);

    private static final ExecutorService threadpool = Executors.newCachedThreadPool();

    private static Future<Boolean> futureTask = null;

    // @Inject
	@Autowired
    private JobRepository jobRepository;

	// @PreAuthorize("")
    @GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public String dowmloadStatics(@DefaultValue("austin") @RequestParam(value="region", defaultValue="austin") String region) {
        logger.log(Level.INFO, "Lock for {0}.", lock.hashCode());

        // synchronized (lock) {
        // if (lock.tryLock()) {
        if (futureTask == null || futureTask.isDone()) {
            job = new Job();
            job.setType(Type.DownloadFiles);
            job.setStatus(Status.NotYetStarted);
            jobRepository.create(job);
            futureTask = threadpool.submit(() -> downloadStaticsAsync_(region));
            job.setStatus(Status.InProgress);
            jobRepository.update(job);

            return "Starting to load for " + region + " and task hash " + futureTask.hashCode();
        } else {
            return "Another job already running with task hash - " + futureTask.hashCode();
        }

        // }
        /* else {
        job.setType(Type.LastJobInProgress);
        job.setStatus(Status.NotYetStarted);
        return "Another job already running, this job id " + job.getId();
        }

         */
    }

    private void postResponse() {
        job.setStatus(Status.Completed);
        jobRepository.update(job);
    }

    private Boolean downloadStaticsAsync_(String region) {
        try {
            // synchronized (lock) {
            if (lock.tryLock()) {
                logger.log(Level.FINER, "Got lock for {0}.", job.hashCode());
                downloadStaticsAsync(region);
                return true;
            }
        } finally {
            lock.unlock();
        }

        return false;
    }

    private void downloadStaticsAsync(String region) {

        var staticsPath = System.getenv("STATICS");

        if(staticsPath.length() <= 0)
            logger.log(Level.SEVERE, "Check if STATICS env var is set");

        if ("bc".equals(region)) {
            var regionIds = System.getenv("BC_REGION_IDS").split(",");
            if (regionIds.length > 0) {

                var url = System.getenv("TRANSIT_STATIC_BC");

                for (String reg : regionIds) {
                    reg = reg.trim();

                    var path = String.format("%s/%s/%s/", staticsPath, region, reg);

                    String[] arr = {region, reg};
                    logger.log(Level.FINER, "Starting to download for {0} and area {1}", arr);

                    // logger.log(Level.INFO, "Path is {0}", path);
                    creatDirs(path);

                    if (downloadFiles(url + reg, path)) {
                        job.setType(Type.ExtractingFiles);
                        job.setStatus(Status.NotYetStarted);
                        if (extractFiles(path)) {
                            logger.log(Level.FINER, "Finished extraction for {0} and area {1}", arr);
                        }
                    }
                }

                postResponse();
                return;
            }

            // van uses date in path
        } else if (region.equals("vancouver")) {
            var path = String.format("%s/%s/", staticsPath, region);
            LocalDate datee = LocalDate.now();
            var url = System.getenv("TRANSIT_STATIC");

            // loop for last 10 days
            for (int i = 0; i < 10; i++) {
                var url_ = url;
                // datee = datee.minusDays(i); this is missing some days
                // var dateSte = datee.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
                url_ = String.format("%s/%s/google_transit.zip", url_, datee.minusDays(i).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
                logger.log(Level.FINER, "Download from {0}", url_);

                creatDirs(path);

                if (downloadFiles(url_, path)) {
                    job.setType(Type.ExtractingFiles);
                    job.setStatus(Status.NotYetStarted);
                    jobRepository.update(job);
                    if (extractFiles(path)) {
                        logger.log(Level.FINER, "Finished extraction for {0}", region);
                        postResponse();
                        return;
                    }
                }
            }

            // most regions one url has the latest
        } else {
            var url = System.getenv("TRANSIT_STATIC");
            var path = String.format("%s/%s/", staticsPath, region);
            logger.log(Level.FINER, "Download {0}", url);

            creatDirs(path);

            if (downloadFiles(url, path)) {
                job.setType(Type.ExtractingFiles);
                job.setStatus(Status.NotYetStarted);
                jobRepository.update(job);
                if (extractFiles(path)) {
                    logger.log(Level.FINER, "Finished extraction for {0}", region);
                    postResponse();
                    return;
                }
            }
        }
        return;
    }

    private boolean creatDirs(String path) {
        try {
            logger.log(Level.FINE, "Creating dirs here - {0}", path);
            Files.createDirectories(Paths.get(path));
            return true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Unable to create dir {0}", ex);
        }
        return false;
    }

    private boolean downloadFiles(String urlString, String subPath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlString).openStream())) {

            FileOutputStream fileOutputStream = new FileOutputStream(subPath + "/statics.zip");

            // 1kb
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            fileOutputStream.close();
            return true;

        } catch (IOException e) {
            // handle exception
            logger.log(Level.SEVERE, "Unable to download {0}", e.toString());
            // e.printStackTrace();
            return false;
        }
    }

    private  static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private boolean extractFiles(String subPath) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(subPath + "/statics.zip"))) {

            var destDir = new File(subPath);

            // 1kb
            byte buffer[] = new byte[1024];
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();

            }

            zis.closeEntry();
            zis.close();

            return true;

        } catch (IOException e) {
            // handle exception
            logger.log(Level.SEVERE, "Unable to download to {0}", e.getMessage());
            // e.printStackTrace();
            return false;
        }
    }

}
