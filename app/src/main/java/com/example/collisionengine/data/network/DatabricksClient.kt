package com.example.collisionengine.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object DatabricksClient {
    // Credentials from backend campus/.env
    private const val HOST = "https://dbc-d4fdaac8-e4fd.cloud.databricks.com"
    private const val TOKEN = "dapidbb67a284c4268db61ebb96d196cb996"
    private const val SPACE_ID = "01f1a511d4491c78a550a4ece2546db8"

    suspend fun askGenie(question: String): String = withContext(Dispatchers.IO) {
        try {
            // 1. Start Genie Conversation
            val startUrl = URL("$HOST/api/2.0/genie/spaces/$SPACE_ID/start-conversation")
            val startConn = startUrl.openConnection() as HttpURLConnection
            startConn.requestMethod = "POST"
            startConn.setRequestProperty("Authorization", "Bearer $TOKEN")
            startConn.setRequestProperty("Content-Type", "application/json")
            startConn.doOutput = true

            val startPayload = JSONObject().put("content", question)
            OutputStreamWriter(startConn.outputStream).use { it.write(startPayload.toString()) }

            if (startConn.responseCode !in 200..299) {
                return@withContext "Failed to start conversation. Error code: ${startConn.responseCode}"
            }

            val startResponse = startConn.inputStream.bufferedReader().use { it.readText() }
            val startJson = JSONObject(startResponse)
            val conversationId = startJson.optString("conversation_id")
            val messageId = startJson.optString("message_id", startJson.optString("id"))

            if (conversationId.isNullOrEmpty() || messageId.isNullOrEmpty()) {
                return@withContext "Invalid response from Databricks Genie API"
            }

            // 2. Poll for message completion
            val pollUrl = URL("$HOST/api/2.0/genie/spaces/$SPACE_ID/conversations/$conversationId/messages/$messageId")
            
            for (i in 0 until 50) {
                delay(2000)
                val pollConn = pollUrl.openConnection() as HttpURLConnection
                pollConn.requestMethod = "GET"
                pollConn.setRequestProperty("Authorization", "Bearer $TOKEN")
                pollConn.setRequestProperty("Content-Type", "application/json")

                if (pollConn.responseCode in 200..299) {
                    val pollResponse = pollConn.inputStream.bufferedReader().use { it.readText() }
                    val pollJson = JSONObject(pollResponse)
                    val status = pollJson.optString("status")

                    if (status == "COMPLETED" || status == "EXECUTED" || status == "SUCCESS") {
                        val attachments = pollJson.optJSONArray("attachments")
                        var answerText = "Completed without text response."
                        
                        if (attachments != null) {
                            for (j in 0 until attachments.length()) {
                                val att = attachments.getJSONObject(j)
                                val textData = att.optJSONObject("text")
                                if (textData != null && textData.has("content")) {
                                    if (textData.optString("purpose") == "TEXT_ATTACHMENT_PURPOSE_ANSWER") {
                                        answerText = textData.getString("content")
                                        break
                                    }
                                }
                            }
                            // Fallback if no exact match found
                            if (answerText == "Completed without text response.") {
                                for (j in 0 until attachments.length()) {
                                    val att = attachments.getJSONObject(j)
                                    val textData = att.optJSONObject("text")
                                    if (textData != null && textData.has("content")) {
                                        answerText = textData.getString("content")
                                        break
                                    }
                                }
                            }
                        }
                        return@withContext answerText
                    } else if (status == "FAILED" || status == "ERROR") {
                        return@withContext "Message processing failed."
                    }
                }
                pollConn.disconnect()
            }
            return@withContext "Request timed out while waiting for Databricks Genie."
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext "Error connecting to Databricks: ${e.message}"
        }
    }
}
