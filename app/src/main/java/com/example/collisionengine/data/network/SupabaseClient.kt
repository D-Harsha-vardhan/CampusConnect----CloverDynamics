package com.example.collisionengine.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    // Paste your Project URL here
    private const val SUPABASE_URL = "add your supabase url"
    
    // Paste your Anon Key here
    private const val SUPABASE_KEY = "add your supabase key"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }
    
    private fun normalizeName(name: String?): String {
        return name?.replace("Dr. ", "", ignoreCase = true)
            ?.replace("Prof. ", "", ignoreCase = true)
            ?.trim()
            ?.lowercase() ?: ""
    }

    suspend fun searchProfilesByNames(names: List<String>): List<com.example.collisionengine.data.model.ProfileMatch> {
        if (names.isEmpty()) return emptyList()
        
        val normalizedNames = names.map { normalizeName(it) }
        val matches = mutableListOf<com.example.collisionengine.data.model.ProfileMatch>()
        
        // Fetch Students
        try {
            val allStudents = client.from("Student").select().decodeList<com.example.collisionengine.data.models.Student>()
            val students = allStudents.filter { normalizeName(it.name) in normalizedNames }
            matches.addAll(students.map { student ->
                com.example.collisionengine.data.model.ProfileMatch(
                    name = student.name ?: "Unknown",
                    role = "${student.department ?: ""} • ${student.year ?: ""}",
                    matchReasonTitle = "Related to your query",
                    matchReasonText = student.projects?.takeIf { it.isNotBlank() } ?: student.skills ?: "",
                    tags = student.skills?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val allStudentsFallback = client.from("students").select().decodeList<com.example.collisionengine.data.models.Student>()
                val students = allStudentsFallback.filter { normalizeName(it.name) in normalizedNames }
                matches.addAll(students.map { student ->
                    com.example.collisionengine.data.model.ProfileMatch(
                        name = student.name ?: "Unknown",
                        role = "${student.department ?: ""} • ${student.year ?: ""}",
                        matchReasonTitle = "Related to your query",
                        matchReasonText = student.projects?.takeIf { it.isNotBlank() } ?: student.skills ?: "",
                        tags = student.skills?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
                    )
                })
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
        
        // Fetch Faculty
        try {
            val allFaculty = client.from("Faculty").select().decodeList<com.example.collisionengine.data.models.Faculty>()
            val facultyList = allFaculty.filter { normalizeName(it.name) in normalizedNames }
            matches.addAll(facultyList.map { faculty ->
                com.example.collisionengine.data.model.ProfileMatch(
                    name = faculty.name ?: "Unknown",
                    role = "Faculty • ${faculty.department ?: ""}",
                    matchReasonTitle = "Related to your query",
                    matchReasonText = faculty.expertise ?: "",
                    tags = faculty.researchInterests?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            try {
                val allFacultyFallback = client.from("faculty").select().decodeList<com.example.collisionengine.data.models.Faculty>()
                val facultyList = allFacultyFallback.filter { normalizeName(it.name) in normalizedNames }
                matches.addAll(facultyList.map { faculty ->
                    com.example.collisionengine.data.model.ProfileMatch(
                        name = faculty.name ?: "Unknown",
                        role = "Faculty • ${faculty.department ?: ""}",
                        matchReasonTitle = "Related to your query",
                        matchReasonText = faculty.expertise ?: "",
                        tags = faculty.researchInterests?.split(",")?.map { it.trim() }?.take(4) ?: emptyList()
                    )
                })
            } catch (e2: Exception) {
                e2.printStackTrace()
            }
        }
        
        return matches
    }
}
