// LLMApiClient.java
import android.content.Context;
import android.util.Log;

import com.example.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LLMApiClient {
    private static final String TAG = "LLMApiClient";

    private Retrofit retrofit;
    private MistralService mistralService;
    private GeminiService geminiService;
    private OpenAIService openAIService;

    public LLMApiClient(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mistralService = retrofit.create(MistralService.class);
        geminiService = retrofit.create(GeminiService.class);
        openAIService = retrofit.create(OpenAIService.class);
    }

    public void callLLMApi(String prompt, File pictureFile, Callback<JsonObject> callback) {
        String apiKey = getApiKey(prompt, pictureFile);

        if (pictureFile != null) {
            callGeminiApi(apiKey, prompt, pictureFile, callback);
        } else if (prompt.endsWith("(App 3)")) {
            String modifiedPrompt = prompt.replace("(App 3)", "").trim();
            callOpenAIApi(apiKey, modifiedPrompt, callback);
        } else {
            callMistralApi(apiKey, prompt, callback);
        }
    }

    private void callMistralApi(String apiKey, String prompt, Callback<JsonObject> callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), createRequestBody(prompt));
        mistralService.generate("Bearer " + apiKey, body).enqueue(new RetrofitCallback(callback));
    }

    private void callGeminiApi(String apiKey, String prompt, File pictureFile, Callback<JsonObject> callback) {
        RequestBody promptBody = RequestBody.create(MediaType.parse("text/plain"), prompt);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), pictureFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", pictureFile.getName(), requestFile);
        geminiService.generate("Bearer " + apiKey, promptBody, body).enqueue(new RetrofitCallback(callback));
    }

    private void callOpenAIApi(String apiKey, String prompt, Callback<JsonObject> callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), createRequestBody(prompt));
        openAIService.generate("Bearer " + apiKey, body).enqueue(new RetrofitCallback(callback));
    }

    private String getApiKey(String prompt, File pictureFile) {
        if (pictureFile != null) {
            return BuildConfig.API_KEY_GEMINI;
        } else if (prompt.endsWith("(App 3)")) {
            return BuildConfig.API_KEY_OPENAI;
        } else {
            return BuildConfig.API_KEY_MISTRAL;
        }
    }

    private String createRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("prompt", prompt);
        return new Gson().toJson(requestBody);
    }

    private static class RetrofitCallback implements Callback<JsonObject> {
        private Callback<JsonObject> callback;

        public RetrofitCallback(Callback<JsonObject> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body() != null) {
                callback.onResponse(call, response);
            } else {
                onFailure(call, new RuntimeException("API call was not successful."));
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "API request failed: ", t);
            callback.onFailure(call, t);
        }
    }
}

// Retrofit Service Interfaces
interface MistralService {
    @POST("/generate")
    Call<JsonObject> generate(@Header("Authorization") String apiKey, @Body RequestBody body);
}

interface GeminiService {
    @Multipart
    @POST("/generate")
    Call<JsonObject> generate(@Header("Authorization") String apiKey, @Part("prompt") RequestBody prompt, @Part MultipartBody.Part file);
}

interface OpenAIService {
    @POST("/generate")
    Call<JsonObject> generate(@Header("Authorization") String apiKey, @Body RequestBody body);
}
