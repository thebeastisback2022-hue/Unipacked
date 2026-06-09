package com.example.data.model

import java.io.Serializable

// --- Catalog Product Model ---
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double, // in USD
    val krwPrice: Int, // in KRW
    val category: String, // "Starter", "Standard", "Premium", "University", "Country", "Halal-Food", "Non-Halal-Food", "Service"
    val subCategory: String = "", // e.g., "Halal snacks", "Halal frozen", "SIM", "Nepal", "India"
    val weightKg: Double = 0.5,
    val imgUrl: String = "",
    val rating: Float = 4.5f,
    val reviewsCount: Int = 12,
    val ingredients: String = "Natural ingredients",
    val allergies: List<String> = emptyList(),
    val isHalalCertified: Boolean = false,
    val originCountry: String = "South Korea",
    val estimatedDeliveryDays: Int = 2,
    val nutritionInfo: String = "High protein, calcium",
    val expiryDays: Int = 180,
    val bundleProducts: List<String> = emptyList() // If kit, lists contained items
) : Serializable

// --- University Guide Model ---
data class University(
    val id: String,
    val name: String,
    val location: String, // Chuncheon, Gangneung, Wonju
    val EnglishName: String,
    val dormitoryAddress: String,
    val emergencyContact: String,
    val globalCampusOffice: String,
    val mainDormName: String,
    val orientationDates: String,
    val guideBookPdfPreview: String, // Details description of campus process
    val usefulPointers: List<String>,
    val latLngString: Pair<Double, Double> // coordinates for maps representation
) : Serializable

// --- Service Item Model ---
data class StudentService(
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val category: String, // "SIM", "Move", "Onboarding", "Admin"
    val features: List<String>,
    val detailGuide: String,
    val estProcessingTime: String
) : Serializable

// --- Product Feedback/Review Model ---
data class UserFeedback(
    val id: String = "",
    val productId: String,
    val userName: String,
    val rating: Int,
    val text: String,
    val reviewType: String = "Product Experience", // "Product Experience", "Delivery Experience", "Suggestion", "Complaint"
    val isVerifiedBuyer: Boolean = true,
    val timestamp: String = "2026-06-08"
) : Serializable

// --- Catalog Provider (Database Seed) ---
object Catalog {
    val universitiesList = listOf(
        // --- GANGWON PROVINCE UNIVERSITIES ---
        University(
            id = "knu",
            name = "Kangwon National University (강원대학교)",
            location = "Chuncheon, Gangwon",
            EnglishName = "Kangwon National University",
            dormitoryAddress = "84 Heukjeok-gil, Toegye-dong, Chuncheon-si, Gangwon-do (Dormitory Yeol-ji-gwan)",
            emergencyContact = "+82-33-250-6981 (International Affairs)",
            globalCampusOffice = "KNU International Lounge, Main Library Fl 2",
            mainDormName = "Yeol-ji-gwan Dual-Rooming & Toegye dorms",
            orientationDates = "Aug 26 - Aug 28, 2026",
            guideBookPdfPreview = "To obtain alien registration cards (ARC), students must submit proof of residence to student office 108. Standard public transit from Incheon Airport is Bus #8113 direct to Chuncheon Intercity Terminal.",
            usefulPointers = listOf(
                "Submit Tuberculosis test results BEFORE arriving at the dormitory.",
                "Purchase T-Money transit card at Incheon T1 convenience stores.",
                "Dorm check-in requires a printed Passport and dormitory payment certificate."
            ),
            latLngString = Pair(37.8687, 127.7441)
        ),
        University(
            id = "hallym",
            name = "Hallym University (한림대학교)",
            location = "Chuncheon, Gangwon",
            EnglishName = "Hallym University",
            dormitoryAddress = "1 Hallymdaehak-gil, Chuncheon-si, Gangwon-do (International Dorm B)",
            emergencyContact = "+82-33-248-1341 (Global Support Team)",
            globalCampusOffice = "Hallym International Hall Office #1420",
            mainDormName = "Deok-Gyeong Hall (International dormitory)",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Hallym orientations run on campus B-Hall. Health insurance signup is mandatory on Day 1. Campus bank partner is Shinhan Bank, situated on the first floor of the Student Union building.",
            usefulPointers = listOf(
                "Opening hours for global centre: 09:00 - 17:00 (Break 12:00-13:00).",
                "Shinhan Bank offers direct foreign student bank account generation on campus."
            ),
            latLngString = Pair(37.8863, 127.7378)
        ),
        University(
            id = "gwnu",
            name = "Gangneung-Wonju National University (강릉원주대학교)",
            location = "Gangneung & Wonju, Gangwon",
            EnglishName = "Gangneung-Wonju National University",
            dormitoryAddress = "7 Jukheon-gil, Gangneung-si, Gangwon-do (Haesong-gwan Dormitory)",
            emergencyContact = "+82-33-640-2766 (Office of International Affairs)",
            globalCampusOffice = "Education & Cooperation Building #304",
            mainDormName = "Haesong Hall (강릉 해송관)",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "GWNU operates dual campuses in Gangneung (coastal) and Wonju. ARC registrations are gathered by the university team during orientation weeks. Tuberculosis clearance certificates must be translated into Korean.",
            usefulPointers = listOf(
                "Excellent shuttle between Gangneung campus and Gyeongpo Beach route.",
                "Direct bus lines from Chuncheon terminal to Gangneung terminal start at 07:00."
            ),
            latLngString = Pair(37.7699, 128.8722)
        ),
        University(
            id = "yonsei_wonju",
            name = "Yonsei University - Mirae Campus (연세대학교 미래캠퍼스)",
            location = "Wonju, Gangwon",
            EnglishName = "Yonsei University Mirae Campus",
            dormitoryAddress = "1 Yonseidae-gil, Wonju-si, Gangwon-do (Cheong-Yeon-Gwan)",
            emergencyContact = "+82-33-760-2114 (Mirae Global Service Centre)",
            globalCampusOffice = "Eagle Hall Wing C Fl 1",
            mainDormName = "Cheong-Yeon-Gwan (청연학사)",
            orientationDates = "Aug 25, 2026",
            guideBookPdfPreview = "Located in Wonju, Mirae campus provides specialized English services. Students are advised to apply for mobile banking on Woori bank platforms. Dorm checks are scheduled strictly between 23:00 - 05:00.",
            usefulPointers = listOf(
                "Woori Bank representative booth operates during orientation weeks.",
                "Maeji Lake surrounds the campus, providing gorgeous walking trails."
            ),
            latLngString = Pair(37.2801, 127.9004)
        ),
        University(
            id = "kwandong",
            name = "Catholic Kwandong University (가톨릭관동대학교)",
            location = "Gangneung, Gangwon",
            EnglishName = "Catholic Kwandong University",
            dormitoryAddress = "24 Beomil-ro 579beon-gil, Gangneung-si, Gangwon-do (Benjamin Hall)",
            emergencyContact = "+82-33-649-7080 (OIA Center)",
            globalCampusOffice = "Gabriel Science Hall Room #202",
            mainDormName = "Benjamin Gwan Dorm",
            orientationDates = "Aug 29, 2026",
            guideBookPdfPreview = "International students have designated staff support at Gabriel Hall. Korean health check guidelines must be fully verified upon arrival. Shuttle schedules are updated every semester on standard web bulletin boards.",
            usefulPointers = listOf(
                "Immigration office agent visits the campus twice monthly for visa issues.",
                "Requires submission of pre-entry COVID/Tuberculosis state proof."
            ),
            latLngString = Pair(37.7371, 128.8874)
        ),
        University(
            id = "sangji",
            name = "Sangji University (상지대학교)",
            location = "Wonju, Gangwon",
            EnglishName = "Sangji University",
            dormitoryAddress = "83 Sangjidae-gil, Wonju-si, Gangwon-do (Woosan-gwan Dormitory)",
            emergencyContact = "+82-33-730-0114 (International Education Center)",
            globalCampusOffice = "Main Administration Building Fl 3",
            mainDormName = "Woosan-gwan Dorms",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Provides strong language support in Chinese, Nepali and Vietnamese. ARC filing is organized by the university administrative office in bulk batches within the first month.",
            usefulPointers = listOf(
                "Student ID card activation also connects to a local Woori Bank account.",
                "Onboarding checklist includes submitting mandatory health certificates in Korean."
            ),
            latLngString = Pair(37.3615, 127.9305)
        ),
        University(
            id = "kyungdong",
            name = "Kyungdong University (경동대학교)",
            location = "Goseong & Wonju, Gangwon",
            EnglishName = "Kyungdong University",
            dormitoryAddress = "46 Bongpo-4-gil, Toseong-myeon, Goseong-gun, Gangwon-do",
            emergencyContact = "+82-33-639-0114 (Global Education Support Office)",
            globalCampusOffice = "Academic Hall Fl 1",
            mainDormName = "Bongpo-gwan Dormitory",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "Kyungdong operates campuses in both scenic coastal Goseong and Wonju on the mainland. International students enjoy localized buddies assisting in local shopping, SIM registration, and public transport onboarding.",
            usefulPointers = listOf(
                "Main bus lines directly link Goseong campus with Sokcho express terminal.",
                "Verify required vaccinations and health check proof prior to dorm keys release."
            ),
            latLngString = Pair(38.2589, 128.5635)
        ),

        // --- SEOUL UNIVERSITIES ---
        University(
            id = "snu",
            name = "Seoul National University (서울대학교)",
            location = "Gwanak, Seoul",
            EnglishName = "Seoul National University",
            dormitoryAddress = "1 Gwanak-ro, Gwanak-gu, Seoul (Gwanak-sa Dormitories)",
            emergencyContact = "+82-2-880-8634 (OIA Support Desk)",
            globalCampusOffice = "OIA Building #152",
            mainDormName = "Gwanak-sa Residence Halls",
            orientationDates = "Aug 24 - Aug 28, 2026",
            guideBookPdfPreview = "SNU Gwanak campus holds registration in bulk. Student ID card collection requires proof of enrollment and passport copies. NH Nonghyup on campus handles bulk bank accounts.",
            usefulPointers = listOf(
                "Collect student ID cards immediately from Gwanak-sa administration office.",
                "The SNU shuttle links the subway station directly to Gwanak-sa dorms.",
                "Mandatory tuberculosis screening must be uploaded to the SNU dorm portal online."
            ),
            latLngString = Pair(37.4598, 126.9519)
        ),
        University(
            id = "yonsei_seoul",
            name = "Yonsei University (연세대학교)",
            location = "Sinchon, Seoul",
            EnglishName = "Yonsei University",
            dormitoryAddress = "50 Yonsei-ro, Seodaemun-gu, Seoul (International House / SK Global House)",
            emergencyContact = "+82-2-2123-2114 (Global Service Center)",
            globalCampusOffice = "Baekyang Hall S-302",
            mainDormName = "SK Global House",
            orientationDates = "Aug 25, 2026",
            guideBookPdfPreview = "SK Global House has key card access systems. Exchange students check in under Global Center orientation cards. Woori Bank on campus is located in the basement of Commons Hall.",
            usefulPointers = listOf(
                "SK Global House features dedicated western food restaurants and laundry.",
                "ARC photo collection is scheduled during the first week at Baekyang Hall."
            ),
            latLngString = Pair(37.5658, 126.9386)
        ),
        University(
            id = "korea_seoul",
            name = "Korea University (고려대학교)",
            location = "Anam, Seoul",
            EnglishName = "Korea University",
            dormitoryAddress = "145 Anam-ro, Seongbuk-gu, Seoul (CJ International House)",
            emergencyContact = "+82-2-3290-5114 (KU Global Services Center)",
            globalCampusOffice = "Dongwon Hall #201",
            mainDormName = "CJ International House / Anam Dorms",
            orientationDates = "Aug 26, 2026",
            guideBookPdfPreview = "CJ International Residence offers high quality double/single rooms. For bank card integration, Hana Bank operates a massive student portal inside Hana Square.",
            usefulPointers = listOf(
                "Hana Square bank branch specializes in English, Chinese, and Vietnamese services.",
                "CJ House checking requires a printed Tuberculosis report scan."
            ),
            latLngString = Pair(37.5891, 127.0322)
        ),
        University(
            id = "hanyang_seoul",
            name = "Hanyang University (한양대학교)",
            location = "Seongdong, Seoul",
            EnglishName = "Hanyang University",
            dormitoryAddress = "222 Wangsimni-ro, Seongdong-gu, Seoul (Hanyang Residence Hall 5)",
            emergencyContact = "+82-2-2220-0045 (International Affairs Desk)",
            globalCampusOffice = "FTC Building Room #218",
            mainDormName = "Hanyang Residence Hall 5 & Technopack Dorms",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Hanyang orientation details target alien registration, NH safety insurance and subway rules. Students can configure checking accounts easily at Shinhan Bank in the IT Hall.",
            usefulPointers = listOf(
                "Hanyang's campus subways are connected directly to Line 2 Hanyang Univ Station.",
                "Laundry tokens must be purchased with Cash KRW in Dorm 5 lobbies."
            ),
            latLngString = Pair(37.5564, 127.0445)
        ),
        University(
            id = "skku_seoul",
            name = "Sungkyunkwan University (성균관대학교)",
            location = "Jongno, Seoul",
            EnglishName = "Sungkyunkwan University",
            dormitoryAddress = "25-2 Sungkyunkwan-ro, Jongno-gu, Seoul (Ko-Gwan Dormitory)",
            emergencyContact = "+82-2-760-0114 (SKKU Global Lounge Team)",
            globalCampusOffice = "International Hall Room #901",
            mainDormName = "Ko-Gwan International Dorms",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "SKKU Humanities & Social Sciences campus operates in central Jongno. The university provides an online ARC consultation desk which helps fill immigration portals easily.",
            usefulPointers = listOf(
                "Easy shuttle commutes from Hyehwa Subway Station direct to campus buildings.",
                "Woori Bank offers multi-currency digital cards at the Student Center branch."
            ),
            latLngString = Pair(37.5882, 126.9936)
        ),
        University(
            id = "kyunghee_seoul",
            name = "Kyung Hee University (경희대학교)",
            location = "Dongdaemun, Seoul",
            EnglishName = "Kyung Hee University",
            dormitoryAddress = "26 Kyungheedae-ro, Dongdaemun-gu, Seoul (Woo-Jung Hall)",
            emergencyContact = "+82-2-961-0114 (KHU Global Service Team)",
            globalCampusOffice = "Main Administration Building Fl 2",
            mainDormName = "Woo-Jung Residence Hall & Sameui-won",
            orientationDates = "Aug 29, 2026",
            guideBookPdfPreview = "KHU requires all international students to enroll in health insurance policies upon arrival. ARC bulk registrations are verified through the student center portal.",
            usefulPointers = listOf(
                "Hana Bank branch is directly inside the university medical center building.",
                "Woo-Jung Hall check-in requires physical passport verification check."
            ),
            latLngString = Pair(37.5962, 127.0526)
        ),

        // --- BUSAN UNIVERSITIES ---
        University(
            id = "pnu_busan",
            name = "Pusan National University (부산대학교)",
            location = "Geumjeong, Busan",
            EnglishName = "Pusan National University",
            dormitoryAddress = "2 Busandaehak-ro 63beon-gil, Geumjeong-gu, Busan (Woongbi-gwan Residence)",
            emergencyContact = "+82-51-510-1887 (PNU International Center)",
            globalCampusOffice = "Sangun Hall #211",
            mainDormName = "Woongbi-gwan Residence Hall",
            orientationDates = "Aug 25, 2026",
            guideBookPdfPreview = "PNU Woongbi-gwan holds onboarding on terminal floors. Bank account opening is done at the PNU Shinhan bank on the first floor of the Main Library building.",
            usefulPointers = listOf(
                "A Tuberculosis test sheet is mandatory to receive your electronic room card key.",
                "PNU shuttle buses connect Geumjeong subway stations straight to the Woongbi residence."
            ),
            latLngString = Pair(35.2335, 129.0792)
        ),
        University(
            id = "pknu_busan",
            name = "Pukyong National University (부경대학교)",
            location = "Nam, Busan",
            EnglishName = "Pukyong National University",
            dormitoryAddress = "45 Yongso-ro, Nam-gu, Busan (Sejong 1 Hall)",
            emergencyContact = "+82-51-629-6911 (International Support Office)",
            globalCampusOffice = "Mirae Hall Room #101",
            mainDormName = "Sejong Hall 1 & 2 Residences",
            orientationDates = "Aug 26, 2026",
            guideBookPdfPreview = "Sejong Hall is fully automated with smart-room temperature controls. Bank setups are partnered with Suhyup Bank located in the Dong-won Building.",
            usefulPointers = listOf(
                "Walking distance to the beautiful Gwangalli beach transit lines.",
                "Suhyup Bank issues local checks with direct transport card functionality activated."
            ),
            latLngString = Pair(35.1341, 129.1026)
        ),
        University(
            id = "donga_busan",
            name = "Dong-A University (동아대학교)",
            location = "Saha, Busan",
            EnglishName = "Dong-A University",
            dormitoryAddress = "37 Nakdong-daero 550beon-gil, Saha-gu, Busan (Hanlim Hall)",
            emergencyContact = "+82-51-200-6111 (Dong-A Support Desk)",
            globalCampusOffice = "Sundeok Hall Room #403",
            mainDormName = "Hanlim Hall Dormitories",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Hanlim Residence operates directly near Busan subway Line 1 (Hadan Station). Busan immigration center visits campus twice monthly for biometric finger tracking.",
            usefulPointers = listOf(
                "Submit immunization clearance reports translated into English or Korean.",
                "Hana Bank offers multi-currency exchange checks on landing week portals."
            ),
            latLngString = Pair(35.1166, 128.9682)
        ),

        // --- MORE SEOUL UNIVERSITIES ---
        University(
            id = "sogang_seoul",
            name = "Sogang University (서강대학교)",
            location = "Mapo, Seoul",
            EnglishName = "Sogang University",
            dormitoryAddress = "35 Baekbeom-ro, Mapo-gu, Seoul (Gonzaga Hall)",
            emergencyContact = "+82-2-705-8114 (Office of International Affairs)",
            globalCampusOffice = "Arrupe Hall Room #101",
            mainDormName = "Gonzaga Hall",
            orientationDates = "Aug 26, 2026",
            guideBookPdfPreview = "Gonzaga Residence Hall is highly modernized with electronic pass cards. Students can open accounts at Woori Bank on campus inside the Gonzaga Plaza.",
            usefulPointers = listOf(
                "Submit health screening and tuberculosis test results before check-in.",
                "Woori Bank opens special booths during registration weeks."
            ),
            latLngString = Pair(37.5509, 126.9410)
        ),
        University(
            id = "ewha_seoul",
            name = "Ewha Womans University (이화여자대학교)",
            location = "Seodaemun, Seoul",
            EnglishName = "Ewha Womans University",
            dormitoryAddress = "52 Ewhayeodae-gil, Seodaemun-gu, Seoul (E-House)",
            emergencyContact = "+82-2-3277-2114 (OIA Support)",
            globalCampusOffice = "ECC (Ewha Campus Complex) B329",
            mainDormName = "E-House Residence Hall",
            orientationDates = "Aug 25, 2026",
            guideBookPdfPreview = "E-House uses an eco-friendly architectural layout with single/double suite options. Campus administrative desks help process health checks and coordinate ARC biometrics.",
            usefulPointers = listOf(
                "ECC serves as the primary hub for banks, study halls, and food options.",
                "Tuberculosis certificates are strictly mandatory for room key collection."
            ),
            latLngString = Pair(37.5618, 126.9468)
        ),
        University(
            id = "cau_seoul",
            name = "Chung-Ang University (중앙대학교)",
            location = "Dongjak, Seoul",
            EnglishName = "Chung-Ang University",
            dormitoryAddress = "84 Heukseok-ro, Dongjak-gu, Seoul (CAU Blue Mir Hall)",
            emergencyContact = "+82-2-820-5114 (Office of International Affairs)",
            globalCampusOffice = "Building 201 (Main Admin) Room #104",
            mainDormName = "Blue Mir Hall (308/309)",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Blue Mir Hall has an internal gym and cafeteria. Student checklists focus on medical insurance registrations and Woori Bank debit cards.",
            usefulPointers = listOf(
                "Easy transit connection to Sangdo/Heukseok Subway Stations.",
                "Bring standard cash KRW for laundry card initialization."
            ),
            latLngString = Pair(37.5050, 126.9571)
        ),
        University(
            id = "hufs_seoul",
            name = "Hankuk University of Foreign Studies (한국외국어대학교)",
            location = "Dongdaemun, Seoul",
            EnglishName = "Hankuk University of Foreign Studies",
            dormitoryAddress = "107 Imun-ro, Dongdaemun-gu, Seoul (Globee Dorm)",
            emergencyContact = "+82-2-2173-2065 (OIA Desk)",
            globalCampusOffice = "Historical Building Fl 1",
            mainDormName = "Globee Dorm & International House",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "HUFS offers comprehensive multilingual onboarding. Special immigration counselors help complete ARC filings. Woori bank on campus manages student checks.",
            usefulPointers = listOf(
                "Extremely international environment with students from 80+ countries.",
                "Dorm passes require pre-uploaded digital tuberculosis scans."
            ),
            latLngString = Pair(37.5972, 127.0578)
        ),
        University(
            id = "sejong_seoul",
            name = "Sejong University (세종대학교)",
            location = "Gwangjin, Seoul",
            EnglishName = "Sejong University",
            dormitoryAddress = "209 Neungdong-ro, Gwangjin-gu, Seoul (Sejong International Student Dormitory)",
            emergencyContact = "+82-2-3408-3114 (Global Education Team)",
            globalCampusOffice = "Gwanggeto Hall Room #401",
            mainDormName = "Sejong International Dorm",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "Specialized support desks coordinates airport pickups and mobile account creation. Shinhan Bank handles campus student cards.",
            usefulPointers = listOf(
                "Located next to Children's Grand Park with beautiful running tracks.",
                "Submit medical screening in Korean to Gwanggeto Office."
            ),
            latLngString = Pair(37.5502, 127.0731)
        ),
        University(
            id = "konkuk_seoul",
            name = "Konkuk University (건국대학교)",
            location = "Gwangjin, Seoul",
            EnglishName = "Konkuk University",
            dormitoryAddress = "120 Neungdong-ro, Gwangjin-gu, Seoul (KU:L House)",
            emergencyContact = "+82-2-2049-6200 (Konkuk Center for International Faculty & Students)",
            globalCampusOffice = "Law School Building Fl 1",
            mainDormName = "KU:L House (Dream/Brave Halls)",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "KU:L House is a premier private dorm with high-end food courts and amenities. Hana Bank handles cards during orientation week.",
            usefulPointers = listOf(
                "Lake Ilgam sits at the heart of the beautiful campus.",
                "Register for health checkups at internal clinic on Day 2."
            ),
            latLngString = Pair(37.5407, 127.0793)
        ),

        // --- GYEONGGI & INCHEON UNIVERSITIES ---
        University(
            id = "inha_incheon",
            name = "Inha University (인하대학교)",
            location = "Incheon",
            EnglishName = "Inha University",
            dormitoryAddress = "100 Inha-ro, Michuhol-gu, Incheon (Inha Dormitory 3)",
            emergencyContact = "+82-32-860-7114 (Inha International Student Center)",
            globalCampusOffice = "Student Union Building Room #122",
            mainDormName = "Inha Dormitory 3",
            orientationDates = "Aug 26, 2026",
            guideBookPdfPreview = "Located near Incheon. Offers strong engineering and shipping guides. NH bank on-campus issues local checking accounts and student cards.",
            usefulPointers = listOf(
                "Easy subway access via Inha University Station.",
                "Proof of tuberculosis check-up required immediately on check-in day."
            ),
            latLngString = Pair(37.4497, 126.6543)
        ),
        University(
            id = "ajou_suwon",
            name = "Ajou University (아주대학교)",
            location = "Suwon, Gyeonggi",
            EnglishName = "Ajou University",
            dormitoryAddress = "206 World cup-ro, Yeongtong-gu, Suwon-si, Gyeonggi-do (Ajou International Dorm)",
            emergencyContact = "+82-31-219-2114 (OIA Desk)",
            globalCampusOffice = "Yulgok Hall Room #152",
            mainDormName = "Hwa-hong Hall / International Dorm",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Ajou sits in beautiful Suwon. Fully guided orientations. Shinhan Bank on campus handles multi-currency cards.",
            usefulPointers = listOf(
                "Suwon is famous for historic Hwaseong Fortress.",
                "Provide health and tuberculosis proof to Yulgok Hall office."
            ),
            latLngString = Pair(37.2831, 127.0461)
        ),
        University(
            id = "gachon_seongnam",
            name = "Gachon University (가천대학교)",
            location = "Seongnam, Gyeonggi",
            EnglishName = "Gachon University",
            dormitoryAddress = "1342 Seongnam-daero, Sujeong-gu, Seongnam-si, Gyeonggi-do (Gachon Dormitory)",
            emergencyContact = "+82-31-750-5114 (International Center)",
            globalCampusOffice = "Vision Tower Wing B Fl 3",
            mainDormName = "Gachon Hall Residence",
            orientationDates = "Aug 29, 2026",
            guideBookPdfPreview = "Directly connected to the Bundang subway line (Gachon University Station). High English-medium coursework assistance.",
            usefulPointers = listOf(
                "Vision Tower contains a massive central plaza with transit exits.",
                "Submit medical test forms in Korean or English."
            ),
            latLngString = Pair(37.4523, 127.1275)
        ),

        // --- REGIONAL & TECHNOLOGICAL UNIVERSITIES ---
        University(
            id = "kaist_daejeon",
            name = "KAIST (한국과학기술원)",
            location = "Daejeon",
            EnglishName = "KAIST",
            dormitoryAddress = "291 Daehak-ro, Yuseong-gu, Daejeon (Heemang/Mir Gwan)",
            emergencyContact = "+82-42-350-2114 (International Scholar & Student Services)",
            globalCampusOffice = "W2-1 Building Room #102",
            mainDormName = "Mir Gwan & Heemang Residence",
            orientationDates = "Aug 24, 2026",
            guideBookPdfPreview = "KAIST operates in Daejeon science hub. Fully English-medium research guides. Woori Bank scales special service desks for research checking accounts.",
            usefulPointers = listOf(
                "Submits automated biometric tracking cards for all residences.",
                "Requires highly rigorous tuberculosis screening before onboarding."
            ),
            latLngString = Pair(36.3721, 127.3604)
        ),
        University(
            id = "cnu_daejeon",
            name = "Chungnam National University (충남대학교)",
            location = "Daejeon",
            EnglishName = "Chungnam National University",
            dormitoryAddress = "99 Daehak-ro, Yuseong-gu, Daejeon (CNU Dormitory Hall 11)",
            emergencyContact = "+82-42-821-5114 (OIA Office)",
            globalCampusOffice = "Main Library Wing C Fl 1",
            mainDormName = "CNU Dormitory Complex",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "CNU holds comprehensive onboarding briefs. NH Bank coordinates cards on campus. Standard student transit leverages Yuseong Spa subway station.",
            usefulPointers = listOf(
                "Located near beautiful cherry blossom walkways along campus.",
                "Submit pre-entry health file to OIA Office."
            ),
            latLngString = Pair(36.3688, 127.3458)
        ),
        University(
            id = "jbnu_jeonju",
            name = "Jeonbuk National University (전북대학교)",
            location = "Jeonju, Jeonbuk",
            EnglishName = "Jeonbuk National University",
            dormitoryAddress = "567 Baekje-daero, Deokjin-gu, Jeonju-si, Jeollabuk-do (Chambit Hall)",
            emergencyContact = "+82-63-270-2114 (Office of International Cooperation)",
            globalCampusOffice = "New Silk Road Center Room #301",
            mainDormName = "Chambit Residence Hall",
            orientationDates = "Aug 27, 2026",
            guideBookPdfPreview = "Chambit Hall features advanced modular rooms and rich laundry sections. ARC physical cards are processed at New Silk Road Center.",
            usefulPointers = listOf(
                "Jeonju is Korea's food capital, famous for Hanok Village.",
                "Requires valid tuberculosis certificate and printed passport copy."
            ),
            latLngString = Pair(35.8468, 127.1293)
        ),
        University(
            id = "cnnu_gwangju",
            name = "Chonnam National University (전남대학교)",
            location = "Gwangju",
            EnglishName = "Chonnam National University",
            dormitoryAddress = "77 Yongbong-ro, Buk-gu, Gwangju (Bongji-gwan Residence)",
            emergencyContact = "+82-62-530-1114 (OIA Support)",
            globalCampusOffice = "G&R Hub Fl 2",
            mainDormName = "Yongbong Hall & Bongji-gwan",
            orientationDates = "Aug 28, 2026",
            guideBookPdfPreview = "Chonnam National holds orientation weeks outlining local transit, immigration, and mobile activations. NH Bank is situated inside Bongji Hall.",
            usefulPointers = listOf(
                "Bongji Lake sits on the beautiful campus area.",
                "Filing of ARC biometric reports done with OIA during orientation."
            ),
            latLngString = Pair(35.1796, 126.9095)
        ),
        University(
            id = "knu_daegu",
            name = "Kyungpook National University (경북대학교)",
            location = "Daegu",
            EnglishName = "Kyungpook National University",
            dormitoryAddress = "80 Daehak-ro, Buk-gu, Daegu (Cheomseong-gwan Dormitory)",
            emergencyContact = "+82-53-950-5114 (Office of International Affairs)",
            globalCampusOffice = "Global Plaza Room #608",
            mainDormName = "Cheomseong-gwan Residences",
            orientationDates = "Aug 25, 2026",
            guideBookPdfPreview = "Cheomseong-gwan is automated. Student ID collection requires health screens. Daegu immigration coordinates direct school registration campaigns.",
            usefulPointers = listOf(
                "Pre-enroll in dynamic student bank cards through Shinhan portals.",
                "Show printed health certificates on landing day check-in."
            ),
            latLngString = Pair(35.8906, 128.6121)
        ),
        University(
            id = "jnu_jeju",
            name = "Jeju National University (제주대학교)",
            location = "Jeju Island",
            EnglishName = "Jeju National University",
            dormitoryAddress = "102 Jejudae-daero, Jeju-si, Jeju-do (Ara Hall Dormitory)",
            emergencyContact = "+82-64-754-2114 (International Center)",
            globalCampusOffice = "Ara Muse Hall Room #204",
            mainDormName = "Ara Residence Hall",
            orientationDates = "Aug 29, 2026",
            guideBookPdfPreview = "Located on Jeju Island with stunning views. Offers dedicated airport pickup busses. Student account setup are managed via NH bank.",
            usefulPointers = listOf(
                "Scenic campus surrounded by Mount Hallasan scenery.",
                "Tuberculosis and health check proofs must be submitted in English."
            ),
            latLngString = Pair(33.4560, 126.5621)
        ),
        University(
            id = "kyungdong",
            name = "Kyungdong University (경동대학교)",
            location = "Goseong & Wonju & Yangju",
            EnglishName = "Kyungdong University",
            dormitoryAddress = "460 Bongpo-ro, Toseong-myeon, Goseong-gun, Gangwon-do (Global Campus Dormitory)",
            emergencyContact = "+82-33-639-0114 (Goseong International Support)",
            globalCampusOffice = "Global Building 1F Room #102",
            mainDormName = "Seorak Dormitory Residence Hall",
            orientationDates = "Aug 26, 2026",
            guideBookPdfPreview = "Kyungdong University provides integration guides for Goseong, Wonju, and Yangju campuses. Academic assistance, visa updates, and Alien Registration Card registrations are processed through the Office of International Affairs.",
            usefulPointers = listOf(
                "Beautiful physical scenery close to Seoraksan and northern coastlines.",
                "NH Nonghyup Bank is the campus provider for automated foreign student accounts.",
                "Submit tuberculosis status documents in English/Korean at check-in day."
            ),
            latLngString = Pair(38.2543, 128.5632)
        )
    )

    val productsList = listOf(
        // STARTER KITS
        Product(
            id = "starter_kit",
            name = "Essential Starter Kit",
            description = "Get all basic necessities required for your first night in South Korea. Prepared in collaboration with universities for effortless room setup.",
            price = 29.00,
            krwPrice = 39000,
            category = "Starter",
            subCategory = "Basic necessities",
            weightKg = 4.2,
            imgUrl = "img_starter_kit_1780881660186",
            rating = 4.8f,
            reviewsCount = 42,
            ingredients = "Plush synthetic pillow, microfiber duvet, Korean plug adapter (Type F), standard shower slippers, 2 hand towels, toilet papers",
            allergies = listOf("Synthetic filling"),
            estimatedDeliveryDays = 1,
            bundleProducts = listOf("Comfort Pillow", "Microfiber Duvet", "Plugs Adapter (EU/KR)", "Shower Slippers", "Toiletry Bundle")
        ),
        // STANDARD KITS
        Product(
            id = "standard_kit",
            name = "Standard Transit Ready Kit",
            description = "The absolute must-have bundle for students. Includes all basic home necessities plus a pre-loaded local T-Money Transit Card and standard eSIM coupon.",
            price = 49.00,
            krwPrice = 62000,
            category = "Standard",
            subCategory = "Essentials + transportation",
            weightKg = 5.0,
            imgUrl = "img_standard_kit_1780881673272",
            rating = 4.9f,
            reviewsCount = 118,
            ingredients = "All Starter bedding + 10,000 KRW pre-loaded T-Money commuter card, 30-Day unlimited LTE eSIM voucher, multi-extension power cord, and laundry bag setup.",
            estimatedDeliveryDays = 2,
            bundleProducts = listOf("Essential Bedding", "Pre-loaded T-Money Card (10,000 KRW)", "30-Day KT eSIM Code", "Korean Multi-tap Power Strip", "Dorm Laundry Kit")
        ),
        // PREMIUM KITS
        Product(
            id = "premium_kit",
            name = "Royal Premium Settle Kit",
            description = "VIP orientation & ultimate comfort kit. Sleep securely and arrive effortless. Bedding, high-speed WiFi setup, and full-service airport-to-dorm luggage logistics handling.",
            price = 89.00,
            krwPrice = 115000,
            category = "Premium",
            subCategory = "Full onboarding support",
            weightKg = 7.5,
            imgUrl = "img_premium_kit_1780881687587",
            rating = 4.95f,
            reviewsCount = 89,
            ingredients = "Premium Memory foam mattress pad, Egyptian cotton sheets, pillow + duvet, 90-Day Unlimited 5G eSIM, 20,000 KRW T-Money card, private taxi transport reservation ticket from Airport straight to Gangwon University.",
            estimatedDeliveryDays = 2,
            bundleProducts = listOf("Premium Mattress Overlay & Cotton Bedding", "90-Day Unlimited eSIM", "20,000 KRW T-Money Card", "Incheon-to-Dorm Direct Private Cab pickup voucher", "Desk Organizer Set")
        ),
        // UNIVERSITY KITS
        Product(
            id = "univ_knu_kit",
            name = "KNU Exclusive University Kit",
            description = "Specifically designed for Kangwon National University dorm rooms. Fits KNU dormitory dimension layouts perfectly.",
            price = 35.00,
            krwPrice = 45000,
            category = "University",
            subCategory = "KNU Essentials",
            weightKg = 4.5,
            imgUrl = "img_starter_kit_1780881660186",
            rating = 4.7f,
            reviewsCount = 31,
            ingredients = "KNU logo premium hoodie, custom KNU size single mattress sheets, 2 KNU hand-towels, power extension strip, local maps, custom locker padlocks.",
            estimatedDeliveryDays = 1,
            bundleProducts = listOf("KNU Logo Hoodie", "Custom-Fit Single Bedding Cover", "Towels with KNU Crest", "Dorm Combination Padlock")
        ),
        Product(
            id = "univ_hallym_kit",
            name = "Hallym University Custom Settle Kit",
            description = "Custom dormitory layout dimensions bedding and necessities tailored exclusively for Hallym University students.",
            price = 37.00,
            krwPrice = 48000,
            category = "University",
            subCategory = "Hallym Essentials",
            weightKg = 4.6,
            imgUrl = "img_starter_kit_1780881660186",
            rating = 4.6f,
            reviewsCount = 19,
            ingredients = "Hallym varsity t-shirt, custom size bed sheet set, Shinhan campus bank instructions card, Hallym University guide, reusable tumbler.",
            estimatedDeliveryDays = 1,
            bundleProducts = listOf("Hallym Varsity Shirt", "Custom Sheet Set", "Shinhan campus guidelines", "Hallym Tumbler")
        ),
        // NATIONALITY CUSTOM KITS
        Product(
            id = "country_nepal_kit",
            name = "Nepali Student Comfort Kit",
            description = "Felt like home! Includes specific instant noodle brand Wai Wai, home spice jars, standard adapter plugins, and dual language guides in Nepali & Korean.",
            price = 25.00,
            krwPrice = 32000,
            category = "Country",
            subCategory = "Customized by nationality",
            weightKg = 3.5,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.85f,
            reviewsCount = 28,
            ingredients = "10x Wai Wai Ready Noodles, authentic Nepali Garam Masala (100g), standard travel adapter plugin, bilingual cultural settling guide book.",
            estimatedDeliveryDays = 2,
            bundleProducts = listOf("10x Wai Wai Noodles", "Nepali Masalas", "Power Adapter", "Bilingual Support Guide Book")
        ),
        Product(
            id = "country_india_kit",
            name = "Indian Student Traditional Kit",
            description = "A warm bundle containing authentic ready-to-eat Indian meals (MTR/Haldirams), key traditional Indian spices (turmeric, garam masala), and premium adapter conversion kits.",
            price = 27.00,
            krwPrice = 35000,
            category = "Country",
            subCategory = "Customized by nationality",
            weightKg = 3.8,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.9f,
            reviewsCount = 37,
            ingredients = "5x Indian Ready-To-Eat entrees, MDH Garam Masala, Tata Tea (100g), high-grade multi-country power converter, and an English-South Korean bank simulator.",
            estimatedDeliveryDays = 2,
            bundleProducts = listOf("5x MTR Ready To Eat", "MDH Spices Combo", "Power Converter", "Settling Guide")
        ),

        // HALAL FOODS
        Product(
            id = "halal_indomie",
            name = "Halal Indomie Mi Goreng (5-Pack)",
            description = "World-famous Indonesian instant fried noodles. Fully certified Halal by Indonesian MUI.",
            price = 5.50,
            krwPrice = 7000,
            category = "Halal-Food",
            subCategory = "Halal instant foods",
            weightKg = 0.45,
            imgUrl = "img_halal_noodles_1780882125450",
            rating = 4.9f,
            reviewsCount = 250,
            ingredients = "Wheat flour, palm oil, salt, garlic powder, sweet soy sauce, chili sauce, fried shallots",
            allergies = listOf("Wheat", "Soy"),
            isHalalCertified = true,
            originCountry = "Indonesia",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 380kcal, Fat: 14g, Net Carbs: 56g",
            expiryDays = 120
        ),
        Product(
            id = "halal_samosas",
            name = "Halal Potato Samosas (Frozen 12pcs)",
            description = "Spicy and crisp frozen potato pastries. Certified Halal. Simply fry or bake to enjoy warm crispy tea snacks.",
            price = 11.00,
            krwPrice = 14000,
            category = "Halal-Food",
            subCategory = "Halal frozen foods",
            weightKg = 1.0,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.7f,
            reviewsCount = 64,
            ingredients = "Potato, Green peas, Spices, Wheat dough wrapper, salt, vegetable oil",
            allergies = listOf("Wheat"),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Calories per piece: 95kcal, Fat: 3g",
            expiryDays = 240
        ),
        Product(
            id = "halal_ghee",
            name = "Pure Halal Cow Ghee (500ml)",
            description = "Clarified butter, perfect for Indian/Pakistani cooking. Certified Halal.",
            price = 14.50,
            krwPrice = 19000,
            category = "Halal-Food",
            subCategory = "Halal snacks", // Using halal tags
            weightKg = 0.5,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.8f,
            reviewsCount = 45,
            ingredients = "100% Clarified Cow Butterfat",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 2,
            nutritionInfo = "100% Saturated fat goodness",
            expiryDays = 365
        ),
        Product(
            id = "halal_ranga_noodles",
            name = "Halal Bangladesh Ranga Noodles",
            description = "Delicious spiced noodles popular in Dhaka. Rich in standard South Asian spices.",
            price = 4.80,
            krwPrice = 6000,
            category = "Halal-Food",
            subCategory = "Halal instant foods",
            weightKg = 0.4,
            imgUrl = "img_halal_noodles_1780882125450",
            rating = 4.6f,
            reviewsCount = 18,
            ingredients = "Noodle cake, salt, cumin, coriander, ground cardamon, turmeric, spice flavoring",
            allergies = listOf("Wheat"),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 350kcal, Sodium: 800mg",
            expiryDays = 90
        ),
        Product(
            id = "halal_mango_juice",
            name = "Pran Mango Juice Drink (6-Pack)",
            description = "Sweet, refreshing real mango juice cups. Certified Halal by Bangladesh Islamic Foundation.",
            price = 6.20,
            krwPrice = 8000,
            category = "Halal-Food",
            subCategory = "Halal drinks",
            weightKg = 1.6,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.8f,
            reviewsCount = 112,
            ingredients = "Water, mango pulp 20%, cane sugar, citric acid, beta-carotene",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Rich in Vitamin C. Sugars: 15g per cup",
            expiryDays = 180
        ),

        // NON-HALAL FOODS
        Product(
            id = "korean_shin_ramyen",
            name = "Nongshim Shin Ramyun (5-Pack)",
            description = "The classic spicy Korean instant noodle. Soft/chewy noodle blocks with deep rich beef flavor.",
            price = 4.50,
            krwPrice = 5800,
            category = "Non-Halal-Food",
            subCategory = "Korea Ready Meals",
            weightKg = 0.6,
            imgUrl = "img_korean_foods_1780881701002",
            rating = 4.9f,
            reviewsCount = 540,
            ingredients = "Wheat flour, potato starch, beef bone extract, mushroom seasoning, red pepper flakes",
            allergies = listOf("Wheat", "Beef", "Soy"),
            isHalalCertified = false,
            originCountry = "South Korea",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 510kcal per pack, Sodium: 1790mg",
            expiryDays = 180
        ),
        Product(
            id = "lotte_pepero_choc",
            name = "Lotte Pepero Chocolate (4-Pack)",
            description = "Biscuit sticks coated in rich milk chocolate compounds. Premium Korean snacks.",
            price = 3.90,
            krwPrice = 5000,
            category = "Non-Halal-Food",
            subCategory = "Korean snacks",
            weightKg = 0.18,
            imgUrl = "img_korean_foods_1780881701002",
            rating = 4.8f,
            reviewsCount = 310,
            ingredients = "Wheat flour, cocoa butter mass, sugar, whole milk powder, almond fragments",
            allergies = listOf("Wheat", "Milk", "Almond"),
            isHalalCertified = false,
            originCountry = "South Korea",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Sugar: 12g, Calories: 170 per box",
            expiryDays = 270
        ),

        // COUNTRY DIVISION SPECIFIC PRODUCT (NESTED SECTIONS IN FOODS)
        Product(
            id = "nepal_waiwai",
            name = "Wai Wai Chicken Noodles (6-Pack)",
            description = "Pre-cooked brown noodles from Nepal. Commonly eaten straight from the packet with seasoning, or cooked as a hot spicy soup bowl.",
            price = 5.90,
            krwPrice = 7600,
            category = "Halal-Food",
            subCategory = "Nepal",
            weightKg = 0.45,
            imgUrl = "img_halal_noodles_1780882125450",
            rating = 4.95f,
            reviewsCount = 85,
            ingredients = "Wheat flour, chicken extract, MSG, shallot oil, chili, black pepper",
            allergies = listOf("Wheat"),
            isHalalCertified = true,
            originCountry = "Nepal",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Calories: 340kcal, Protein: 7g",
            expiryDays = 150
        ),
        Product(
            id = "nepal_bhuja",
            name = "Crispy Nepalese Bhuja (Puffed Rice)",
            description = "Crisp, premium quality puffed rice (Murmura), commonly used as a light Nepalese tea-time snack or mixed with fresh mustard oil, onions and chilies.",
            price = 3.50,
            krwPrice = 4500,
            category = "Halal-Food",
            subCategory = "Nepal",
            weightKg = 0.35,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.8f,
            reviewsCount = 39,
            ingredients = "Puffed rice, salt, trace vegetable oil",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Nepal",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Calories: 120kcal, Low Fat",
            expiryDays = 180
        ),
        Product(
            id = "nepal_chiura",
            name = "Nepalese Chiura (Beaten Rice)",
            description = "Premium traditional beaten flat rice flakes. Fits traditional Nepalese breakfasts, satisfyingly crunchy when roasted or combined with yogurt/curry.",
            price = 3.90,
            krwPrice = 5000,
            category = "Halal-Food",
            subCategory = "Nepal",
            weightKg = 0.5,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.7f,
            reviewsCount = 28,
            ingredients = "Flat beaten parboiled rice",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Nepal",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Calories: 180kcal, Fiber rich",
            expiryDays = 240
        ),
        Product(
            id = "nepal_roasted_soybeans",
            name = "Roasted Dry Soybeans (Bhatmas)",
            description = "Crunchy roasted golden brown Bhatmas with salt. High plant-based protein snack, commonly combined with Bhuja or eaten straight.",
            price = 4.20,
            krwPrice = 5500,
            category = "Halal-Food",
            subCategory = "Nepal",
            weightKg = 0.3,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.85f,
            reviewsCount = 51,
            ingredients = "100% Roasted soybeans, salt",
            allergies = listOf("Soy"),
            isHalalCertified = true,
            originCountry = "Nepal",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Protein: 14g, Calories: 160kcal",
            expiryDays = 120
        ),
        Product(
            id = "india_aloo_bhujia",
            name = "Haldirams Aloo Bhujia (Spicy Potato)",
            description = "Delectable crispy potato and chickpea flour noodle noodles infused with mint, ginger, and hot Indian spices. The classic late-night study snack.",
            price = 4.50,
            krwPrice = 5800,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.35,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.95f,
            reviewsCount = 180,
            ingredients = "Potato, chickpea flour, edible oil, salt, coriander, mint, dry mango powder, chili",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 540kcal per 100g, High sodium",
            expiryDays = 180
        ),
        Product(
            id = "india_bhujia",
            name = "Traditional Spiced Bhujia Sev",
            description = "Classic moth pulse and dew gram flour fried strands, seasoned to perfection with red chilies, black pepper, and cardamon.",
            price = 3.80,
            krwPrice = 4900,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.3,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.8f,
            reviewsCount = 92,
            ingredients = "Moth bean flour, gram flour, salt, spice seasonings, edible oil",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 512kcal, Protein: 12g",
            expiryDays = 180
        ),
        Product(
            id = "india_namkeen",
            name = "Royal Spiced Namkeen Mixture",
            description = "A colorful, crisp Indian salty-sour mixture containing gram flour noodles, golden cornflakes, roasted nuts, dry fruits, and puffed lentils.",
            price = 5.20,
            krwPrice = 6700,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.4,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.88f,
            reviewsCount = 114,
            ingredients = "Gram flour, split chickpeas, peanuts, cashews, raisins, spices, salt",
            allergies = listOf("Peanuts", "Cashews"),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Nutritious mixed crunch. Calories: 480kcal",
            expiryDays = 150
        ),
        Product(
            id = "india_murukku",
            name = "Crunchy Spiral Rice Murukku",
            description = "Traditional South Indian crispy spiral rice flour rings scented with aromatic sesame and carom (ajwain) seeds. Irresistibly crunchy.",
            price = 4.10,
            krwPrice = 5300,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.25,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.75f,
            reviewsCount = 67,
            ingredients = "Rice flour, black gram flour, sesame seeds, salt, vegetable oil, butter",
            allergies = listOf("Sesame", "Dairy"),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Calories: 150kcal per serving",
            expiryDays = 90
        ),
        Product(
            id = "india_banana_chips",
            name = "Kerala Style Crispy Banana Chips",
            description = "Crunchy sliced yellow plantain bananas cooked purely in fresh cold-pressed coconut oil with a sprinkle of healthy turmeric and salt.",
            price = 4.90,
            krwPrice = 6300,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.25,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.9f,
            reviewsCount = 135,
            ingredients = "Fresh raw bananas, coconut oil, salt, turmeric powder",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Rich in potassium, cooked in natural healthy oil",
            expiryDays = 120
        ),
        Product(
            id = "india_masala_peanuts",
            name = "Spicy Gram-Flour Masala Peanuts",
            description = "Premium high-grade peanuts coated with spicy, zesty gram-flour paste, deep fried until uniquely crisp. Excellent flavor pop.",
            price = 3.60,
            krwPrice = 4600,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.25,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.82f,
            reviewsCount = 89,
            ingredients = "Peanuts, Bengal gram flour, chili powder, ginger-garlic paste, dry mango powder, oil",
            allergies = listOf("Peanuts"),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 1,
            nutritionInfo = "High protein tea snack. 560kcal per Pack",
            expiryDays = 180
        ),
        Product(
            id = "pakistan_nimco",
            name = "Karachi Special Nimco Mix",
            description = "Classic salty-sour Pakistani snack mixture combining crunchy gram-flour sev ribbons, salty split peas, and crispy roasted chickpeas.",
            price = 4.50,
            krwPrice = 5800,
            category = "Halal-Food",
            subCategory = "Pakistan",
            weightKg = 0.35,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.85f,
            reviewsCount = 74,
            ingredients = "Gram flour, yellow split peas, cumin, coriander, salt, black salt, citric acid",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Tangy & salty. Calories: 450kcal",
            expiryDays = 180
        ),
        Product(
            id = "pakistan_daal_sev",
            name = "Daal Sev Crunchy Spiced Mix",
            description = "Popular Pakistani crispy snack combining thin seasoned gram-flour noodles with fried yellow split lentils. Savory tea-time dynamic companion.",
            price = 3.80,
            krwPrice = 4900,
            category = "Halal-Food",
            subCategory = "Pakistan",
            weightKg = 0.28,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.7f,
            reviewsCount = 42,
            ingredients = "Gram flour, yellow split pea, vegetable oil, hot Pakistani spices",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Savory snack. Protein: 9g, Fiber: 4g",
            expiryDays = 180
        ),
        Product(
            id = "pakistan_masala_peanuts",
            name = "Lahori Double-Coated Masala Peanuts",
            description = "Double crunchy Lahori recipe roasted masala peanuts seasoned with black salt, dry pomegranate seed powder (Anardana), and red pepper flakes.",
            price = 3.90,
            krwPrice = 5000,
            category = "Halal-Food",
            subCategory = "Pakistan",
            weightKg = 0.25,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.8f,
            reviewsCount = 59,
            ingredients = "Peanuts, chickpea batter, anardana, salt, Pakistani red chili, cornstarch",
            allergies = listOf("Peanuts"),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Zesty & highly hot. Calories: 520kcal",
            expiryDays = 180
        ),
        Product(
            id = "pakistan_roasted_chana",
            name = "Roasted Crunchy Salted Yellow Chana",
            description = "Dry roasted skinless yellow baby chickpeas. A staple healthy, oil-free snack from Pakistan loaded with dietary fiber and high plant protein.",
            price = 3.70,
            krwPrice = 4800,
            category = "Halal-Food",
            subCategory = "Pakistan",
            weightKg = 0.3,
            imgUrl = "img_south_asian_snacks_1780882593046",
            rating = 4.9f,
            reviewsCount = 81,
            ingredients = "Roasted chickpeas, iodized salt, turmeric dust",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Oil-Free. Protein: 18g, Fiber: 12g",
            expiryDays = 270
        ),
        Product(
            id = "bangladesh_chanachur",
            name = "Premium Bengali Spicy Chanachur",
            description = "Authentic crispy Dhaka-style hot snack mix. Loaded with crispy lentils, split peas, and seasoned gram-flour sticks.",
            price = 4.20,
            krwPrice = 5400,
            category = "Halal-Food",
            subCategory = "Bangladesh",
            weightKg = 0.3,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.9f,
            reviewsCount = 125,
            ingredients = "Gram flour, peanuts, peas, chili flakes, ginger extract, garlic, baking salt",
            allergies = listOf("Peanuts"),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Spicy snack. Calories: 420kcal",
            expiryDays = 150
        ),
        Product(
            id = "bangladesh_jhal_chanachur",
            name = "Dhakaiya Hot Jhal Chanachur",
            description = "Extra-hot and savory street-style chanachur mix featuring a prominent premium Bengali mustard oil aroma and hot green chili flakes.",
            price = 4.60,
            krwPrice = 5900,
            category = "Halal-Food",
            subCategory = "Bangladesh",
            weightKg = 0.3,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.93f,
            reviewsCount = 111,
            ingredients = "Bengal gram strands, peanuts, mustard oil accent, black pepper, red chili dust, dry mango powder",
            allergies = listOf("Peanuts", "Mustard"),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 1,
            nutritionInfo = "High heat index. Calories: 440kcal",
            expiryDays = 120
        ),
        Product(
            id = "bangladesh_roasted_peas",
            name = "Spicy Fried Green Peas (Motor Bhaja)",
            description = "A classic crunchy, spiced deep-fried green peas snack popular in Bangladeshi tea stalls, seasoned with black salt and hot red chilies.",
            price = 3.50,
            krwPrice = 4500,
            category = "Halal-Food",
            subCategory = "Bangladesh",
            weightKg = 0.25,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.75f,
            reviewsCount = 68,
            ingredients = "Heirloom green peas, vegetable oil, chili, black salt, cumin",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 280kcal per portion",
            expiryDays = 180
        ),
        Product(
            id = "bangladesh_spicy_puffed_rice",
            name = "Premium Dhakaiya Jhal Muri Mix",
            description = "Zesty spiced puffed rice mix containing tiny potato bhujia, crunchy lentils, mustard oil spices, and crisp puffed rice flakes. Easy to assemble street snack.",
            price = 3.90,
            krwPrice = 5000,
            category = "Halal-Food",
            subCategory = "Bangladesh",
            weightKg = 0.25,
            imgUrl = "img_puffed_rice_1780882608924",
            rating = 4.88f,
            reviewsCount = 95,
            ingredients = "Puffed rice, chanachur particles, mustard seasoning oil pack, dry lime dust",
            allergies = listOf("Peanuts", "Mustard"),
            isHalalCertified = true,
            originCountry = "Bangladesh",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Vibrant light street snack. Calories: 190kcal",
            expiryDays = 120
        ),
        Product(
            id = "india_mtr_paneer",
            name = "MTR Paneer Butter Masala (Ready-to-eat)",
            description = "Rich cottage cheese cubes cooked in spiced tomato-cashew curry cream.",
            price = 3.80,
            krwPrice = 4900,
            category = "Halal-Food",
            subCategory = "India",
            weightKg = 0.3,
            imgUrl = "img_indian_meals_1780882139370",
            rating = 4.75f,
            reviewsCount = 92,
            ingredients = "Cottage cheese, Tomato, Butter, Onions, Ginger, Cashew paste, Spices",
            allergies = listOf("Milk", "Cashew"),
            isHalalCertified = true,
            originCountry = "India",
            estimatedDeliveryDays = 2,
            nutritionInfo = "All natural. No preservatives.",
            expiryDays = 365
        ),
        Product(
            id = "pakistan_laziza_kheer",
            name = "Laziza Kheer Mix (Pistachio)",
            description = "Delicious Pakistani rice pudding dessert kit flavored with saffron and pistachios.",
            price = 2.90,
            krwPrice = 3800,
            category = "Halal-Food",
            subCategory = "Pakistan",
            weightKg = 0.15,
            imgUrl = "img_halal_foods_1780881716620",
            rating = 4.8f,
            reviewsCount = 34,
            ingredients = "Sugar, processed rice, pistachio, cardamon flavor",
            allergies = listOf("Pistachio"),
            isHalalCertified = true,
            originCountry = "Pakistan",
            estimatedDeliveryDays = 2,
            nutritionInfo = "Carbs: 45g per serving",
            expiryDays = 360
        ),
        Product(
            id = "southeast_asia_phomama",
            name = "Thai Mama Tom Yum Shrimp Noodles",
            description = "Spicy Tom Yum flavor hot soup noodle cups with authentic lemongrass zest.",
            price = 5.20,
            krwPrice = 6700,
            category = "Halal-Food",
            subCategory = "Southeast Asia",
            weightKg = 0.5,
            imgUrl = "img_halal_noodles_1780882125450",
            rating = 4.85f,
            reviewsCount = 76,
            ingredients = "Wheat flour, shrimp paste, lemongrass, kaffir lime leaf, chili oil",
            allergies = listOf("Wheat", "Crustacean"),
            isHalalCertified = true,
            originCountry = "Thailand",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Spicy zesty soup. 320kcal",
            expiryDays = 180
        ),
        Product(
            id = "kr_banana_milk",
            name = "Binggrae Banana Flavored Milk (6-Pack)",
            description = "The absolute legendary sweet banana flavored milk. A classic, comforting favorite among all international students in Korea.",
            price = 9000.00,
            krwPrice = 9000,
            category = "Non-Halal-Food",
            subCategory = "Korean snacks",
            weightKg = 1.2,
            imgUrl = "img_banana_milk_1780882154019",
            rating = 4.95f,
            reviewsCount = 422,
            ingredients = "Milk, sugar, banana juice concentrate, natural banana flavor",
            allergies = listOf("Milk"),
            isHalalCertified = false,
            originCountry = "South Korea",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 200kcal per bottle, Calcium rich",
            expiryDays = 14
        ),
        Product(
            id = "multi_adapter_3pack",
            name = "Premium Grounded Type-F Multi-Plug (3-Pack)",
            description = "Crucial Type-F rounded adapters with grounding protection. Comfortably charge your high-power laptops, hair dryers, and mobile devices safely in Korean dormitories.",
            price = 12000.00,
            krwPrice = 12000,
            category = "Starter",
            subCategory = "Tech survival",
            weightKg = 0.3,
            rating = 4.9f,
            reviewsCount = 85,
            ingredients = "High-grade fire-retardant brass core and copper terminal contacts",
            allergies = emptyList(),
            isHalalCertified = false,
            originCountry = "South Korea",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Standard 250V 16A maximum rating",
            expiryDays = 1000
        ),
        Product(
            id = "vietnam_g7_coffee",
            name = "G7 Vietnamese Instant Coffee (50-Pack)",
            description = "Rich, highly aromatic robusta coffee sachets. The ultimate academic study fuel for long lecture preparation nights.",
            price = 8500.00,
            krwPrice = 8500,
            category = "Halal-Food",
            subCategory = "Vietnam",
            weightKg = 0.8,
            imgUrl = "img_instant_coffee_1780882167044",
            rating = 4.8f,
            reviewsCount = 143,
            ingredients = "100% Vietnamese Robusta coffee extract",
            allergies = emptyList(),
            isHalalCertified = true,
            originCountry = "Vietnam",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Zero sugar, pure high caffeine energy",
            expiryDays = 365
        ),
        Product(
            id = "halal_chicken_tikka",
            name = "Halal Premium Chicken Tikka Masala (2-Pack)",
            description = "Delicious spiced roasted chicken pieces in a thick, rich Punjabi curry gravy. 100% Halal certified. Microwavable in 2 minutes inside your student pantry.",
            price = 15000.00,
            krwPrice = 15000,
            category = "Halal-Food",
            subCategory = "Halal ready meals",
            weightKg = 0.6,
            imgUrl = "img_indian_meals_1780882139370",
            rating = 4.88f,
            reviewsCount = 94,
            ingredients = "Halal chicken breast, yogurt, tomato paste, ginger, garlic, garam masala, heavy cream",
            allergies = listOf("Dairy", "Mustard"),
            isHalalCertified = true,
            originCountry = "United Kingdom",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Calories: 450kcal, Protein: 32g, Low carb",
            expiryDays = 180
        ),
        Product(
            id = "student_fleece_blanket",
            name = "Cozy Double-Layer Fleece Study Blanket",
            description = "An ultra-soft flannel fleece blanket designed to keep you cozy during long nighttime study sessions at your dormitory desk.",
            price = 18000.00,
            krwPrice = 18000,
            category = "Starter",
            subCategory = "Dormitory Comfort",
            weightKg = 1.1,
            rating = 4.92f,
            reviewsCount = 104,
            ingredients = "100% premium microfiber polyester flannel fabric",
            allergies = emptyList(),
            isHalalCertified = false,
            originCountry = "South Korea",
            estimatedDeliveryDays = 1,
            nutritionInfo = "Machine washable, anti-pilling technology",
            expiryDays = 2000
        )
    )

    val servicesList = listOf(
        StudentService(
            id = "sim_kt_unlimited",
            title = "KT 90-Day Unlimited 4G/LTE eSIM",
            description = "Unlocks on local cellular networks instantly on landing. Direct activation using passports via verified online submission.",
            price = 25000.00,
            category = "SIM",
            features = listOf(
                "No physical card needed. QR delivered on email.",
                "Unlimited LTE data speed (up to 5Mbps after 3GB/day).",
                "Includes incoming phone calls/texts.",
                "Perfect for setting up KakaoTalk & Naver Maps."
            ),
            detailGuide = "Ensure your phone is carrier-unlocked. Scan QR code within 24 hours of landing at Incheon Airport. KYC passport image match is required to submit activation forms on landing.",
            estProcessingTime = "Active within 1 hour of landing"
        ),
        StudentService(
            id = "pickup_gangwon_shuttle",
            title = "Airport Private Taxi Pickup (Incheon to Gangwon)",
            description = "Avoid transit confusion on heavy baggage day. Professional local driver meets you at arrival gate, packing you and your baggage directly to your dormitory dorm door.",
            price = 80000.00,
            category = "Onboarding",
            features = listOf(
                "Meet-and-greet in arrivals lobby with name sign.",
                "Private toll-fee covered sedan for up to 3 large suitcases.",
                "Stops directly in front of your assigned Gangwon university dorm.",
                "Driver speaks basic conversational English/Chinese."
            ),
            detailGuide = "Provide flight number and targeted landing details during purchase. Clean tracking system alerts drivers in real-time regarding delayed flights. Direct airport gate terminal exit pickup.",
            estProcessingTime = "Scheduled to match flight terminal landing"
        ),
        StudentService(
            id = "bank_shinhan_onboard",
            title = "Korean Campus Bank Account Opening Assistance",
            description = "Fast-track Shinhan or Woori bank setups. Our Korean representative schedules a 1-on-1 translation briefing at the campus branch so you walk out with a working debit card & mobile app active.",
            price = 7000.00,
            category = "Admin",
            features = listOf(
                "Document preparation guidelines (proof files, certificates).",
                "Assigned native-speaker assistant for physical branch visit.",
                "Mobile banking app setup in English and digital certificate installation."
            ),
            detailGuide = "Requires physical presence, Passport or ARC, and Certificate of Admission (CoA). Assures successful Korean check-card setup so you can pay seamlessly via Samsung Pay, KakaoPay, or Coupang.",
            estProcessingTime = "1-hour physical branch appointment"
        ),
        StudentService(
            id = "immigration_arc_file",
            title = "Alien Registration Card (ARC) Fast Assistance",
            description = "Expert filing package for Chuncheon/Korean Immigration. Generates completed paperwork and schedules priority visits, reducing waiting times by up to 4 weeks.",
            price = 12000.00,
            category = "Admin",
            features = listOf(
                "Pre-populated forms in correct Korean fields.",
                "Priority reservation slot secured at immigration offices.",
                "Translation service for address proof documents and dorm receipts."
            ),
            detailGuide = "ARC is required in South Korea for internet verification, phone bills, and online shopping (Coupang). Standard registration takes over 6 weeks; but our pre-audit cuts file turnaround significantly.",
            estProcessingTime = "Filing completes in 5 business days"
        )
    )

    // Initial mock feedback reviews
    val defaultReviews = mutableListOf(
        UserFeedback("1", "starter_kit", "Aarav Sharma (India)", 5, "Saved my absolute first night! Arrived at 11 PM and Toegye-gwan dorm was empty. Bedding was warm and pillow cozy.", "Product Experience", true),
        UserFeedback("2", "starter_kit", "Rina Thapa (Nepal)", 5, "Bedding fits Chuncheon campus dorms easily. Slipper quality is solid, very essential for Korean style washrooms.", "Product Experience", true),
        UserFeedback("3", "halal_indomie", "Siti Aminah (Malaysia)", 5, "MUI Halal sticker present. Authentic spicy sweet Indomie taste, great to have ready in KNU dorms.", "Product Experience", true),
        UserFeedback("4", "sim_kt_unlimited", "Devon Lin (USA)", 4, "Scanned the eSIM QR code while queueing for immigration. Worked immediately. Fast internet in Chuncheon.", "Product Experience", true)
    )
}
