/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.SpringAiApplication;
import com.example.chat.ActorsFilms;
import com.example.tool.MockWeatherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexandros Pappas
 *
 * The DeepSeek API uses an API format compatible with OpenAI, allowing developers to
 * easily integrate it into existing systems that use the OpenAI SDK5.
 *
 * For more information on DeepSeek behavior, refer to its API documentation:
 * <a href="https://api-docs.deepseek.com/">DeepSeek API</a>
 */
@SpringBootTest(classes = SpringAiApplication.class)
@Slf4j
class DeepSeekWithOpenAiChatModelIT {

    private static final Logger logger = LoggerFactory.getLogger(DeepSeekWithOpenAiChatModelIT.class);

    @Value("classpath:/prompts/system-message.st")
    private Resource systemResource;

    @Autowired
    private OpenAiChatModel chatModel;

    @Test
    public void roleTest() {
        UserMessage userMessage = new UserMessage(
                "Tell me about 3 famous pirates from the Golden Age of Piracy and what they did.");
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.systemResource);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "Bob", "voice", "pirate"));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ChatResponse response = this.chatModel.call(prompt);
        assertThat(response.getResults()).hasSize(1);
        String text = response.getResults().get(0).getOutput().getText();
        log.info(text);
        assertThat(text).contains("Blackbeard");
    }

    @Test
    void streamRoleTest() {
        UserMessage userMessage = new UserMessage(
                "Tell me about 3 famous pirates from the Golden Age of Piracy and what they did.");
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.systemResource);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "Bob", "voice", "pirate"));
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        Flux<ChatResponse> flux = this.chatModel.stream(prompt);

        List<ChatResponse> responses = flux.collectList().block();
        assertThat(responses.size()).isGreaterThan(1);

        String stitchedResponseContent = responses.stream()
                .map(ChatResponse::getResults)
                .flatMap(List::stream)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .collect(Collectors.joining());

        assertThat(stitchedResponseContent).contains("Blackbeard");
    }

    @Test
    void streamingWithTokenUsage() {
        var promptOptions = OpenAiChatOptions.builder().streamUsage(true).seed(1).build();

        var prompt = new Prompt("List two colors of the Polish flag. Be brief.", promptOptions);

        var streamingTokenUsage = this.chatModel.stream(prompt).blockLast().getMetadata().getUsage();
        var referenceTokenUsage = this.chatModel.call(prompt).getMetadata().getUsage();

        assertThat(streamingTokenUsage.getPromptTokens()).isGreaterThan(0);
        assertThat(streamingTokenUsage.getCompletionTokens()).isGreaterThan(0);
        assertThat(streamingTokenUsage.getTotalTokens()).isGreaterThan(0);

        assertThat(streamingTokenUsage.getPromptTokens()).isEqualTo(referenceTokenUsage.getPromptTokens());
        assertThat(streamingTokenUsage.getCompletionTokens()).isEqualTo(referenceTokenUsage.getCompletionTokens());
        assertThat(streamingTokenUsage.getTotalTokens()).isEqualTo(referenceTokenUsage.getTotalTokens());

    }

    @Test
    void listOutputConverter() {
        DefaultConversionService conversionService = new DefaultConversionService();
        ListOutputConverter outputConverter = new ListOutputConverter(conversionService);

        String format = outputConverter.getFormat();
        String template = """
				List five {subject}
				{format}
				""";
        PromptTemplate promptTemplate = new PromptTemplate(template,
                Map.of("subject", "ice cream flavors", "format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatModel.call(prompt).getResult();

        List<String> list = outputConverter.convert(generation.getOutput().getText());
        assertThat(list).hasSize(5);

    }

    @Test
    void mapOutputConverter() {
        MapOutputConverter outputConverter = new MapOutputConverter();

        String format = outputConverter.getFormat();
        String template = """
				Provide me a List of {subject}
				{format}
				""";
        PromptTemplate promptTemplate = new PromptTemplate(template,
                Map.of("subject", "numbers from 1 to 9 under they key name 'numbers'", "format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatModel.call(prompt).getResult();

        Map<String, Object> result = outputConverter.convert(generation.getOutput().getText());
        assertThat(result.get("numbers")).isEqualTo(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    }

    @Test
    void beanOutputConverter() {

        BeanOutputConverter<ActorsFilms> outputConverter = new BeanOutputConverter<>(ActorsFilms.class);

        String format = outputConverter.getFormat();
        String template = """
				Generate the filmography for a random actor.
				{format}
				""";
        PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatModel.call(prompt).getResult();

        ActorsFilms actorsFilms = outputConverter.convert(generation.getOutput().getText());
        assertThat(actorsFilms.getActor()).isNotEmpty();
    }

    @Test
    void beanOutputConverterRecords() {

        BeanOutputConverter<DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord> outputConverter = new BeanOutputConverter<>(
                DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord.class);

        String format = outputConverter.getFormat();
        String template = """
				Generate the filmography of 5 movies for Tom Hanks.
				{format}
				""";
        PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());
        Generation generation = this.chatModel.call(prompt).getResult();

        DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord actorsFilms = outputConverter
                .convert(generation.getOutput().getText());
        logger.info("{}", actorsFilms);
        assertThat(actorsFilms.actor()).isEqualTo("Tom Hanks");
        assertThat(actorsFilms.movies()).hasSize(5);
    }

    @Test
    void beanStreamOutputConverterRecords() {

        BeanOutputConverter<DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord> outputConverter = new BeanOutputConverter<>(
                DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord.class);

        String format = outputConverter.getFormat();
        String template = """
				Generate the filmography of 5 movies for Tom Hanks.
				{format}
				""";
        PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("format", format));
        Prompt prompt = new Prompt(promptTemplate.createMessage());

        String generationTextFromStream = this.chatModel.stream(prompt)
                .collectList()
                .block()
                .stream()
                .map(ChatResponse::getResults)
                .flatMap(List::stream)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .collect(Collectors.joining());

        DeepSeekWithOpenAiChatModelIT.ActorsFilmsRecord actorsFilms = outputConverter.convert(generationTextFromStream);
        logger.info("{}", actorsFilms);
        assertThat(actorsFilms.actor()).isEqualTo("Tom Hanks");
        assertThat(actorsFilms.movies()).hasSize(5);
    }

    @Test
    @Disabled("The current version of the deepseek-chat model's Function Calling capability is unstable, which may result in looped calls or empty responses.")
    void functionCallTest() {

        UserMessage userMessage = new UserMessage("What's the weather like in San Francisco, Tokyo, and Paris?");

        List<Message> messages = new ArrayList<>(List.of(userMessage));

        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("getCurrentWeather", new MockWeatherService())
                        .description("Get the weather in location")
                        .inputType(MockWeatherService.Request.class)
                        .build()))
                .build();

        ChatResponse response = this.chatModel.call(new Prompt(messages, promptOptions));

        logger.info("Response: {}", response);

        assertThat(response.getResult().getOutput().getText()).contains("30", "10", "15");
    }

    @Test
    @Disabled("The current version of the deepseek-chat model's Function Calling capability is unstable, which may result in looped calls or empty responses.")
    void streamFunctionCallTest() {

        UserMessage userMessage = new UserMessage(
                "What's the weather like in San Francisco, Tokyo, and Paris? Return the temperature in Celsius.");

        List<Message> messages = new ArrayList<>(List.of(userMessage));

        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
                        .function("getCurrentWeather", new MockWeatherService())
                        .description("Get the weather in location")
                        .inputType(MockWeatherService.Request.class)
                        .build()))
                .build();

        Flux<ChatResponse> response = this.chatModel.stream(new Prompt(messages, promptOptions));

        String content = response.collectList()
                .block()
                .stream()
                .map(ChatResponse::getResults)
                .flatMap(List::stream)
                .map(Generation::getOutput)
                .map(AssistantMessage::getText)
                .collect(Collectors.joining());
        logger.info("Response: {}", content);

        assertThat(content).contains("30", "10", "15");
    }

    @ParameterizedTest(name = "{0} : {displayName} ")
    @ValueSource(strings = { "deepseek-chat", "deepseek-reasoner" })
    void validateCallResponseMetadata(String model) {
        // @formatter:off
        ChatResponse response = ChatClient.create(this.chatModel).prompt()
                .options(OpenAiChatOptions.builder().model(model).build())
                .user("Tell me about 3 famous pirates from the Golden Age of Piracy and what they did")
                .call()
                .chatResponse();
        // @formatter:on

        logger.info(response.toString());
        assertThat(response.getMetadata().getId()).isNotEmpty();
        assertThat(response.getMetadata().getModel()).containsIgnoringCase(model);
        assertThat(response.getMetadata().getUsage().getPromptTokens()).isPositive();
        assertThat(response.getMetadata().getUsage().getCompletionTokens()).isPositive();
        assertThat(response.getMetadata().getUsage().getTotalTokens()).isPositive();
    }

    record ActorsFilmsRecord(String actor, List<String> movies) {

    }

}