package com.example.collisionengine.data.network

import android.content.Context
import com.example.collisionengine.data.model.ProfileMatch
import com.example.collisionengine.data.models.Faculty
import com.example.collisionengine.data.models.Student
import kotlinx.serialization.json.Json
import java.io.InputStreamReader

object LocalDatasetClient {
    private var allStudents: List<Student> = emptyList()
    private var allFaculty: List<Faculty> = emptyList()

    private val jsonFormat = Json { ignoreUnknownKeys = true }

    fun init(context: Context) {
        try {
            // Load Students
            context.assets.open("Students.json").use { inputStream ->
                val jsonString = InputStreamReader(inputStream).readText()
                allStudents = jsonFormat.decodeFromString(jsonString)
            }
            // Load Faculty
            context.assets.open("Faculty.json").use { inputStream ->
                val jsonString = InputStreamReader(inputStream).readText()
                allFaculty = jsonFormat.decodeFromString(jsonString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun normalizeName(name: String?): String {
        if (name == null) return ""
        return name
            .lowercase()
            .replace("dr.", "")
            .replace("dr ", "")
            .replace("prof.", "")
            .replace("prof ", "")
            .trim()
    }

    fun searchProfilesByNames(names: List<String>): List<ProfileMatch> {
        if (names.isEmpty()) return emptyList()

        val normalizedNames = names.map { normalizeName(it) }
        val matches = mutableListOf<ProfileMatch>()

        // Search in Students
        val matchedStudents = allStudents.filter { normalizeName(it.name) in normalizedNames }
        matches.addAll(matchedStudents.map { student ->
            ProfileMatch(
                name = student.name ?: "Unknown",
                role = "${student.department ?: ""} • ${student.year ?: ""}",
                matchReasonTitle = "Related to your query",
                matchReasonText = student.projects?.takeIf { it.isNotBlank() } ?: student.skills ?: "",
                tags = student.skills?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
            )
        })

        // Search in Faculty
        val matchedFaculty = allFaculty.filter { normalizeName(it.name) in normalizedNames }
        matches.addAll(matchedFaculty.map { faculty ->
            ProfileMatch(
                name = faculty.name ?: "Unknown",
                role = "Faculty • ${faculty.department ?: ""}",
                matchReasonTitle = "Related to your query",
                matchReasonText = faculty.expertise ?: "",
                tags = faculty.researchInterests?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
            )
        })

        return matches
    }
}
