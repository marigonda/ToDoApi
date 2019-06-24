package todo.example.ToDoApi;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class ToDoApiApplication {

	// SL4J logger
	// Internal logger!
	private static final Logger logger = LoggerFactory.getLogger(ToDoApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ToDoApiApplication.class, args);

		logger.debug("ToDoApi Application started...");
	}

	// Time metrics in MICROMETER
	@Configuration
	public class TimedConfiguration {
		@Bean
		public TimedAspect timedAspect(MeterRegistry registry) {
			return new TimedAspect(registry);
		}
	}

	@Component
	public class CustomHealthIndicator extends AbstractHealthIndicator {

		@Override
		protected void doHealthCheck(Health.Builder builder) {
			builder.up()
					.withDetail("requests", 0)
					.withDetail("avg_request_time", 0);
		}
	}
}