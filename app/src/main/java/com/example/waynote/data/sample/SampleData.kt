package com.example.waynote

import com.example.waynote.R

val popularDestinations = listOf(
    Destination(
        id = 1,
        title = "Mount Tai",
        description = "Sunrise hikes above the clouds in Shandong.",
        imageRes = R.drawable.mount_tai,
        details = "Climb the 7,000 stone steps through ancient temples to reach the South Heavenly Gate and watch the sun rise above a sea of clouds.",
        developmentTime = "Open year-round; best at sunrise (04:00-08:00).",
        ticketInfo = "Entrance ¥115; cable car ¥100 (one-way).",
        keywords = listOf("Sunrise", "Temples", "Hiking", "Sacred Mountain")
    ),
    Destination(
        id = 2,
        title = "Mount Huang",
        description = "Iconic granite peaks and sea of clouds vistas.",
        imageRes = R.drawable.mount_huang,
        details = "Walk along the narrow stone ridges, ride the cable cars above jagged spires, and stay for the famous \"cloud ocean\" sunsets.",
        developmentTime = "Peak season April–November; sunrise decks open 24h.",
        ticketInfo = "Entrance ¥190; Yungu cable car ¥90 (one-way).",
        keywords = listOf("Sea of Clouds", "Granite Peaks", "Cable Car", "Sunrise")
    ),
    Destination(
        id = 3,
        title = "Zhangjiajie",
        description = "Sandstone pillars that inspired floating mountains.",
        imageRes = R.drawable.zhangjiajie,
        details = "Explore glass-bottom walkways, misty forests, and towering sandstone columns that inspired the landscapes in Avatar.",
        developmentTime = "Best in spring/autumn; early mornings for clear views.",
        ticketInfo = "Park pass ¥248 (valid 4 days); glass bridge ¥138.",
        keywords = listOf("Avatar", "Pillars", "Glass Bridge", "Forest")
    ),
    Destination(
        id = 4,
        title = "Guilin",
        description = "Karst hills and river cruises through lush valleys.",
        imageRes = R.drawable.guilin,
        details = "Cruise the Li River at sunrise, bike through the karst countryside, and take in panoramic views from Xianggong Hill.",
        developmentTime = "Li River cruises 09:00–14:00; sunrise views ideal Mar–Oct.",
        ticketInfo = "Li River cruise ¥210+; Xianggong Hill ¥60.",
        keywords = listOf("Karst", "River Cruise", "Cycling", "Photography")
    )
)

val destinationTranslations = mapOf(
    1 to DestinationTranslation(
        titleZh = "泰山",
        descriptionZh = "山东云海之上的日出徒步。",
        detailsZh = "踏上7000级石阶穿过古老寺庙，抵达南天门迎接云海日出。",
        developmentTimeZh = "全年开放；最佳观日出时间 04:00-08:00。",
        ticketInfoZh = "门票 ¥115；索道单程 ¥100。",
        keywordsZh = listOf("日出", "寺庙", "徒步", "五岳")
    ),
    2 to DestinationTranslation(
        titleZh = "黄山",
        descriptionZh = "花岗岩奇峰与云海胜景。",
        detailsZh = "沿狭窄石脊漫步，乘索道俯瞰峭壁，等待著名的“云海”日落。",
        developmentTimeZh = "旺季4-11月；日出观景台全天开放。",
        ticketInfoZh = "门票 ¥190；云谷索道单程 ¥90。",
        keywordsZh = listOf("云海", "奇峰", "索道", "日出")
    ),
    3 to DestinationTranslation(
        titleZh = "张家界",
        descriptionZh = "启发悬浮山的砂岩石柱。",
        detailsZh = "漫步玻璃栈道、云雾森林与高耸石柱，仿佛置身《阿凡达》场景。",
        developmentTimeZh = "春秋最佳；清晨视野更佳。",
        ticketInfoZh = "门票 ¥248（4天有效）；玻璃桥 ¥138。",
        keywordsZh = listOf("阿凡达", "石柱", "玻璃桥", "森林")
    ),
    4 to DestinationTranslation(
        titleZh = "桂林",
        descriptionZh = "喀斯特山水与江面游船。",
        detailsZh = "黎明乘坐漓江游船、骑行喀斯特山间、登上相公山俯瞰全景。",
        developmentTimeZh = "漓江游船 09:00–14:00；3-10月日出最佳。",
        ticketInfoZh = "漓江游船 ¥210+；相公山 ¥60。",
        keywordsZh = listOf("喀斯特", "漓江游船", "骑行", "摄影")
    )
)

val guangzhouShanghaiFlights = listOf(
    FlightOption(
        id = 1,
        airline = "China Southern",
        flightNumber = "CZ3531",
        logoUrl = "https://imagepphcloud.thepaper.cn/pph/image/335/680/373.jpg",
        from = "Guangzhou (CAN)",
        to = "Shanghai (SHA)",
        departureTime = "08:10",
        arrivalTime = "10:35",
        durationLabel = "2h 25m",
        durationMinutes = 145,
        departureMinutes = 8 * 60 + 10,
        tag = FlightBadge.Recommended,
        aircraft = "Airbus A321neo",
        fares = listOf(
            FlightFare(
                cabin = FlightCabin.Economy,
                price = 720,
                baggage = "20kg",
                carryOn = "7kg",
                refundRule = "Refund ¥240 before 24h; ¥360 within 24h",
                changeRule = "Change ¥120"
            ),
            FlightFare(
                cabin = FlightCabin.Business,
                price = 1380,
                baggage = "30kg",
                carryOn = "10kg",
                refundRule = "Refund ¥320 before 2h",
                changeRule = "Free once; ¥200 afterwards"
            )
        )
    ),
    FlightOption(
        id = 2,
        airline = "Spring Airlines",
        flightNumber = "9C8888",
        logoUrl = "https://bkimg.cdn.bcebos.com/pic/9a504fc2d5628535f0d984309fef76c6a7ef63e1",
        from = "Guangzhou (CAN)",
        to = "Shanghai (PVG)",
        departureTime = "09:20",
        arrivalTime = "11:55",
        durationLabel = "2h 35m",
        durationMinutes = 155,
        departureMinutes = 9 * 60 + 20,
        tag = FlightBadge.Cheapest,
        aircraft = "Airbus A320",
        fares = listOf(
            FlightFare(
                cabin = FlightCabin.Economy,
                price = 580,
                baggage = "15kg",
                carryOn = "7kg",
                refundRule = "Refund ¥200 before 24h",
                changeRule = "Change ¥90"
            ),
            FlightFare(
                cabin = FlightCabin.Business,
                price = 1180,
                baggage = "25kg",
                carryOn = "10kg",
                refundRule = "Refund ¥260 before 2h",
                changeRule = "Change ¥160"
            )
        )
    ),
    FlightOption(
        id = 3,
        airline = "Air China",
        flightNumber = "MU5208",
        logoUrl = "https://mmbiz.qpic.cn/mmbiz_png/3EfBJFibml6xDfRAEAJPWofRt4H8wViaQbOZ2q7DSKiap2q0W8StPTyiamFmq7C29VQO0dF3y6FQCu3FB01c5TTfkw/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1&tp=webp#imgIndex=1",
        from = "Guangzhou (CAN)",
        to = "Shanghai (SHA)",
        departureTime = "18:15",
        arrivalTime = "20:25",
        durationLabel = "2h 10m",
        durationMinutes = 130,
        departureMinutes = 18 * 60 + 15,
        tag = FlightBadge.Fastest,
        aircraft = "Boeing 787-9",
        fares = listOf(
            FlightFare(
                cabin = FlightCabin.Economy,
                price = 860,
                baggage = "20kg",
                carryOn = "7kg",
                refundRule = "Refund ¥300 before 24h",
                changeRule = "Change ¥150"
            ),
            FlightFare(
                cabin = FlightCabin.Business,
                price = 1580,
                baggage = "30kg",
                carryOn = "10kg",
                refundRule = "Refund ¥420 before 2h",
                changeRule = "Free once; ¥220 afterwards"
            )
        )
    )
)

val recommendedCommunityPosts = listOf(
    CommunityPost(
        id = 1,
        author = CommunityAuthor(
            name = "Mia · Waynote",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=60"
        ),
        title = "Kyoto dawn: Fushimi Inari to Uji rail ride",
        summary = "Skip the crowds via the back trail, catch misty empty tracks, and finish with matcha by the Uji river.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1504208434309-cb69f4fe52b0?w=900&auto=format&fit=crop&q=80",
                description = "Fushimi Inari morning"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1467269204594-9661b134dd2b?w=900&auto=format&fit=crop&q=80",
                description = "Kyoto railway"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1571474004506-04c1b7540f4b?w=900&auto=format&fit=crop&q=80",
                description = "Uji river"
            )
        ),
        tags = listOf("Kyoto", "Sunrise", "Local route"),
        location = "Kyoto, Japan",
        device = "iPhone 15 Pro",
        likes = 248,
        comments = 53,
        saves = 121,
        highlight = "30-min sunrise route"
    ),
    CommunityPost(
        id = 2,
        author = CommunityAuthor(
            name = "Leo · Alps Basecamp",
            avatarUrl = "https://images.unsplash.com/photo-1528892952291-009c663ce843?auto=format&fit=crop&w=200&q=80"
        ),
        title = "Swiss Alps hut night: routes, bookings, pack list",
        summary = "What I wish I knew before staying in a mountain hut near Grindelwald. Trains, hut etiquette, and food you actually get.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1517824748781-ce1c7c2bf82f?w=900&auto=format&fit=crop&q=80",
                description = "Hut exterior at blue hour"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1501854140801-50d01698950b?w=900&auto=format&fit=crop&q=80",
                description = "Trail to hut"
            )
        ),
        tags = listOf("Switzerland", "Hiking", "Hut"),
        location = "Grindelwald",
        device = "Sony A7C II",
        likes = 412,
        comments = 88,
        saves = 209,
        highlight = "Hut FAQ"
    ),
    CommunityPost(
        id = 3,
        author = CommunityAuthor(
            name = "Chen · Tea Trails",
            avatarUrl = "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&auto=format&fit=crop&q=60"
        ),
        title = "Hangzhou 24h: tea hills to night boats",
        summary = "Morning Longjing harvest, slow lunch in Meijiawu, then West Lake sunset boat. Map + Didi tips inside.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1540131810-66b9c1b3ac31?w=900&auto=format&fit=crop&q=80",
                description = "Longjing tea hills"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1505764706515-aa95265c5abc?w=900&auto=format&fit=crop&q=80",
                description = "West Lake boat"
            )
        ),
        tags = listOf("Hangzhou", "Tea", "Day trip"),
        location = "Hangzhou, China",
        device = "Fujifilm X100V",
        likes = 186,
        comments = 24,
        saves = 97,
        highlight = "24h template"
    )
)

val followingCommunityPosts = listOf(
    CommunityPost(
        id = 4,
        author = CommunityAuthor(
            name = "Nora · Slow Alps",
            avatarUrl = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&auto=format&fit=crop&q=60"
        ),
        title = "Dolomites via ferrata weekend",
        summary = "Beginner-friendly ladders, gear rentals, and where to sleep near Lago di Braies.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1469474968028-56623f02e42e?w=900&auto=format&fit=crop&q=80",
                description = "Via ferrata view"
            )
        ),
        tags = listOf("Dolomites", "Hiking", "Weekend"),
        location = "South Tyrol, Italy",
        device = "GoPro 12",
        likes = 132,
        comments = 19,
        saves = 74,
        highlight = "Beginner gear list"
    ),
    CommunityPost(
        id = 5,
        author = CommunityAuthor(
            name = "Ken · City Routes",
            avatarUrl = "https://images.unsplash.com/photo-1544723795-3fb6469f5b39?w=400&auto=format&fit=crop&q=60"
        ),
        title = "Singapore rain day back-up plan",
        summary = "Indoor hawker crawl, Peranakan Museum, and Jewel waterfall timing with kids.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=900&auto=format&fit=crop&q=80",
                description = "Jewel waterfall"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1541417904950-b855846fe074?w=900&auto=format&fit=crop&q=80",
                description = "Hawker food"
            )
        ),
        tags = listOf("Singapore", "Family", "Rainy day"),
        location = "Singapore",
        device = "Pixel 8 Pro",
        likes = 205,
        comments = 44,
        saves = 110,
        highlight = "Kid-friendly"
    )
)

val challengeCommunityPosts = listOf(
    CommunityPost(
        id = 6,
        author = CommunityAuthor(
            name = "Mia · Waynote",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=400&auto=format&fit=crop&q=60"
        ),
        title = "72-hour film challenge: Guilin karst sunrise",
        summary = "Join this week's challenge: capture sunrise motion from Guilin karst peaks. Winner gets Zhiyun gimbal.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Video,
                thumbnailUrl = "https://images.unsplash.com/photo-1516375195448-0b5455d622e0?w=900&auto=format&fit=crop&q=80",
                description = "Guilin sunrise motion",
                durationLabel = "00:43"
            )
        ),
        tags = listOf("Challenge", "Sunrise", "Video"),
        location = "Guilin, China",
        device = "DJI Pocket 3",
        likes = 320,
        comments = 76,
        saves = 142,
        highlight = "Weekly challenge"
    ),
    CommunityPost(
        id = 7,
        author = CommunityAuthor(
            name = "Alex · Gear Lab",
            avatarUrl = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=400&auto=format&fit=crop&q=60"
        ),
        title = "Trail fail to trail ready: pack test",
        summary = "Three loadouts for 2-day treks under 8kg. Free pack fitting for top entries.",
        media = listOf(
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1472214103451-9374bd1c798e?w=900&auto=format&fit=crop&q=80",
                description = "Pack lineup"
            ),
            CommunityMedia(
                type = MediaType.Image,
                thumbnailUrl = "https://images.unsplash.com/photo-1508766206392-8bd5cf550d1b?w=900&auto=format&fit=crop&q=80",
                description = "Trail test"
            )
        ),
        tags = listOf("Challenge", "Gear", "Backpacking"),
        location = "Any trail",
        device = "Sony A7C II",
        likes = 241,
        comments = 58,
        saves = 164,
        highlight = "Challenge in progress"
    )
)
