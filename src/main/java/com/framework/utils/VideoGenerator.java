package com.framework.utils;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class VideoGenerator {
    String ffmpegPath = "./ffmpeg_sof/ffmpeg.exe";

    private static long extractTimestamp(String filename) {
        // Extract timestamp from filename (e.g., "stepName_20241213_223845_123.png")
        Pattern pattern = Pattern.compile("\\d{8}_\\d{6}_\\d{3}");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            String timestamp = matcher.group().replace("_", ""); // Remove underscores
            return Long.parseLong(timestamp); // Parse as long for full precision
        }
        return 0; // Default if no timestamp is found
    }

    public void generateVideo(String screenshotDir, String outputVideoPath, int frameRate) throws IOException, InterruptedException {
        File dir = new File(screenshotDir);
        File[] screenshots = dir.listFiles((d, name) -> name.endsWith(".png"));

        if (screenshots == null || screenshots.length == 0) {
            throw new IllegalArgumentException("No images found in the directory: " + screenshotDir);
        }

        // Sort files based on extracted timestamps
        Arrays.sort(screenshots, Comparator.comparingLong(file -> extractTimestamp(file.getName())));

        // Debug sorted order
        System.out.println("Sorted screenshots:");
        for (File screenshot : screenshots) {
            System.out.println(screenshot.getName() + " -> Timestamp: " + extractTimestamp(screenshot.getName()));
        }

        String fileListPath = screenshotDir + File.separator + "filelist.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileListPath))) {
            for (File screenshot : screenshots) {
                writer.write("file '" + screenshot.getAbsolutePath() + "'\n");
                writer.write("duration 2\n");
            }
            // Add the last frame for a pause at the end
            writer.write("file '" + screenshots[screenshots.length - 1].getAbsolutePath() + "'\n");
        }

        // Construct FFmpeg command
        String command = String.format(
            "%s -f concat -safe 0 -i \"%s\" -vf scale=trunc(iw/2)*2:trunc(ih/2)*2 -c:v libx264 -r %d -pix_fmt yuv420p \"%s\"",
            ffmpegPath, fileListPath, frameRate, outputVideoPath
        );

        // Execute FFmpeg command
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg process failed with exit code: " + exitCode);
        }
        System.out.println("Video generated successfully: " + outputVideoPath);
    }
}
