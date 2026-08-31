package com.example.collisionengine.data.models

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("student_id") val studentId: String,
    val name: String,
    val department: String,
    val year: String,
    val skills: String,
    val projects: String,
    @SerializedName("research_interests") val researchInterests: String,
    val certifications: String,
    @SerializedName("career_interests") val careerInterests: String
)

data class Project(
    @SerializedName("project_id") val projectId: String,
    @SerializedName("student_id") val studentId: String,
    val title: String,
    val description: String,
    val technologies: String,
    val domain: String,
    val methodology: String,
    val year: String
)

data class Research(
    @SerializedName("research_id") val researchId: String,
    @SerializedName("person_id") val personId: String,
    val title: String,
    val abstract: String,
    @SerializedName("research_area") val researchArea: String,
    val methodologies: String,
    val publication: String
)

data class Placement(
    @SerializedName("placement_id") val placementId: String,
    @SerializedName("student_id") val studentId: String,
    val company: String,
    val role: String,
    val skills: String,
    @SerializedName("interview_topics") val interviewTopics: String,
    val outcome: String,
    val year: String
)

data class Faculty(
    @SerializedName("faculty_id") val facultyId: String,
    val name: String,
    val department: String,
    @SerializedName("research_interests") val researchInterests: String,
    val publications: String,
    val expertise: String
)

// UI wrapper for matches
data class CollisionMatch(
    val personName: String,
    val roleTitle: String, // "Student (Final Year)" or "Faculty"
    val matchReason: String, // e.g., "Also worked on YOLO and Edge AI"
    val score: Int // 0-100%
)
