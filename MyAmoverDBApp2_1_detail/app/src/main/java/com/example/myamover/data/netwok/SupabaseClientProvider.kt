package com.example.myamover.data.netwok

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {

//    // ⚠ substitui pelos dados do projeto Supabase:
//   //    private const val SUPABASE_URL = "https://ivskhhextbjtlnyhibch.supabase.co"
//
//    // anon public key (Settings → API → anon public):
//    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYm" +
//            "FzZSIsInJlZiI6Iml2c2toaGV4dGJqdGxueWhpYmNoIiwicm9sZSI6Im" +
//            "Fub24iLCJpYXQiOjE3NTkzODYwODIsImV4cCI6MjA3NDk2MjA4Mn0.dkQoH2fNkQnUwlsXYhvVkZ9c97dP6Saq4nDBkD2QAog"

    val client =
        createSupabaseClient(
            supabaseUrl = "https://ivskhhextbjtlnyhibch.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Iml2c2toaGV4dGJqdGxueWhpYmNoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTkzODYwODIsImV4cCI6MjA3NDk2MjA4Mn0.dkQoH2fNkQnUwlsXYhvVkZ9c97dP6Saq4nDBkD2QAog"

        ) {
            //install(io.github.jan.supabase.auth.Auth)
            install(Auth)
            //install(io.github.jan.supabase.postgrest.Postgrest)
            install(Postgrest)
        }
    }


