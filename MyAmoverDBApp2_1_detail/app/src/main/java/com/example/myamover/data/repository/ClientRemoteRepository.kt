package com.example.myamover.data.repository

import com.example.myamover.data.netwok.SupabaseClientProvider
import com.example.myamover.data.remote.ClientRemote
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClientRemoteRepository {

    private val client = SupabaseClientProvider.client


    suspend fun getAllClient():List<ClientRemote> = withContext(Dispatchers.IO){
        client.postgrest["Client"]
            .select()
            .decodeList<ClientRemote>()
    }
}