package interview.toyota.model

final case class MovieRank(movieId: String,
                       averageRating: Double,
                       numVotes: Int,
                       rank: Double)