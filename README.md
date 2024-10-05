# AI powered expert system demo

Spring AI re-implementation of https://github.com/marcushellberg/java-ai-playground

This app shows how you can use [Spring AI](https://github.com/spring-projects/spring-ai) to build an AI-powered system that:

- Has access to terms and conditions (retrieval augmented generation, RAG)
- Can access tools (Java methods) to perform actions (Function Calling)
- Uses an LLM to interact with the user

![alt text](diagram.jpg)

## Requirements

- Java 17+
- OpenAI API key in `OPENAI_API_KEY` environment variable

## Running

Run the app by running `Application.java` in your IDE or `mvn` in the command line.

### With OpenAI Chat

Add to the POM the Spring AI Open AI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>
```

Add the OpenAI configuraiton to the `applicaiton.properties`:

```
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o
```

### WIth VertexAI Geminie Chat

Add to the POM the Spring AI VertexAI Gemeni and Onnx Transfomer Embedding boot starters:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-vertex-ai-gemini-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the VertexAI Gemini configuraiton to the `applicaiton.properties`:

```
spring.ai.vertex.ai.gemini.project-id=${VERTEX_AI_GEMINI_PROJECT_ID}
spring.ai.vertex.ai.gemini.location=${VERTEX_AI_GEMINI_LOCATION}
spring.ai.vertex.ai.gemini.chat.options.model=gemini-1.5-pro-001
# spring.ai.vertex.ai.gemini.chat.options.model=gemini-1.5-flash-001
```

### With Azure OpenAI Chat

Add to the POM the Spring AI Azure OpenAI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-azure-openai-spring-boot-starter</artifactId>
</dependency>
```

Add the Azure OpenAI configuraiton to the `applicaiton.properties`:

```
spring.ai.azure.openai.api-key=${AZURE_OPENAI_API_KEY}
spring.ai.azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT}
spring.ai.azure.openai.chat.options.deployment-name=gpt-4o
```

### With Groq Chat

It reuses the OpenAI Chat client but ponted to the Groq endpont

Add to the POM the Spring AI Open AI boot starter:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-openai-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the Groq configuraiton to the `applicaiton.properties`:

```
spring.ai.openai.api-key=${GROQ_API_KEY}
spring.ai.openai.base-url=https://api.groq.com/openai
spring.ai.openai.chat.options.model=llama3-70b-8192
```

### With Anthropic Claude 3 Chat

Add to the POM the Spring AI Anthropic Claude and Onnx Transfomer Embedding boot starters:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-anthropic-spring-boot-starter</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-transformers-spring-boot-starter</artifactId>
</dependency>
```

Add the Anthropic configuraiton to the `applicaiton.properties`:

```
spring.ai.anthropic.api-key=${ANTHROPIC_API_KEY}
spring.ai.openai.chat.options.model=llama3-70b-8192
spring.ai.anthropic.chat.options.model=claude-3-5-sonnet-20240620
```


## Build Jar

```shell
./mvnw clean install -Pproduction
```

```shell
java -jar ./target/playground-flight-booking-0.0.1-SNAPSHOT.jar
```


```
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres ankane/pgvector
```

### Test: qwen2.5:72b
- Hi! I'm John Doe. My booking number is 101. Can I cancel my order?
  - (Took 91790.259 ms) Hi John! I see that your booking is confirmed for today's flight from Frankfurt (FRA) to San Jose (SJC) in Business Class. According to our cancellation policy, you can cancel your booking up to 48 hours before the flight, and there is a $25 cancellation fee for Business Class. Would you like to proceed with the cancellation? If so, I'll need your confirmation to go ahead.
- OK
  - (Took 102499.510 ms) I'm sorry, John, but it looks like your booking can't be canceled within 48 hours of the flight. According to our policy, you need to cancel at least 48 hours before the flight to avoid any issues. Is there anything else I can assist you with? Maybe we can look into rescheduling your flight to a later date if that works for you! 😊
- Yes
  - Hi John! Thanks for your response. Since it's currently less than 48 hours before your flight, we unfortunately cannot cancel your booking without incurring a penalty. However, I can help you reschedule your flight to a later date if you'd like. There is no change fee for Business Class bookings, so this would be a good option to avoid any additional charges. Would you like to proceed with rescheduling your flight? If so, could you please provide me with the new preferred travel dates and times? Looking forward to helping you out! 😊
- 2024-10-12
  - (Took 140305.120 ms) I'm sorry, John, but it looks like we can't change your booking to a different date within 24 hours of your current flight. According to our policy, changes must be made at least 24 hours before the scheduled departure. Is there anything else I can assist you with? Maybe we can explore other options or help you with any other questions you might have! 😊
- Tell me joke
  - Sure, I'd love to share a joke with you! Here it goes: `Why don’t scientists trust atoms? Because they make up everything!` Hope that brought a smile to your face! Is there anything else I can assist you with today? 😊

### Test: llama3.1:70b
- Hi! I'm John Doe. My booking number is 101. Can I cancel my order?
  - (Took 59523.358 ms) Hi John! I've checked on your booking details. According to our terms and conditions, you can cancel your order up to 48 hours before the flight. However, please note that a cancellation fee of $75 will apply since you're booked in Economy class. Before proceeding with the cancellation, I'd like to confirm that you'd like to cancel your booking (101) from SJC to LHR on October 5th. If so, please let me know, and I'll guide you through the next steps.
- OK
  - (Took 63087.560 ms) Sorry John, unfortunately, I need to inform you that since your flight is today (October 5th), it's too close to the departure time and falls outside the cancellation window. According to our terms and conditions, cancellations are only allowed up to 48 hours before the flight. If you'd like to discuss other options or have any further questions, please feel free to ask!
- Yes
  - (Took 54887.253 ms) Hi John! I've checked on your booking details. According to our terms and conditions, you can cancel your order up to 48 hours before the flight. However, please note that a cancellation fee of $75 will apply since you're booked in Economy class. Before proceeding with the cancellation, I'd like to confirm that you'd like to cancel your booking (101) from SJC to LHR on October 5th. If so, please let me know, and I'll guide you through the next steps.
- I want to change my travel date to 2024-10-12
  - (Took 53939.525 ms) Hi John! I've checked on your booking details. According to our terms and conditions, you can change your travel date up to 24 hours before the flight. However, please note that since your original flight is today (October 5th), it's too close to the departure time and falls outside the change window. If you'd like to discuss other options or have any further questions, please feel free to ask!
- Just change date to 2024-10-12 now
  - (Took 54457.788 ms) Hi John! Unfortunately, I need to inform you that since your original flight is today (October 5th), it's too close to the departure time and falls outside the change window. If you'd like to discuss other options or have any further questions, please feel free to ask!
- Tell me joke
  - No function callback found for function name: I'm afraid I can't answer this question
  - No function callback found for function name: None
