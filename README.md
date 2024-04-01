# Multi LLM FrontEnd
Provides a single use front end for interacting with multiple LLM models on the backend via API

# LLM API Client App

This Android application allows you to interact with various Language Model (LLM) APIs, including Mistral, Gemini, and OpenAI. It provides a convenient way to send prompts and receive generated responses from these APIs based on the specified conditions.

## Features

- Call the Mistral API for general prompts
- Call the Gemini API when a picture file is attached to the prompt
- Call the OpenAI API (GPT-4) when the prompt ends with "(App 3)"
- Seamless switching between different LLM APIs based on the prompt and presence of a picture file
- Secure API key management using `BuildConfig`
- Efficient network calls using Retrofit and Gson for JSON parsing
- Proper error handling and logging for better debugging and monitoring

## Prerequisites

- Android Studio
- Android SDK (minimum SDK version 21)
- Java Development Kit (JDK) 8 or later

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/your-username/llm-api-client-app.git
```

2. Open the project in Android Studio.

3. Update the `build.gradle` file with your API keys:

```groovy
buildTypes {
    debug {
        buildConfigField "String", "API_BASE_URL", "\"https://api.example.com/\""
        buildConfigField "String", "API_KEY_MISTRAL", "\"your_mistral_api_key\""
        buildConfigField "String", "API_KEY_GEMINI", "\"your_gemini_api_key\""
        buildConfigField "String", "API_KEY_OPENAI", "\"your_openai_api_key\""
    }
}
```

Replace `your_mistral_api_key`, `your_gemini_api_key`, and `your_openai_api_key` with your actual API keys.

4. Build and run the application on an Android device or emulator.

## Usage

1. Enter a prompt in the provided input field.
2. Optionally, attach a picture file to the prompt if required.
3. If the prompt is intended for the GPT-4 API, append "(App 3)" to the prompt.
4. Click the "Generate" button to send the prompt to the selected LLM API.
5. The generated response will be displayed in the app.

## Configuration

The application uses `BuildConfig` to manage the API base URL and API keys. You can modify these values in the `build.gradle` file:

```groovy
buildTypes {
    debug {
        buildConfigField "String", "API_BASE_URL", "\"https://api.example.com/\""
        buildConfigField "String", "API_KEY_MISTRAL", "\"your_mistral_api_key\""
        buildConfigField "String", "API_KEY_GEMINI", "\"your_gemini_api_key\""
        buildConfigField "String", "API_KEY_OPENAI", "\"your_openai_api_key\""
    }
}
```

Make sure to update the `API_BASE_URL` with the correct base URL for the LLM APIs you are using.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
