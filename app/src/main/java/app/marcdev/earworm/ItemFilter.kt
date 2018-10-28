package app.marcdev.earworm

data class ItemFilter(var startDay: Int,
                      var startMonth: Int,
                      var startYear: Int,
                      var endDay: Int,
                      var endMonth: Int,
                      var endYear: Int,
                      var includeSongs: Boolean,
                      var includeAlbums: Boolean,
                      var includeArtists: Boolean,
                      var searchTerm: String)