package com.example.sharktracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import kotlinx.coroutines.delay

class SharkRepository {

    // Enhanced mock data with real OCEARCH shark names and realistic locations

    suspend fun getSharks(): Result<List<Shark>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SharkRepository", "Loading enhanced shark data...")
                delay(1000) // Simulate network delay

                val enhancedSharks = listOf(
                    Shark(
                        id = "mary_lee",
                        name = "Mary Lee",
                        species = "Great White Shark",
                        gender = "Female",
                        stage = "Adult",
                        length = "16 ft",
                        weight = "3,456 lbs",
                        tagLocation = "Cape Cod, MA",
                        tagDate = "2012-09-17",
                        description = "Famous mature female great white shark. Mary Lee was one of OCEARCH's most tracked sharks and helped scientists understand migration patterns.",
                        profilePhoto = "https://www.ocearch.org/images/sharks/mary-lee.jpg",
                        tracker = true
                    ),
                    Shark(
                        id = "nukumi",
                        name = "Nukumi",
                        species = "Great White Shark",
                        gender = "Female",
                        stage = "Adult",
                        length = "17.2 ft",
                        weight = "3,541 lbs",
                        tagLocation = "Nova Scotia, Canada",
                        tagDate = "2019-10-02",
                        description = "One of the largest great whites ever tagged by OCEARCH. Named after a legendary Mi'kmaq grandmother figure.",
                        profilePhoto = "https://www.ocearch.org/images/sharks/nukumi.jpg",
                        tracker = true
                    ),
                    Shark(
                        id = "unamaki",
                        name = "Unama'ki",
                        species = "Great White Shark",
                        gender = "Male",
                        stage = "Adult",
                        length = "15.3 ft",
                        weight = "2,076 lbs",
                        tagLocation = "Nova Scotia, Canada",
                        tagDate = "2019-10-05",
                        description = "Large adult male great white shark named after the Mi'kmaq word for Cape Breton.",
                        profilePhoto = "https://www.ocearch.org/images/sharks/unamaki.jpg",
                        tracker = true
                    ),
                    Shark(
                        id = "ironbound",
                        name = "Ironbound",
                        species = "Great White Shark",
                        gender = "Male",
                        stage = "Adult",
                        length = "12.1 ft",
                        weight = "998 lbs",
                        tagLocation = "Montauk, NY",
                        tagDate = "2019-10-03",
                        description = "Named after West Ironbound Island near Lunenburg, Nova Scotia. Known for frequent coastal appearances.",
                        profilePhoto = "https://www.ocearch.org/images/sharks/ironbound.jpg",
                        tracker = true
                    ),
                    Shark(
                        id = "savannah",
                        name = "Savannah",
                        species = "Great White Shark",
                        gender = "Female",
                        stage = "Sub-Adult",
                        length = "14.3 ft",
                        weight = "1,668 lbs",
                        tagLocation = "Savannah, GA",
                        tagDate = "2020-12-05",
                        description = "Young female great white showing typical coastal migration patterns along the Eastern Seaboard.",
                        profilePhoto = "https://www.ocearch.org/images/sharks/savannah.jpg",
                        tracker = true
                    )
                )

                Log.d("SharkRepository", "Successfully loaded ${enhancedSharks.size} enhanced sharks")
                Result.success(enhancedSharks)
            } catch (e: Exception) {
                Log.e("SharkRepository", "Error loading enhanced sharks: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun getSharkPings(): Result<List<SharkPing>> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("SharkRepository", "Loading enhanced shark ping data...")
                delay(1500) // Simulate network delay

                // Generate realistic recent pings along known migration routes
                val enhancedPings = listOf(
                    // Mary Lee - Off Cape Cod
                    SharkPing(
                        id = "ping_mary_1",
                        sharkId = "mary_lee",
                        datetime = "2024-07-16 08:15:00",
                        latitude = 41.6688,
                        longitude = -70.2962,
                        depth = "18 m",
                        temperature = "16°C",
                        name = "Mary Lee",
                        species = "Great White Shark",
                        profilePhoto = "https://www.ocearch.org/images/sharks/mary-lee.jpg"
                    ),
                    // Nukumi - Off Nova Scotia
                    SharkPing(
                        id = "ping_nukumi_1",
                        sharkId = "nukumi",
                        datetime = "2024-07-16 09:42:00",
                        latitude = 44.2619,
                        longitude = -63.7443,
                        depth = "25 m",
                        temperature = "14°C",
                        name = "Nukumi",
                        species = "Great White Shark",
                        profilePhoto = "https://www.ocearch.org/images/sharks/nukumi.jpg"
                    ),
                    // Unama'ki - Moving south
                    SharkPing(
                        id = "ping_unamaki_1",
                        sharkId = "unamaki",
                        datetime = "2024-07-16 11:20:00",
                        latitude = 42.3584,
                        longitude = -71.0598,
                        depth = "12 m",
                        temperature = "18°C",
                        name = "Unama'ki",
                        species = "Great White Shark",
                        profilePhoto = "https://www.ocearch.org/images/sharks/unamaki.jpg"
                    ),
                    // Ironbound - Near Long Island
                    SharkPing(
                        id = "ping_ironbound_1",
                        sharkId = "ironbound",
                        datetime = "2024-07-16 13:05:00",
                        latitude = 40.7589,
                        longitude = -73.9851,
                        depth = "8 m",
                        temperature = "20°C",
                        name = "Ironbound",
                        species = "Great White Shark",
                        profilePhoto = "https://www.ocearch.org/images/sharks/ironbound.jpg"
                    ),
                    // Savannah - Off Georgia coast
                    SharkPing(
                        id = "ping_savannah_1",
                        sharkId = "savannah",
                        datetime = "2024-07-16 14:30:00",
                        latitude = 32.0835,
                        longitude = -80.9007,
                        depth = "15 m",
                        temperature = "22°C",
                        name = "Savannah",
                        species = "Great White Shark",
                        profilePhoto = "https://www.ocearch.org/images/sharks/savannah.jpg"
                    )
                )

                Log.d("SharkRepository", "Successfully loaded ${enhancedPings.size} enhanced pings")
                Result.success(enhancedPings)
            } catch (e: Exception) {
                Log.e("SharkRepository", "Error loading enhanced pings: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun getSharkPings(sharkId: String): Result<List<SharkPing>> {
        return withContext(Dispatchers.IO) {
            try {
                val allPings = getSharkPings().getOrNull() ?: emptyList()
                val filteredPings = allPings.filter { it.sharkId == sharkId }
                Result.success(filteredPings)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}