package com.example.collisionengine.data.repository

import android.content.Context
import com.example.collisionengine.data.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

class MockCollisionRepository(private val context: Context) {

    private val gson = Gson()
    
    // In-memory mock database
    private var students: List<Student> = emptyList()
    private var projects: List<Project> = emptyList()
    private var research: List<Research> = emptyList()
    private var placements: List<Placement> = emptyList()
    private var faculty: List<Faculty> = emptyList()

    init {
        loadData()
    }

    private fun loadData() {
        students = loadJson("Students.json", object : TypeToken<List<Student>>() {})
        projects = loadJson("Projects.json", object : TypeToken<List<Project>>() {})
        research = loadJson("Research.json", object : TypeToken<List<Research>>() {})
        placements = loadJson("Placement.json", object : TypeToken<List<Placement>>() {})
        faculty = loadJson("Faculty.json", object : TypeToken<List<Faculty>>() {})
    }

    private fun <T> loadJson(filename: String, typeToken: TypeToken<List<T>>): List<T> {
        return try {
            val inputStream = context.assets.open(filename)
            val reader = InputStreamReader(inputStream)
            val list = gson.fromJson<List<T>>(reader, typeToken.type)
            reader.close()
            list ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Mock Intelligence for Phase 6
    // It basically does a glorified keyword search over the loaded JSON
    fun findCollisions(query: String, type: String): List<CollisionMatch> {
        val keywords = query.lowercase().split(" ", ",", ".", "?").filter { it.length > 3 }
        val matches = mutableListOf<CollisionMatch>()

        if (type == "Research") {
            // Find students who have relevant skills/projects/research
            students.forEach { student ->
                var score = 0
                var matchReason = ""
                
                val allText = "${student.skills} ${student.researchInterests} ${student.projects}".lowercase()
                keywords.forEach { keyword ->
                    if (allText.contains(keyword)) score += 20
                }
                
                if (score > 0) {
                    val relatedProject = projects.find { it.studentId == student.studentId }
                    if (relatedProject != null) {
                        val projText = "${relatedProject.technologies} ${relatedProject.domain}".lowercase()
                        keywords.forEach { keyword -> if (projText.contains(keyword)) score += 30 }
                        matchReason = "Built project: '${relatedProject.title}' using ${relatedProject.technologies.split(",").take(2).joinToString(", ")}"
                    } else {
                        matchReason = "Has skills in ${student.skills.split(",").take(3).joinToString(", ")}"
                    }
                    matches.add(CollisionMatch(student.name, "Student (${student.year})", matchReason, score.coerceAtMost(99)))
                }
            }
            
            // Find faculty
            faculty.forEach { fac ->
                var score = 0
                val allText = "${fac.researchInterests} ${fac.expertise}".lowercase()
                keywords.forEach { keyword ->
                    if (allText.contains(keyword)) score += 35
                }
                if (score > 0) {
                    matches.add(CollisionMatch(fac.name, "Faculty (${fac.department})", "Expertise in ${fac.expertise.split(",").take(2).joinToString(", ")}", score.coerceAtMost(98)))
                }
            }

        } else if (type == "Placement") {
            // Find students who interviewed at companies or roles
            placements.forEach { placement ->
                var score = 0
                val allText = "${placement.company} ${placement.role} ${placement.skills}".lowercase()
                keywords.forEach { keyword ->
                    if (allText.contains(keyword)) score += 40
                }
                
                if (score > 0) {
                    val student = students.find { it.studentId == placement.studentId }
                    val name = student?.name ?: "Unknown Peer"
                    matches.add(CollisionMatch(name, "Peer (${placement.outcome})", "Interviewed for ${placement.role} at ${placement.company}", score.coerceAtMost(95)))
                }
            }
        }
        
        return matches.sortedByDescending { it.score }.take(10)
    }
}
