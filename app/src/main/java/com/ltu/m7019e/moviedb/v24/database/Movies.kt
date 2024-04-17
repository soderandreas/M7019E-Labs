package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.Movie

class Movies {
    val genreDesc: Map<String, String> = mapOf(
        "Animation" to "Animation is a filmmaking technique by which still images are manipulated to create moving images. In traditional animation, images are drawn or painted by hand on transparent celluloid sheets (cels) to be photographed and exhibited on film. Animation has been recognized as an artistic medium, specifically within the entertainment industry. Many animations are computer animations made with computer-generated imagery (CGI). Stop motion animation, in particular claymation, has continued to exist alongside these other forms.",
        "Family" to "A family film, is a film genre that generally relates to children in the context of home and family. Children's films are made specifically for children and not necessarily for a general audience, while family films are made for a wider appeal with a general audience in mind. Children's films come in several major genres like realism, fantasy, adventure, war, musicals, comedy, and literary adaptations.",
        "Fantasy" to "Fantasy films are films that belong to the fantasy genre with fantastic themes, usually magic, supernatural events, mythology, folklore, or exotic fantasy worlds. The genre is considered a form of speculative fiction alongside science fiction films and horror films, although the genres do overlap. Fantasy films often have an element of magic, myth, wonder, escapism, and the extraordinary.",
        "Action" to "The action film is a film genre that predominantly features chase sequences, fights, shootouts, explosions, and stunt work. The specifics of what constitutes an action film has been in scholarly debate since the 1980s. While some scholars such as David Bordwell suggested they were films that favor spectacle to storytelling, others such as Goeff King stated they allow the scenes of spectacle to be attuned to story telling. Action films are often hybrid with other genres, mixing into various forms ranging to comedies, science fiction films, and horror films.",
        "Adventure" to "An adventure film is a form of adventure fiction, and is a genre of film. Subgenres of adventure films include swashbuckler films, pirate films, and survival films. Adventure films may also be combined with other film genres such as action, comedy, drama, fantasy, science fiction, family, horror, war, or the medium of animation.",
        "Thriller" to "Thriller film, also known as suspense film or suspense thriller, is a broad film genre that evokes excitement and suspense in the audience. The suspense element found in most films' plots is particularly exploited by the filmmaker in this genre. Tension is created by delaying what the audience sees as inevitable, and is built through situations that are menacing or where escape seems impossible.",
        "Drama" to "In film and television, drama is a category or genre of narrative fiction (or semi-fiction) intended to be more serious than humorous in tone. The drama of this kind is usually qualified with additional terms that specify its particular super-genre, macro-genre, or micro-genre, such as soap opera, police crime drama, political drama, legal drama, historical drama, domestic drama, teen drama, and comedy-drama (dramedy). These terms tend to indicate a particular setting or subject matter, or they combine a drama's otherwise serious tone with elements that encourage a broader range of moods. To these ends, a primary element in a drama is the occurrence of conflict—emotional, social, or otherwise—and its resolution in the course of the storyline.",
        "Comedy" to "A comedy film is a film genre that emphasizes humor. These films are designed to amuse audiences and make them laugh. Films in this genre typically have a happy ending, with dark comedy being an exception to this rule. Comedy is one of the oldest genres in film, and it is derived from classical comedy in theatre. Some of the earliest silent films were slapstick comedies, which often relied on visual depictions, such as sight gags and pratfalls, so they could be enjoyed without requiring sound. To provide drama and excitement to silent movies, live music was played in sync with the action on the screen, on pianos, organs, and other instruments. When sound films became more prevalent during the 1920s, comedy films grew in popularity, as laughter could result from both burlesque situations but also from humorous dialogue.",
        "Crime" to "Crime films, in the broadest sense, is a film genre inspired by and analogous to the crime fiction literary genre. Films of this genre generally involve various aspects of crime and its detection. Stylistically, the genre may overlap and combine with many other genres, such as drama or gangster film, but also include comedy, and, in turn, is divided into many sub-genres, such as mystery, suspense or noir.",
    )

    fun getMovies(): List<Movie> {
        return listOf<Movie>(
            Movie(
                1,
                "Raya and the Last Dragon",
                "/lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
                "/9xeEGUZjgiKlI69jwIOi0hjKUIk.jpg",
                "2021-03-03",
                "Long ago, in the fantasy world of Kumandra, humans and dragons lived together in harmony. But when an evil force threatened the land, the dragons sacrificed themselves to save humanity. Now, 500 years later, that same evil has returned and it’s up to a lone warrior, Raya, to track down the legendary last dragon to restore the fractured land and its divided people.",
                listOf("Animation", "Family", "Fantasy", "Action", "Adventure"),
                "https://movies.disney.com/raya-and-the-last-dragon",
                "tt5109280"
            ),
            Movie(
                2,
                "Sentinelle",
                "/fFRq98cW9lTo6di2o4lK1qUAWaN.jpg",
                "/6TPZSJ06OEXeelx1U1VIAt0j9Ry.jpg",
                "2021-03-05",
                "Transferred home after a traumatizing combat mission, a highly trained French soldier uses her lethal skills to hunt down the man who hurt her sister.",
                listOf("Thriller", "Action", "Drama"),
                "https://www.netflix.com/title/81218770",
                "tt11734264"
            ),
            Movie(
                3,
                "Zack Snyder's Justice League",
                "/tnAuB8q5vv7Ax9UAEje5Xi4BXik.jpg",
                "/pcDc2WJAYGJTTvRSEIpRZwM3Ola.jpg",
                "2021-03-18",
                "Determined to ensure Superman's ultimate sacrifice was not in vain, Bruce Wayne aligns forces with Diana Prince with plans to recruit a team of metahumans to protect the world from an approaching threat of catastrophic proportions.",
                listOf("Action", "Adventure", "Fantasy"),
                "https://www.hbomax.com/zacksnydersjusticeleague",
                "tt12361974"
            ),
            Movie(
                4,
                "Tom & Jerry",
                "/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
                "/z7HLq35df6ZpRxdMAE0qE3Ge4SJ.jpg",
                "2021-02-11",
                "Tom the cat and Jerry the mouse get kicked out of their home and relocate to a fancy New York hotel, where a scrappy employee named Kayla will lose her job if she can’t evict Jerry before a high-class wedding at the hotel. Her solution? Hiring Tom to get rid of the pesky mouse.",
                listOf("Comedy", "Family", "Animation"),
                "https://www.tomandjerrymovie.com",
                "tt1361336"
            ),
            Movie(
                5,
                "Below Zero",
                "/dWSnsAGTfc8U27bWsy2RfwZs0Bs.jpg",
                "/srYya1ZlI97Au4jUYAktDe3avyA.jpg",
                "2021-01-29",
                "When a prisoner transfer van is attacked, the cop in charge must fight those inside and outside while dealing with a silent foe: the icy temperatures.",
                listOf("Action", "Crime", "Thriller"),
                "https://www.netflix.com/title/81038588",
                "tt9845564"
            ),
        )
    }
}