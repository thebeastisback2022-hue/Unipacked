package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.GeminiRepository
import com.example.data.repository.Content
import com.example.data.repository.Part
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed interface AuthState {
    object LoggedOut : AuthState
    object SignUpForm : AuthState
    data class OtpVerification(val email: String, val otpCode: String, val isSignUp: Boolean) : AuthState
    data class TwoFactorChallenge(val email: String, val code: String) : AuthState
    object LoggedIn : AuthState
}

class MainViewModel(
    private val profileDao: ProfileDao,
    private val cartDao: CartDao,
    private val customKitDao: CustomKitDao,
    private val placedOrderDao: PlacedOrderDao
) : ViewModel() {

    // --- Core Repository & API layer ---
    private val geminiRepo = GeminiRepository()

    // --- Authentication & Session States ---
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedIn) // Default logged in for smooth UX, can toggle
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUserEmail = MutableStateFlow("student.unipacked@gmail.com")
    val currentUserEmail: StateFlow<String> = _currentUserEmail.asStateFlow()

    val loginHistory = mutableStateListOf(
        MapEntry("Jun 08, 2026 - 09:24", "Incheon Terminal 1 App Launch (Mobile App)"),
        MapEntry("Jun 07, 2026 - 18:12", "Chuncheon Campus Dormitory WiFi (Android Emulator)")
    )

    // --- Profile & KYC States (Reactive to Room) ---
    val userProfile: StateFlow<UserProfileEntity> = profileDao.getProfileFlow()
        .map { it ?: UserProfileEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfileEntity()
        )

    // --- Cart System (Reactive to Room) ---
    val cartItems: StateFlow<List<CartItemEntity>> = cartDao.getAllCartItemsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Derived cart properties using StateFlow transformations
    val cartCount: StateFlow<Int> = cartItems.map { list ->
        list.filter { !it.isSavedForLater }.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val cartTotalWeightKg: StateFlow<Double> = cartItems.map { list ->
        list.filter { !it.isSavedForLater }.sumOf { it.weight * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val rawSubtotalPrice: StateFlow<Double> = cartItems.map { list ->
        list.filter { !it.isSavedForLater }.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    // --- Customs & Coupons / Referrals ---
    private val _appliedPromoCode = MutableStateFlow("")
    val appliedPromoCode: StateFlow<String> = _appliedPromoCode.asStateFlow()

    private val _discountPercentUnlocked = MutableStateFlow(0)
    val discountPercentUnlocked: StateFlow<Int> = _discountPercentUnlocked.asStateFlow()

    // Final calculations
    val deliveryFee: StateFlow<Double> = rawSubtotalPrice.map { subtotal ->
        if (subtotal == 0.0) 0.0 else if (subtotal > 100000.0) 0.0 else 20000.0 // Free delivery over 100,000 ₩, else 20,000 ₩
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val promoDiscountAmount: StateFlow<Double> = combine(rawSubtotalPrice, discountPercentUnlocked) { subtotal, pct ->
        (subtotal * pct) / 100.0
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val finalTotalPrice: StateFlow<Double> = combine(rawSubtotalPrice, deliveryFee, promoDiscountAmount) { sub, del, disc ->
        (sub + del - disc).coerceAtLeast(0.0)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    // --- Saved Custom Kits (Reactive to Room) ---
    val customKits: StateFlow<List<CustomKitEntity>> = customKitDao.getAllCustomKitsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Placed Orders (Reactive to Room) ---
    val placedOrders: StateFlow<List<PlacedOrderEntity>> = placedOrderDao.getAllOrdersFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Initial builder items
    private val _customBuilderProducts = MutableStateFlow<Map<String, Int>>(emptyMap()) // productId -> quantity
    val customBuilderProducts: StateFlow<Map<String, Int>> = _customBuilderProducts.asStateFlow()

    // --- Reviews & Moderation System ---
    val reviewsList = mutableStateListOf<UserFeedback>().apply {
        addAll(Catalog.defaultReviews)
    }

    private val _isReviewModerating = MutableStateFlow(false)
    val isReviewModerating: StateFlow<Boolean> = _isReviewModerating.asStateFlow()

    // --- AI Chat Service (Uni Chatbot Logs) ---
    val chatMessages = mutableStateListOf(
        ChatMessage("system", "Hello! 😊 I'm **Uni**, your guide in South Korea. Ask me anything about visas, transit, packing, dorm onboarding, or locating halal groceries!", "09:20")
    )
    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // --- Navigation Selection Helper ---
    private val _selectedUnivId = MutableStateFlow("knu")
    val selectedUnivId: StateFlow<String> = _selectedUnivId.asStateFlow()

    val currentUnivInfo: StateFlow<University> = _selectedUnivId.map { id ->
        Catalog.universitiesList.find { it.id == id } ?: Catalog.universitiesList.first()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Catalog.universitiesList.first())

    // Save profile initializer on first launch if empty
    init {
        viewModelScope.launch {
            val current = profileDao.getProfileDirect()
            if (current == null) {
                profileDao.insertOrUpdateProfile(UserProfileEntity())
            }
        }
    }

    // --- Auth Event Procedures ---
    fun loginWithEmail(email: String) {
        viewModelScope.launch {
            _currentUserEmail.value = email
            // Seed a simulated OTP challenge code (4 digits)
            _authState.value = AuthState.OtpVerification(email, "5026", isSignUp = false)
        }
    }

    fun signUpWithEmail(email: String) {
        viewModelScope.launch {
            _currentUserEmail.value = email
            _authState.value = AuthState.OtpVerification(email, "1234", isSignUp = true)
        }
    }

    fun verifyOtp(enteredCode: String) {
        viewModelScope.launch {
            val state = _authState.value
            if (state is AuthState.OtpVerification) {
                if (enteredCode == state.otpCode) {
                    // Update user profile record as login verified!
                    val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
                    profileDao.insertOrUpdateProfile(prof.copy(isLoginVerified = true, email = state.email))

                    // If profile 2FA toggled on, redirect to 2FA challenge code
                    if (prof.hasTwoFactorEnabled) {
                        _authState.value = AuthState.TwoFactorChallenge(state.email, "7788")
                    } else {
                        loginHistory.add(0, MapEntry("Just now", "Successful Verification from Android emulator"))
                        _authState.value = AuthState.LoggedIn
                    }
                }
            }
        }
    }

    fun verifyTwoFactor(code: String) {
        val state = _authState.value
        if (state is AuthState.TwoFactorChallenge && code == state.code) {
            loginHistory.add(0, MapEntry("Just now", "2-Factor Verification Approved"))
            _authState.value = AuthState.LoggedIn
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.LoggedOut
            val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
            profileDao.insertOrUpdateProfile(prof.copy(isLoginVerified = false))
        }
    }

    fun toggleTwoFactor(enabled: Boolean) {
        viewModelScope.launch {
            val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
            profileDao.insertOrUpdateProfile(prof.copy(hasTwoFactorEnabled = enabled))
        }
    }

    // --- Profile & KYC Updates ---
    fun updateProfile(
        name: String,
        university: String,
        nationality: String,
        languagePreference: String,
        address: String,
        arrivalDate: String
    ) {
        viewModelScope.launch {
            val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
            profileDao.insertOrUpdateProfile(
                prof.copy(
                    name = name,
                    university = university,
                    nationality = nationality,
                    languagePreference = languagePreference,
                    address = address,
                    arrivalDate = arrivalDate
                )
            )
        }
    }

    fun simulateDocUpload(type: String, uriString: String) {
        viewModelScope.launch {
            val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
            val updated = when (type) {
                "Passport" -> prof.copy(passportImageUri = uriString, kycStatus = "PENDING")
                "ARC" -> prof.copy(arcImageUri = uriString, kycStatus = "PENDING")
                "StudentID" -> prof.copy(studentIdImageUri = uriString, kycStatus = "PENDING")
                "Visa" -> prof.copy(visaImageUri = uriString, kycStatus = "PENDING")
                else -> prof
            }
            profileDao.insertOrUpdateProfile(updated)
        }
    }

    fun simulateAdminKycApprove() {
        viewModelScope.launch {
            val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
            profileDao.insertOrUpdateProfile(prof.copy(kycStatus = "VERIFIED"))
        }
    }

    // --- Cart Actions ---
    fun addToCart(product: Product, quantitySelected: Int = 1) {
        viewModelScope.launch {
            val existing = cartDao.getCartItemByProductId(product.id)
            if (existing != null) {
                cartDao.insertCartItem(existing.copy(quantity = existing.quantity + quantitySelected))
            } else {
                cartDao.insertCartItem(
                    CartItemEntity(
                        productId = product.id,
                        name = product.name,
                        category = product.category,
                        price = product.krwPrice.toDouble(),
                        quantity = quantitySelected,
                        imgUrl = product.imgUrl,
                        weight = product.weightKg,
                        isSavedForLater = false
                    )
                )
            }
        }
    }

    fun changeCartQuantity(item: CartItemEntity, delta: Int) {
        viewModelScope.launch {
            val newQty = item.quantity + delta
            if (newQty <= 0) {
                cartDao.deleteCartItem(item)
            } else {
                cartDao.insertCartItem(item.copy(quantity = newQty))
            }
        }
    }

    fun saveForLater(item: CartItemEntity, isSaved: Boolean) {
        viewModelScope.launch {
            cartDao.insertCartItem(item.copy(isSavedForLater = isSaved))
        }
    }

    fun removeFromCart(item: CartItemEntity) {
        viewModelScope.launch {
            cartDao.deleteCartItem(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartDao.clearCart()
        }
    }

    fun checkoutAndRecordOrder(referenceCode: String, university: String, totalKrwPrice: Double) {
        viewModelScope.launch {
            val items = cartItems.value.filter { !it.isSavedForLater }
            if (items.isEmpty()) return@launch
            val summary = items.joinToString(", ") { "${it.name} (Qty: ${it.quantity})" }
            val order = PlacedOrderEntity(
                orderReferenceCode = referenceCode,
                university = university,
                itemNamesSummary = summary,
                totalKrwPrice = totalKrwPrice,
                status = "PRE-STAGED",
                timestamp = "2026-06-08"
            )
            placedOrderDao.insertOrder(order)
            cartDao.clearCart()
        }
    }

    // --- Referral & Promo Coupon Features ---
    fun applyCoupon(code: String): String {
        val uppercaseCode = code.trim().uppercase()
        _appliedPromoCode.value = uppercaseCode

        return when {
            uppercaseCode == "UNIPACK_WELCOME" -> {
                _discountPercentUnlocked.value = 15
                "Welcome Code Applied! 15% discount subtracted!"
            }
            uppercaseCode.startsWith("UNIPACK_") -> {
                _discountPercentUnlocked.value = 10
                // Actively reward the referral locally!
                viewModelScope.launch {
                    val prof = profileDao.getProfileDirect() ?: UserProfileEntity()
                    profileDao.insertOrUpdateProfile(
                        prof.copy(
                            referralCount = prof.referralCount + 1,
                            unlockedDiscountPercent = 10
                        )
                    )
                }
                "Referral Code Applied successfully! 10% discount subtracted!"
            }
            uppercaseCode == "GANGWON_FREE" -> {
                _discountPercentUnlocked.value = 5
                "Gangwon Student Special! 5% discount applied."
            }
            else -> {
                _discountPercentUnlocked.value = 0
                "Invalid promo code entered. Please try another code!"
            }
        }
    }

    // --- Custom Kit Builder Actions ---
    fun addItemToCustomBuilder(prodId: String) {
        val current = _customBuilderProducts.value.toMutableMap()
        val count = current[prodId] ?: 0
        current[prodId] = count + 1
        _customBuilderProducts.value = current
    }

    fun subtractItemFromCustomBuilder(prodId: String) {
        val current = _customBuilderProducts.value.toMutableMap()
        val count = current[prodId] ?: 0
        if (count > 1) {
            current[prodId] = count - 1
        } else {
            current.remove(prodId)
        }
        _customBuilderProducts.value = current
    }

    fun removeItemFromCustomBuilder(prodId: String) {
        val current = _customBuilderProducts.value.toMutableMap()
        current.remove(prodId)
        _customBuilderProducts.value = current
    }

    fun getBuilderTotalWeight(): Double {
        return _customBuilderProducts.value.entries.sumOf { (prodId, qty) ->
            val p = Catalog.productsList.find { it.id == prodId } ?: return@sumOf 0.0
            p.weightKg * qty
        }
    }

    fun getBuilderTotalPrice(): Double {
        return _customBuilderProducts.value.entries.sumOf { (prodId, qty) ->
            val p = Catalog.productsList.find { it.id == prodId } ?: return@sumOf 0.0
            p.krwPrice.toDouble() * qty
        }
    }

    fun saveCustomKit(customName: String) {
        viewModelScope.launch {
            if (_customBuilderProducts.value.isEmpty()) return@launch
            val itemsFormatted = _customBuilderProducts.value.entries.joinToString(", ") { (id, qty) ->
                val p = Catalog.productsList.find { it.id == id }?.name ?: id
                "$p (Qty: $qty)"
            }
            customKitDao.insertCustomKit(
                CustomKitEntity(
                    kitName = customName.ifBlank { "My Custom Kit" },
                    itemsJsonContent = itemsFormatted,
                    totalPrice = getBuilderTotalPrice(),
                    totalWeight = getBuilderTotalWeight()
                )
            )
            // Empty custom builder values on success
            _customBuilderProducts.value = emptyMap()
        }
    }

    fun deleteCustomKit(kit: CustomKitEntity) {
        viewModelScope.launch {
            customKitDao.deleteCustomKit(kit)
        }
    }

    // Add saved custom kit directly to cart!
    fun addCustomKitToCart(kit: CustomKitEntity) {
        viewModelScope.launch {
            cartDao.insertCartItem(
                CartItemEntity(
                    productId = "custom_kit_${kit.id}",
                    name = kit.kitName,
                    category = "Starter",
                    price = kit.totalPrice,
                    quantity = 1,
                    imgUrl = "",
                    weight = kit.totalWeight
                )
            )
        }
    }

    // --- Product Feedback / Reviews Writer & Moderation System ---
    fun submitUserReview(productId: String, rating: Int, reviewText: String, typeSelected: String): String {
        if (reviewText.trim().length < 5) {
            return "Review must be at least 5 characters long!"
        }
        val isVerified = true // Checked out by user locally

        // Quick simulation of toxic/spam keyword filter to emulate moderation
        val bannedWords = listOf("scam", "cheat", "ugly", "fake")
        val isFlagged = bannedWords.any { reviewText.lowercase().contains(it) }

        if (isFlagged) {
            return "Review rejected by moderation. Contains flagged keywords: " +
                    bannedWords.filter { reviewText.lowercase().contains(it) }.joinToString()
        }

        reviewsList.add(
            0,
            UserFeedback(
                id = UUID.randomUUID().toString(),
                productId = productId,
                userName = userProfile.value.name.ifBlank { "International Student" } + " (Verified Buyer)",
                rating = rating,
                text = reviewText,
                reviewType = typeSelected,
                isVerifiedBuyer = isVerified
            )
        )
        return "Feedback uploaded successfully! Thank you for sharing."
    }

    // --- AI Uni Service Integration (Gemini Service + History Logs) ---
    fun sendChatMessageToUni(promptText: String) {
        if (promptText.isBlank()) return

        // Clear input box and add user text
        chatMessages.add(ChatMessage("user", promptText, "12:12"))
        _isAiThinking.value = true

        viewModelScope.launch {
            // Build direct memory conversational log
            val geminiHistory = chatMessages.drop(1).dropLast(1).map {
                Content(parts = listOf(Part(it.message)), role = if (it.sender == "user") "user" else "model")
            }

            val botResponse = geminiRepo.generateUniResponse(promptText, geminiHistory)
            chatMessages.add(ChatMessage("uni", botResponse, "12:13"))
            _isAiThinking.value = false
        }
    }

    fun selectUniversity(univId: String) {
        _selectedUnivId.value = univId
    }
}

// Helper models for VM
data class MapEntry(val key: String, val value: String)
data class ChatMessage(val sender: String, val message: String, val timestamp: String)
