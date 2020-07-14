#! python

class Review:
    def __init__(self, source, rating, title, reviewText, containsSpoilers):
        self.source = source
        self.rating = rating
        self.title = title
        self.reviewText = reviewText
        self.containsSpoilers = containsSpoilers

    def print(self):
        if self.rating is None:
            print('No rating given')
            print(self.title)
            print(self.reviewText)
        else:
            print(self.rating)
            print(self.title)
            print(self.reviewText)

class MovieReviewData:
    def __init__(self, movieTitle, overallRating, numberOfReviews, spoilerFreeReviews, spoilerReviews):
        self.movieTitle = movieTitle
        self.overallRating = overallRating
        self.numberOfReviews = numberOfReviews
        self.allReviews = spoilerFreeReviews + spoilerReviews
