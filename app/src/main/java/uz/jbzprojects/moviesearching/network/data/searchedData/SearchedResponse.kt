package uz.jbzprojects.moviesearching.network.data.searchedData

data class SearchedResponse(
    val page: Int,
    val results: List<SearchedMovies>,
    val total_pages: Int,
    val total_results: Int
)