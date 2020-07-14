#! python

import requests
from bs4 import BeautifulSoup
from crawler import constants
from crawler.review import Review, MovieReviewData

def getMovieReviews(imdbId):
    # movieTitle, overallRating = getMoviePageData(imdbId)
    spoilerFreeReviews, spoilerReviews = getReviewPageData(imdbId)

    # return MovieReviewData(movieTitle, overallRating, numberOfReviews, spoilerFreeReviews, spoilerReviews)
    return spoilerFreeReviews + spoilerReviews

def getMoviePageData(imdbId):
    page = requests.get(constants.imdbMovieBaseUrl + imdbId).text
    soup = BeautifulSoup(page, 'lxml')

    #Title
    title = soup.find('div', class_='title_wrapper').h1.text

    #Overall rating
    rating = soup.find('div', class_='ratingValue').strong.span.text

    return title, rating

def getReviews(reviewContainers, containSpoilers):
    allReviews = []
    for reviewDiv in reviewContainers:
        rating = reviewDiv.findAll('div', class_='ipl-ratings-bar')
        if len(rating) != 0:
            rating = rating[0].span.span.text
        else:
            rating = None
        title = reviewDiv.div.div.a.text
        text = reviewDiv.find('div', class_='content').div.text
        
        movieReview = Review('imdb', rating, title, text, containSpoilers)
        allReviews.append(movieReview)

    return allReviews

def getReviewPageData(imdbId):
    page = requests.get(constants.imdbMovieBaseUrl + imdbId + '/reviews?sort=reviewVolume&dir=desc').text
    soup = BeautifulSoup(page, 'lxml')

    #How many user reviews again
    numberOfReviews = soup.find('div', class_='header').div.span.text

    #Get review container
    container = soup.find('div', class_='lister-list')
    spoilerFreeReviewsContainer = container.findAll('div', class_='lister-item mode-detail imdb-user-review collapsable')
    spoilerReviewsContainer = container.findAll('div', class_='lister-item mode-detail imdb-user-review with-spoiler')
    
    spoilerFreeReviews = getReviews(spoilerFreeReviewsContainer, False)
    spoilerReviews = getReviews(spoilerReviewsContainer, True)

    return spoilerFreeReviews, spoilerReviews