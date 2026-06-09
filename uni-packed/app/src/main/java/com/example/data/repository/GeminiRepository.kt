package com.example.data.repository

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class Part(val text: String)

@JsonClass(generateAdapter = true)
data class Content(val parts: List<Part>, val role: String = "user")

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null
)

@JsonClass(generateAdapter = true)
data class PartResponse(val text: String?)

@JsonClass(generateAdapter = true)
data class ContentResponse(val parts: List<PartResponse>?)

@JsonClass(generateAdapter = true)
data class Candidate(val content: ContentResponse?)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(val candidates: List<Candidate>?)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}

class GeminiRepository {
    private val systemInstruction = Content(
        parts = listOf(
            Part(
                """
                You are "Uni", the custom AI Assistant for the "Uni Packed" app, built for international students arriving in South Korea (especially Gangwon Province).
                Your tone is warm, friendly, reassuring, and highly expert.
                You help with:
                1. Navigating airport-to-campus transit (bus, train, taxi). 
                2. Dormitory onboarding procedures, physical tuberculosis health certificates.
                3. Visa & immigration steps, such as securing the indispensable Alien Registration Card (ARC) in Chuncheon/Gangneung.
                4. Shopping smart with custom Starter/Standard/Premium settling kits.
                5. Locating authentic Halal food options (Indomie, Samosas, local Indian/Halal restaurants).
                6. Conversational language translations (English to handy Korean dorm survival phrases!).
                Always keep your explanations scannable, using clear bullet points. Present real-world advice to foster a positive, confident transition.
                """.trimIndent()
            )
        )
    )

    suspend fun generateUniResponse(userPrompt: String, history: List<Content> = emptyList()): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w("GeminiRepository", "API Key is empty/placeholder. Activating natural local intelligence fallback.")
            return getLocalAiFallbackResponse(userPrompt)
        }

        // Gather conversational history and append user prompt
        val fullContents = history + Content(parts = listOf(Part(userPrompt)), role = "user")
        val request = GenerateContentRequest(
            contents = fullContents,
            systemInstruction = systemInstruction
        )

        return try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "Uni is thinking deeply, but couldn't retrieve a text response. Please try again!"
        } catch (e: Exception) {
            Log.e("GeminiRepository", "Error calling Gemini API: ${e.message}", e)
            getLocalAiFallbackResponse(userPrompt)
        }
    }

    private fun getLocalAiFallbackResponse(prompt: String): String {
        val query = prompt.lowercase()
        return when {
            query.contains("hi") || query.contains("hello") || query.contains("hey") || query.contains("who are you") -> {
                "Hello! 👋 I'm **Uni**, your welcoming onboarding assistant at Uni Packed! " +
                        "How can I help you pack, settle, or navigate your journey to South Korea today? Ask me about: \n\n" +
                        "• 🚌 **Transit** (Incheon Airport to Chuncheon or Gangwon)\n" +
                        "• 🛏️ **Dorm check-ins** and Tuberculosis certificate rules\n" +
                        "• 📄 **ARC & Visas** setup\n" +
                        "• 🍲 **Halal grocery suggestions** or custom settling kits!"
            }
            query.contains("transit") || query.contains("airport") || query.contains("transport") || query.contains("bus") || query.contains("taxi") -> {
                "🚍 **Airport Terminal to Gangwon Transit Guide**\n\n" +
                        "Arriving at Incheon Airport (T1 or T2) and heading to Gangwon Province is simple and reliable:\n\n" +
                        "1. **By Airport Bus (Recommended for heavy luggage)**:\n" +
                        "   • **To Chuncheon (KNU / Hallym)**: Head to Terminal 1, Gate 13 (Platform 13B) or Terminal 2, B1 Traffic Center (Platform 10). Look for Bus **#8113** (Destination: Chuncheon Terminal). Cost is ~17,000 KRW. It takes roughly 2.5 hours.\n" +
                        "   • **To Gangneung**: Look for Airport Limousine terminal gates to Gangneung Intercity Terminal. Takes ~3.5 hours.\n\n" +
                        "2. **By Subway/Train (AREX + ITX Cheongchun)**:\n" +
                        "   • Take the AREX commuter subway from Airport to **Yongsan Station** or **Cheongnyangni Station**.\n" +
                        "   • Purchase an **ITX-Cheongchun high-speed rail ticket** straight to **Chuncheon Station**. From Chuncheon Station, take a short Kakao T taxi to KNU (~5,000 KRW) or Hallym (~4,000 KRW).\n\n" +
                        "💡 *Pro-tip: Download Naver Map or KakaoMap immediately. Google Maps does not give accurate walking/bus routes in South Korea due to domestic security storage regulations!*"
            }
            query.contains("dorm") || query.contains("bedding") || query.contains("housing") || query.contains("check-in") -> {
                "🏢 **Dormitory Settle-In & Health Checklist**\n\n" +
                        "Most universities in South Korea have strict protocols. Here is how to cross them without a hitch:\n\n" +
                        "1. **Tuberculosis Clean Bill (CRITICAL)**:\n" +
                        "   • You **must** present a written medical result (often translated to English or Korean) stating you are free of Tuberculosis before you can collect your key. Many dorms reject entry outright if you arrive at night without it!\n\n" +
                        "2. **Bedding & Pillow Necessities**:\n" +
                        "   • South Korean dorm rooms do **not** supply pillows, blankets, or mattress covers. They only supply a bare mattress frame. Our **Essential Starter Kit** or **Standard Kit** supplies customized duvet sizes specifically fitting Gangwon dorm mattresses, plus plugs adapters!\n\n" +
                        "3. **Curfew Regulations**:\n" +
                        "   • Most dorms close their main entrance gates from **23:00 to 05:00**, with automatic biometrics scanner checks. Watch your entrance rating to avoid housing penalties."
            }
            query.contains("arc") || query.contains("alien") || query.contains("visa") || query.contains("registration") -> {
                "📄 **Getting Your Alien Registration Card (ARC)**\n\n" +
                        "Any international student staying in South Korea for more than 90 days **must** apply for an ARC at the nearest Immigration Office. Here is the blueprint:\n\n" +
                        "• **Where to apply**: The **Chuncheon Immigration Office** (for Chuncheon KNU/Hallym) or the Gangneung subdivision.\n" +
                        "• **Key documents you need**:\n" +
                        "  1. Completed application form + passport-standard, white-background photo.\n" +
                        "  2. Original Passport + copy.\n" +
                        "  3. Certificate of Admission (CoA) + Certificate of Enrollment.\n" +
                        "  4. Proof of Dwelling (dormitory receipt / housing contract).\n" +
                        "  5. 30,000 KRW processing fee.\n\n" +
                        "💡 *Uni Packed Service Highlight*: We offer an **ARC Fast Assistance Package** in our Services tab. We help pre-fill the complex Korean forms, schedule your priority slot, and doublecheck addresses so you skip the 4-week waiting queue!"
            }
            query.contains("halal") || query.contains("muslim") || query.contains("food") || query.contains("snack") || query.contains("restaurant") -> {
                "🕌 **Halal Food & Grocery Guide for Gangwon Students**\n\n" +
                        "Eating Halal in Gangwon Province is much easier than it used to be, thanks to rising student networks! Here is what's near you:\n\n" +
                        "1. **Uni Packed Halal Marketplace (Our App!)**:\n" +
                        "   • Check our **Foods screen**! We stock certified Halal Indomie pack sets, frozen Samosas, Masala spices, cow Ghee, and Pran mango drinks. Ordered from central inventory to ship straight to your dorm room doorstep.\n\n" +
                        "2. **Halal spots in Chuncheon (Near KNU/Hallym)**:\n" +
                        "   • *Chuncheon Asian Mart*: Stocked with basmati rice, lentils, Halal mutton & chicken, situated in Hyoja-dong.\n" +
                        "   • *Jailan Rest*: Serves delicious kebab platters, mutton curry and flatbreads near KNU back gate.\n\n" +
                        "3. **Survival Tip**:\n" +
                        "   • Scan foods with Google Translate camera to watch for non-halal items such as pork (돼지고기 - *dwaejigogi*) or lard (돈지 - *donji*)."
            }
            query.contains("translate") || query.contains("korean") || query.contains("phrase") || query.contains("speak") -> {
                "🗣️ **Survival Dormitory Korean Phrases**\n\n" +
                        "Here are some indispensable phrases you will use continuously:\n\n" +
                        "• **Greetings**: \"An-nyeong-ha-se-yo\" (안녕하세요) — Hello!\n" +
                        "• **Gratitude**: \"Gam-sa-ham-ni-da\" (감사합니다) — Thank you!\n" +
                        "• **Asking direction**: \"___ eo-di-yeo-yo?\" (___ 어디여요?) — Where is the ___?\n" +
                        "  *(e.g., Kisa-gwan odiyeoyo? = Where is the dormitory?)*\n" +
                        "• **Help**: \"Do-wa-ju-se-yo!\" (도와주세요!) — Please help me!\n" +
                        "• **No Pork**: \"Dwae-ji-go-gi ppae-ju-se-yo\" (돼지고기 빼주세요) — Please remove pork from this meal.\n" +
                        "• **Dorm check-in**: \"Gi-suk-sa ip-sa ha-reo wass-eo-yo\" (기숙사 입사 하러 왔어요) — I am here to check-in to the dormitory."
            }
            query.contains("referral") || query.contains("discount") || query.contains("code") || query.contains("coupon") || query.contains("save") -> {
                "🎟️ **Referral & Discounts Program**\n\n" +
                        "Pack smart and save together! In the **Profile Screen**, you will find your custom referral code (e.g. `UNIPACK_STUDENT`).\n\n" +
                        "• **Share with Friends**: Share this code with incoming classmates.\n" +
                        "• **Double Rewards**: When they enter your code in their **Cart/Checkout** section, they instantly save **10% off** their first transaction!\n" +
                        "• **Earn Credits**: Once they make their first purchase, you unlock a **10% coupon** for your next Halal food or service order!"
            }
            else -> {
                "I hear you! South Korea is incredibly exciting but can feel a bit overwhelming at first. " +
                        "Regarding your request: I highly recommend checking out our specialized **Kits section** for essentials, " +
                        "browsing country-specific foods for a home-cooked meal, or letting me handle your airport pickup and SIM setup " +
                        "in our **Services** listing. \n\n" +
                        "Could you specify what university you are arriving at?"
            }
        }
    }
}
