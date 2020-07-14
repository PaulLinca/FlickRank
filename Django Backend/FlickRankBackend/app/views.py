from django.shortcuts import render
import json
from rest_framework.views import APIView
from django.http import HttpResponse
from crawler import reviewCrawlerWrapper
from movies import movieWrapper
from posterDetection import posterRecognitionWrapper

class review(APIView):
    def get(self, request, imdbId):
        jsonResponse = reviewCrawlerWrapper.getReviewData(imdbId)
        return HttpResponse(jsonResponse, content_type="application/json")

class popularMovies(APIView):
    def get(self, request):
        jsonResponse = movieWrapper.getPopularMovies()
        return HttpResponse(jsonResponse, content_type="application/json")

class recommendedMovies(APIView):
    def get(self, request, imdbId):
        jsonResponse = movieWrapper.findRecommendedMovies(imdbId)
        return HttpResponse(jsonResponse, content_type="application/json")

class imageSearch(APIView):
    def post(self, request):
        bodyAsJson = json.loads(request.body)
        movieTitle = posterRecognitionWrapper.getMovieFromPoster(bodyAsJson['bitmapString'])
        return HttpResponse(movieTitle, content_type="text/plain")