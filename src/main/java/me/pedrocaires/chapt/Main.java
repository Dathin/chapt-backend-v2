package me.pedrocaires.chapt;

import me.pedrocaires.chapt.springconfig.SpringContextConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {

	public static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		var context = configureSpringContext();
		startWebSocket(context);
	}

	private static AnnotationConfigApplicationContext configureSpringContext() {
		return new AnnotationConfigApplicationContext(SpringContextConfig.class);
	}

	private static void startWebSocket(AnnotationConfigApplicationContext context) {
		var server = context.getBean(SimpleServer.class);
		closeServerOnShutdown(server);
		server.run();
	}

	private static void closeServerOnShutdown(SimpleServer server) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				LOGGER.info("Stopping server");
				server.stop();
				LOGGER.info("Server stopped");
			}
			catch (IOException ex) {
				LOGGER.error("IO error at shutdown", ex);
			}
			catch (InterruptedException ex) {
				LOGGER.error("Interrupted", ex);
				Thread.currentThread().interrupt();
			}
		}));
	}

}
