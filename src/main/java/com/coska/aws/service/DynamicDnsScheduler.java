package com.coska.aws.controller;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;


// @Component
public class DynamicDnsScheduler {

    private static final String GOOGLE_DOMAINS_DDNS_API_URL = "https://domains.google.com/nic/update";
    private final OkHttpClient httpClient = new OkHttpClient();

    @Value("${google.domains.hostname}")
    private String hostname;
    @Value("${google.domains.username}")
    private String username;
    @Value("${google.domains.password}")
    private String password;

    private boolean enabled = false;
    private boolean firstRun = true;

    @Scheduled(fixedDelay = 1000 * 60)
    public void updateDDNS() {

        if (firstRun) {
            firstRun = false;
            enabled = isEnabled();
        }

        if (enabled) {
            System.out.println("updateDDNS called");

            try {
                String currentIp = getCurrentIp();
                String dnsIp = getDnsIp();

                if (!currentIp.equals(dnsIp)) {
                    updateDnsRecord(currentIp);
                    System.out.println("DNS record updated to " + currentIp);
                } else {
                    System.out.println("DNS record is already up-to-date");
                }
            }
            catch (Exception e) {
                System.err.println("catch:" + e.getMessage() );
            }
        }
    }

    private boolean isEnabled() {
        return isNotNullOrEmpty(hostname) && isNotNullOrEmpty(username) && isNotNullOrEmpty(password);
    }

    private static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    private String getCurrentIp() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.ipify.org/")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private String getDnsIp() throws IOException {
        Request request = new Request.Builder()
                .url(GOOGLE_DOMAINS_DDNS_API_URL + "?hostname=" + hostname)
                .header("Authorization", Credentials.basic(username, password))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = Objects.requireNonNull(response.body()).string();
            String[] responseTokens = responseBody.split(" ");
            String responseCode = responseTokens[0];

            if (responseCode.startsWith("good") || responseCode.startsWith("nochg")) {
                return responseTokens[1];
            } else {
                throw new IOException("Failed to retrieve DNS IP: " + responseBody);
            }
        }
    }

    private void updateDnsRecord(String ip) throws IOException {
        Request request = new Request.Builder()
                .url(GOOGLE_DOMAINS_DDNS_API_URL + "?hostname=" + hostname + "&myip=" + ip)
                .header("Authorization", Credentials.basic(username, password))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = Objects.requireNonNull(response.body()).string();
            String[] responseTokens = responseBody.split(" ");
            String responseCode = responseTokens[0];

            if (!responseCode.startsWith("good")) {
                throw new IOException("Failed to update DNS record: " + responseBody);
            }
        }
    }

}
