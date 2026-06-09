package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Room Entities ---

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val email: String = "",
    val isLoginVerified: Boolean = false,
    val hasTwoFactorEnabled: Boolean = false,
    val university: String = "Kangwon National University",
    val arrivalDate: String = "2026-08-25",
    val nationality: String = "",
    val languagePreference: String = "English",
    val address: String = "1 Gangwondaehak-gil, Chuncheon-si, Gangwon-do",
    val kycStatus: String = "PENDING", // PENDING, VERIFIED, REJECTED
    val passportImageUri: String? = null,
    val arcImageUri: String? = null,
    val studentIdImageUri: String? = null,
    val visaImageUri: String? = null,
    val referralCode: String = "UNIPACK_STUDENT",
    val referralCount: Int = 0,
    val unlockedDiscountPercent: Int = 0
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: String,
    val name: String,
    val category: String, // "Starter", "Standard", "Premium", "Food", "Service", "Halal"
    val price: Double,
    val quantity: Int,
    val imgUrl: String,
    val weight: Double, // in kg
    val isSavedForLater: Boolean = false
)

@Entity(tableName = "custom_kits")
data class CustomKitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val kitName: String,
    val itemsJsonContent: String, // String representation of list of products/quantities
    val totalPrice: Double,
    val totalWeight: Double,
    val isShared: Boolean = false,
    val isCustom: Boolean = true
)

@Entity(tableName = "placed_orders")
data class PlacedOrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderReferenceCode: String,
    val university: String,
    val itemNamesSummary: String,
    val totalKrwPrice: Double,
    val status: String, // "PRE-STAGED", "SHIPPED", "DELIVERED"
    val timestamp: String
)

// --- Room DAOs ---

@Dao
interface ProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getProfileDirect(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItemsFlow(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    @Query("SELECT * FROM cart_items WHERE productId = :prodId LIMIT 1")
    suspend fun getCartItemByProductId(prodId: String): CartItemEntity?
}

@Dao
interface CustomKitDao {
    @Query("SELECT * FROM custom_kits ORDER BY id DESC")
    fun getAllCustomKitsFlow(): Flow<List<CustomKitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomKit(kit: CustomKitEntity)

    @Delete
    suspend fun deleteCustomKit(kit: CustomKitEntity)
}

@Dao
interface PlacedOrderDao {
    @Query("SELECT * FROM placed_orders ORDER BY id DESC")
    fun getAllOrdersFlow(): Flow<List<PlacedOrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: PlacedOrderEntity)

    @Query("DELETE FROM placed_orders")
    suspend fun clearOrders()
}

// --- App Database Definition ---

@Database(
    entities = [UserProfileEntity::class, CartItemEntity::class, CustomKitEntity::class, PlacedOrderEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun cartDao(): CartDao
    abstract fun customKitDao(): CustomKitDao
    abstract fun placedOrderDao(): PlacedOrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unipacked_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
