package ai.spring.demo.ai.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

import org.springframework.web.client.RestClient;

@SpringBootApplication
@Theme(value = "customer-support-agent")
public class Application implements AppShellConfigurator {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).run(args);
	}

	/**
	 * This overrides the default factory bean for the REST client, and allows us to customize it.
	 * In this case we are adding an interceptor to log the request and response.
	 * The rest of this code comes from the default implementation in
	 *   org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration
	 */
	@Bean
	@Scope("prototype")
	RestClient.Builder restClientBuilder (RestClientBuilderConfigurer restClientBuilderConfigurer) {
		RestClient.Builder builder = RestClient.builder()
				.requestFactory(ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS))
				.requestInterceptor(new RestClientInterceptor());
		return restClientBuilderConfigurer.configure(builder);
	}

	// In the real world, ingesting documents would often happen separately, on a CI
	// server or similar.
	@Bean
	CommandLineRunner ingestTermOfServiceToVectorStore(
			EmbeddingModel embeddingModel, VectorStore vectorStore,
			@Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {

		return args -> {
			// Ingest the document into the vector store
			vectorStore.write(
					new TokenTextSplitter().transform(
							new TextReader(termsOfServiceDocs).read()));

			vectorStore.similaritySearch("Cancelling Bookings").forEach(doc -> {
				logger.info("Similar Document: {}", doc.getContent());
			});
		};
	}

	@Bean
	public EmbeddingModel embeddingModel(OllamaApi ollamaApi, CommonProps commonProps) {
		return new OllamaEmbeddingModel(ollamaApi, OllamaOptions.create().withModel(commonProps.ollamaAiModel));
	}

	@Bean
	public VectorStore vectorStore(EmbeddingModel embeddingModel) {
		return new SimpleVectorStore(embeddingModel);
	}

	@Bean
	public ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}
}
