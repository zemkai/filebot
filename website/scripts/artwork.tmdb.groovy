// filebot -script fn:artwork.tmdb /path/to/movies/

/*
 * Fetch movie artwork. The movie is determined using the parent folders name.
 */

// artwork/nfo helpers
include("lib/htpc")


args.eachMediaFolder{ dir ->
	// fetch only missing artwork by default
	if (_args.conflict == "skip" && dir.hasFile{it =~ /movie.nfo$/} && dir.hasFile{it =~ /poster.jpg$/} && dir.hasFile{it =~ /fanart.jpg$/}) {
		println "Skipping $dir"
		return
	}
	
	def videos = dir.listFiles{ it.isVideo() }
	
	def query = _args.query
	def options = []
	
	if (query) {
		// manual search
		options = TheMovieDB.searchMovie(query, _args.locale)
		// sort by relevance
		options = options.sortBySimilarity(query, { it.name })
	} else {
		// auto-detection
		options = net.sourceforge.filebot.media.MediaDetection.detectMovie(videos[0], null, TheMovieDB, _args.locale, true)
	}
	
	if (options.isEmpty()) {
		println "Movie not found: $query"
		return
	}
	
	// auto-select movie
	def movie = options[0]
	
	// maybe require user input
	if (options.size() != 1 && !_args.nonStrict && !java.awt.GraphicsEnvironment.headless) {
		movie = javax.swing.JOptionPane.showInputDialog(null, "Please select Movie:", dir.path, 3, null, options.toArray(), movie);
		if (movie == null) return null
	}
	
	println "$dir => $movie"
	try {
		fetchMovieArtworkAndNfo(dir, movie, dir.getFiles{ it.isVideo() }.sort{ it.length() }.reverse().findResult{ it }, true)
	} catch(e) {
		println "${e.class.simpleName}: ${e.message}"
	}
}
