package ai.spring.demo.ai.playground;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommonProps {

	private static final String MODEL_LLAMA3_2 = OllamaModel.LLAMA3_2.id();
	private static final String MODEL_LLAMA3_1_70B = "llama3.1:70b";
	private static final String OLLAMA_MODEL = MODEL_LLAMA3_1_70B;

    public final String ollamaAiModel;

    public CommonProps(@Value("${spring.ai.ollama.chat.options.model}") String ollamaAiModel) {
        if (StringUtils.isEmpty(ollamaAiModel)) ollamaAiModel = OllamaModel.MISTRAL.id();
        this.ollamaAiModel = ollamaAiModel;
    }
    
}
