package com.example.collisionengine.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    @SerializedName("student_id") @SerialName("student_id") val studentId: String? = "",
    val name: String? = "",
    val department: String? = "",
    val year: String? = "",
    val skills: String? = "",
    val projects: String? = "",
    @SerializedName("research_interests") @SerialName("research_interests") val researchInterests: String? = "",
    val certifications: String? = "",
    @SerializedName("career_interests") @SerialName("career_interests") val careerInterests: String? = ""
)

@Serializable
data class Project(
    @SerializedName("project_id") @SerialName("project_id") val projectId: String? = "",
    @SerializedName("student_id") @SerialName("student_id") val studentId: String? = "",
    val title: String? = "",
    val description: String? = "",
    val technologies: String? = "",
    val domain: String? = "",
    val methodology: String? = "",
    val year: String? = ""
)

@Serializable
data class Research(
    @SerializedName("research_id") @SerialName("research_id") val researchId: String? = "",
    @SerializedName("person_id") @SerialName("person_id") val personId: String? = "",
    val title: String? = "",
    val abstract: String? = "",
    @SerializedName("research_area") @SerialName("research_area") val researchArea: String? = "",
    val methodologies: String? = "",
    val publication: String? = ""
)

@Serializable
data class Placement(
    @SerializedName("placement_id") @SerialName("placement_id") val placementId: String? = "",
    @SerializedName("student_id") @SerialName("student_id") val studentId: String? = "",
    val company: String? = "",
    val role: String? = "",
    val skills: String? = "",
    @SerializedName("interview_topics") @SerialName("interview_topics") val interviewTopics: String? = "",
    val outcome: String? = "",
    val year: String? = ""
)

@Serializable
data class Faculty(
    @SerializedName("faculty_id") @SerialName("faculty_id") val facultyId: String? = "",
    val name: String? = "",
    val department: String? = "",
    @SerializedName("research_interests") @SerialName("research_interests") val researchInterests: String? = "",
    val publications: String? = "",
    val expertise: String? = ""
)

// UI wrapper for matches
@Serializable
data class CollisionMatch(
    val personName: String,
    val roleTitle: String, // "Student (Final Year)" or "Faculty"
    val matchReason: String, // e.g., "Also worked on YOLO and Edge AI"
    val score: Int // 0-100%
)
