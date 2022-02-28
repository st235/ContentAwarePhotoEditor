package st235.com.github.sampleapp

val FEED_ITEMS = listOf(
    FeedItem(
        id = 1,
        description = "Never measure the height of a mountain until you reach the top. Then you will see how low it was.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "120K",
            isLiked = false
        ),
        tags = listOf("#mountains", "#night", "#beauty", "#nature"),
        image = R.drawable.mountain_1
    ),
    FeedItem(
        id = 2,
        description = "Somewhere between the bottom of the climb and the summit is the answer to the mystery why we climb.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "50K",
            isLiked = false
        ),
        tags = listOf("mountains", "🇫🇷 France", "snow", "skiing", "hiking", "sport", "climbing"),
        image = R.drawable.mountain_2
    ),
    FeedItem(
        id = 3,
        description = "The ocean makes me feel really small and it makes me put my whole life into perspective.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "1.2M",
            isLiked = false
        ),
        tags = listOf("ocean", "sand", "beach"),
        image = R.drawable.ocean_1
    ),
    FeedItem(
        id = 4,
        description = "There's nothing wrong with enjoying looking at the surface of the ocean itself, except that when you finally see what goes on underwater, you realize that you've been missing the whole point of the ocean. Staying on the surface all the time is like going to the circus and staring at the outside of the tent.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "99K",
            isLiked = false
        ),
        tags = listOf("#ocean"),
        image = R.drawable.ocean_2
    ),
    FeedItem(
        id = 5,
        description = "I go to Paris, I go to London, I go to Rome, and I always say, 'There's no place like New York. It's the most exciting city in the world now.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "354K",
            isLiked = false
        ),
        tags = listOf("New-York", "London", "Rome", "Paris"),
        image = R.drawable.city_1
    ),
    FeedItem(
        id = 6,
        description = "Owners of dogs will have noticed that, if you provide them with food and water and shelter and affection, they will think you are God. Whereas owners of cats are compelled to realize that, if you provide them with food and water and affection, they draw the conclusion that they are God.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "∞",
            isLiked = true
        ),
        tags = listOf("cat", "pets", "home", "dogs",  "food"),
        image = R.drawable.cat_1
    ),
    FeedItem(
        id = 7,
        description = "New York now leads the world's great cities in the number of people around whom you shouldn't make a sudden move.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "6K",
            isLiked = true
        ),
        tags = listOf("city", "new-york"),
        image = R.drawable.city_2
    ),
    FeedItem(
        id = 8,
        description = "Cats know how to obtain food without labor, shelter without confinement, and love without penalties.",
        author = Author(
            avatar = R.drawable.avatar,
            name = "Alexander Dadukin"
        ),
        likes = Likes(
            count = "611K",
            isLiked = true
        ),
        tags = listOf("cat", "love ❤️"),
        image = R.drawable.cat_2
    )
)
