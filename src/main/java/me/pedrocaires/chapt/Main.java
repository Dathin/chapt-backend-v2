package me.pedrocaires.chapt;

import me.pedrocaires.chapt.springconfig.SpringContextConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main {

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
				System.out.println("Stopping server");
				server.stop();
				System.out.println("Server stopped");
			}
			catch (InterruptedException | IOException ex) {
				System.out.println("Unable to stop service");
			}
		}));
	}

}
