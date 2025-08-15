package com.drivncook.util;

import java.util.List;

public class NewsletterUtil {
    public static void sendNewsletter(List<String> emails, String subject, String content) {
        // Simulation d'envoi : impression console
        System.out.println("Newsletter envoyée à : " + emails);
        System.out.println("Sujet : " + subject);
        System.out.println("Contenu : " + content);
    }
}
