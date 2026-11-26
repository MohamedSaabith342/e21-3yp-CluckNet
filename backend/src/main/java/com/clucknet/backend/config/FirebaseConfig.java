package com.clucknet.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                File file = new File(credentialsPath);

                if (!file.isFile()) {
                    log.warn("Firebase credentials not found. Push notifications will be disabled.");
                    return;
                }

                try (InputStream serviceAccount = new FileInputStream(file)) {

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    FirebaseApp.initializeApp(options);

                    log.info("Firebase Application has been initialized successfully.");
                }
            }
        } catch (IOException e) {
            log.error("Error initializing Firebase App: {}", e.getMessage());
        }
    }
}