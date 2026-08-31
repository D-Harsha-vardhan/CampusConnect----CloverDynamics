package com.example.collisionengine.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {
    // Paste your Project URL here
    private const val SUPABASE_URL = "https://otdxrxyojmlbllpbbwfi.supabase.co"
    
    // Paste your Anon Key here
    private const val SUPABASE_KEY = "sb_publishable_nmRPk6YXjBRhYuViLb1J7Q_OR5q20sO"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }
}
