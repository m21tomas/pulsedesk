package com.example.pulsedesk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.pulsedesk.service.CommentService;

@Component
public class DataInitializer implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
	
	private final CommentService commentService;

    public DataInitializer(CommentService commentService) {
        this.commentService = commentService;
    }

	@Override
	public void run(String... args) throws Exception {	
		String[] testComments = {
	        "My account was suspended due to long inactivity. Can you admins please reactivate it again?",
	        "You app is nothing special. I saw many like this. Nothing impressive.",
	        "The purchased item does not match the order description and price in the email. What the hell?",
	        "Я оплатил годовую подписку через карту, но мой статус до сих пор 'Бесплатный'. Деньги списались,"
	        + " а доступа к премиум-функциям нет. Пожалуйста, проверьте мой платеж.",
	        "Būtų labai patogu, jei programėlė turėtų galimybę pridėti priminimus tiesiai prie užduočių. "
	        + "Dabar turiu naudoti kitą kalendorių, o tai užima daug laiko.",
	        "Sveiki, tiesiog norėjau pasidžiaugti, kad jūsų pagalbos komanda dirba labai greitai. Ačiū už pagalbą vakar!",
	        "фыва олдж лорипм куцкен.",
	        "I just wanted to say that the new dark mode looks absolutely stunning. Keep up the great work, team!",
	        "The export to PDF function is completely broken. Every time I click the button, "
	        + "the app hangs and I have to force close my browser. I've tried it on Chrome and Safari.",
	        "Oh god! I cannot sign in with my correctly password! During signing up I entered it correctly twice. "
	        + "What have you don with the app? Jesus!"		
	    };

	    for (String comment : testComments) {
	        try {
	            logger.info("Processing test comment: {}", comment.substring(0, Math.min(100, comment.length())) + "...");
	            commentService.submitComment(comment);
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            logger.error("Startup initialization interrupted");
	        } catch (Exception e) {
	            logger.error("AI triage failed for a comment, but skipping to next: {}", e.getMessage());
	        }
	    }
	    logger.info("Initial comments processing completed.");
	}
}
