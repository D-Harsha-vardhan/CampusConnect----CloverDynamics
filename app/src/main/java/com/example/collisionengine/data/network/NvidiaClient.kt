package com.example.collisionengine.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object NvidiaClient {
    // Optional: Add your Nvidia API Key here if you want to use the LLM for name extraction
    private const val NVIDIA_API_KEY = "add your nvidid api key"
    
    suspend fun extractNames(text: String): List<String> = withContext(Dispatchers.IO) {
        if (NVIDIA_API_KEY.isBlank()) {
            return@withContext extractNamesRegex(text)
        }
        
        try {
            val url = URL("https://integrate.api.nvidia.com/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $NVIDIA_API_KEY")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            val systemPrompt = "You are a data extraction tool. Extract human names from the text and return them as a comma-separated list. You must wrap your final comma-separated list in <names> and </names> tags."
            
            val messages = JSONArray()
                .put(JSONObject().put("role", "system").put("content", systemPrompt))
                .put(JSONObject().put("role", "user").put("content", text))

            val payload = JSONObject()
                .put("model", "nvidia/nemotron-3.5-lightning-30b-a3b")
                .put("messages", messages)
                .put("max_tokens", 1024)
                .put("temperature", 0.0)

            OutputStreamWriter(conn.outputStream).use { it.write(payload.toString()) }

            if (conn.responseCode in 200..299) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(response)
                val choices = json.optJSONArray("choices")
                if (choices != null && choices.length() > 0) {
                    val message = choices.getJSONObject(0).optJSONObject("message")
                    val content = message?.optString("content") ?: ""
                    if (content.isNotBlank()) {
                        // Extract text between <names> and </names> if present
                        val regex = Regex("""<names>(.*?)</names>""", RegexOption.DOT_MATCHES_ALL)
                        val matchResult = regex.find(content)
                        val namesList = if (matchResult != null) {
                            matchResult.groupValues[1]
                        } else {
                            content
                        }
                        return@withContext namesList.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        // Fallback to Regex if API fails
        return@withContext extractNamesRegex(text)
    }

    private fun extractNamesRegex(text: String): List<String> {
        // Look for names bolded by Databricks Genie: **First Last**
        val regex = Regex("""\*\*(.*?)\*\*""")
        val matches = regex.findAll(text)
        val extracted = matches.map { it.groupValues[1].trim() }.toList()
        
        // Filter out obvious non-names like "fake news detection"
        return extracted.filter { name -> 
            val words = name.split(" ")
            words.size in 2..3 && words.all { it.isNotEmpty() && it.first().isUpperCase() }
        }
    }
}
