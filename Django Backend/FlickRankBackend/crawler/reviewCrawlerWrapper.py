#! python

from crawler import imdbCrawler, metacriticCrawler
import json

def getReviewData(imdbId):
    imdbReviews = imdbCrawler.getMovieReviews(imdbId)
    metacriticReviews = metacriticCrawler.getMovieReviews(imdbId)

    allReviews = imdbReviews + metacriticReviews
    allReviewsAsJson = json.dumps(allReviews, default=lambda o: o.__dict__, sort_keys=False, indent=4)
    return allReviewsAsJson
# # Print movie reviews
# print(movieReviewData.movieTitle)
# print(movieReviewData.overallRating)
# print(movieReviewData.numberOfReviews)
# movieReviewData.printReviews()
# print(movieReviewDataAsJson)