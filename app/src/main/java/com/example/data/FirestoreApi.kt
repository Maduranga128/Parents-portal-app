package com.example.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface FirestoreApi {
    @GET("projects/vidyartha-parents-portal/databases/(default)/documents/parents")
    suspend fun getParents(
        @Query("pageSize") pageSize: Int = 300
    ): FirestoreDocumentsResponse

    @POST("projects/vidyartha-parents-portal/databases/(default)/documents/parents")
    suspend fun createParent(
        @Body request: FirestoreDocumentRequest
    ): FirestoreDocumentResponse

    @DELETE("projects/vidyartha-parents-portal/databases/(default)/documents/parents/{id}")
    suspend fun deleteParent(
        @Path("id") id: String
    ): Response<Unit>

    companion object {
        private const val BASE_URL = "https://firestore.googleapis.com/v1/"

        fun create(): FirestoreApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            return retrofit.create(FirestoreApi::class.java)
        }
    }
}
