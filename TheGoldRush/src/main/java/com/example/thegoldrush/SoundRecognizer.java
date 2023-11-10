package com.example.thegoldrush;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class SoundRecognizer {
    private boolean soundRecognitionRunning = false;
    double intensity;

    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 4096;

    public SoundRecognizer() {

    }

    public boolean startSoundRecognition(double soundThreshold) {

        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Microphone not supported.");
                return false;
            }

            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            byte[] buffer = new byte[BUFFER_SIZE];

            soundRecognitionRunning = true;

            while (soundRecognitionRunning) {
                int bytesRead = line.read(buffer, 0, buffer.length);

                // Calculate sound intensity
                double soundIntensity = calculateSoundIntensity(buffer, bytesRead);

                // Update sound intensity property
                setSoundIntensity(soundIntensity);


                if (soundIntensity >= soundThreshold) {
                    return true;
                }

                Thread.sleep(100); // Interval between audio readings
            }

            line.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Door not opened
    }

    private void setSoundIntensity(double intensity) {
        this.intensity=intensity;
    }

    public double getIntensity() {
        return intensity;
    }

    private double calculateSoundIntensity(byte[] buffer, int bytesRead) {
        double sum = 0.0;
        for (int i = 0; i < bytesRead; i += 2) {
            short sample = (short) ((buffer[i] & 0xFF) | (buffer[i + 1] << 8));
            sum += Math.abs(sample);
        }
        double average = sum / (bytesRead / 2);
        return average;
    }
}
