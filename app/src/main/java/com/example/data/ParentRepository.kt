package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ParentRepository(
    private val dao: ParentDao,
    private val api: FirestoreApi
) {
    val allParents: Flow<List<ParentEntity>> = dao.getAllParentsFlow()

    suspend fun fetchFromCloud(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.getParents()
            val entities = response.documents?.map { it.toEntity() } ?: emptyList()
            dao.clearAll()
            dao.insertParents(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveParent(
        studentName: String,
        studentClass: String,
        parentName: String,
        job: String,
        support: String,
        phone: String,
        address: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Check if entry already exists locally in Room cache
            val all = dao.getAllParents()
            val exists = all.any { 
                it.studentName.equals(studentName, ignoreCase = true) && 
                it.studentClass.equals(studentClass, ignoreCase = true) 
            }
            if (exists) {
                return@withContext Result.failure(Exception("EXISTS"))
            }

            val timestamp = java.text.DateFormat.getDateTimeInstance().format(java.util.Date())
            val request = FirestoreDocumentRequest(
                fields = createFirestoreFields(
                    studentName = studentName,
                    studentClass = studentClass,
                    parentName = parentName,
                    job = job,
                    support = support,
                    phone = phone,
                    address = address,
                    timestamp = timestamp
                )
            )

            // POST to Firestore Cloud Map
            val response = api.createParent(request)
            val entity = response.toEntity()

            // Database insertion
            dao.insertParent(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteParent(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.deleteParent(id)
            dao.deleteParentById(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
