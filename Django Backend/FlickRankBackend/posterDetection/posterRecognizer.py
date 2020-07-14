import cv2
import numpy as np
from matplotlib import pyplot as plt
from posterDetection import OriginalPoster
from imageio import imread
from urllib.request import urlopen


def splitImageFourWays(image, showResults = False):
    originalImage = image

    rows, cols, channels = originalImage.shape
    halfWidth = round(cols / 2)
    halfHeight = round(rows / 2)

    topLeft = originalImage[0:halfHeight, 0:halfWidth]
    topRight = originalImage[0:halfHeight, halfWidth:cols]
    bottomLeft = originalImage[halfHeight:rows, 0:halfWidth]
    bottomRight = originalImage[halfHeight:rows, halfWidth:cols]

    if showResults:
        print(rows, cols)
        print(halfWidth, halfHeight)
        cv2.imshow("Top Right", topRight)
        cv2.imshow("Top Left", topLeft)
        cv2.imshow("Bottom Left", bottomLeft)
        cv2.imshow("Bottom Right", bottomRight)
        cv2.waitKey(0)

    return topLeft, topRight, bottomLeft, bottomRight


def getHSVHistogram(image):
    # Convert image to HSV
    hsvImage = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # Set up arguments to calculate the histograms
    hueBins = 180
    saturationBins = 255
    histogramSize = [hueBins, saturationBins]

    hueRanges = [0, 180]  # hue varies from 0 to 179
    saturationRanges = [0, 256]  # saturation from 0 to 255
    ranges = hueRanges + saturationRanges
    channels = [0, 1]

    hsvHistogram = cv2.calcHist([hsvImage], channels, None, histogramSize, ranges, accumulate=False)
    cv2.normalize(hsvHistogram, hsvHistogram, alpha=0, beta=1, norm_type=cv2.NORM_MINMAX)

    plt.show()

    return hsvHistogram


def compareHSVHistograms(firstImage, secondImage):
    firstImageHistogram = getHSVHistogram(firstImage)
    secondImageHistogram = getHSVHistogram(secondImage)

    compareMethods = (
        ("Correlation", cv2.HISTCMP_CORREL),
        ("Chi-Squared", cv2.HISTCMP_CHISQR),
        ("Intersection", cv2.HISTCMP_INTERSECT),
        ("Hellinger", cv2.HISTCMP_BHATTACHARYYA))

    histograms = []
    for (methodName, method) in compareMethods:
        histograms.append(cv2.compareHist(firstImageHistogram, secondImageHistogram, method))

    return histograms


def hisEqulColor(img):
    ycrcb=cv2.cvtColor(img,cv2.COLOR_BGR2YCR_CB)
    channels=cv2.split(ycrcb)
    cv2.equalizeHist(channels[0],channels[0])
    cv2.merge(channels,ycrcb)
    cv2.cvtColor(ycrcb,cv2.COLOR_YCR_CB2BGR,img)
    return img


def compareHistograms(firstImage, secondImage, showResults = False):
    height, width, channels = firstImage.shape
    secondImage = cv2.resize(secondImage, (width, height))
    firstImage = hisEqulColor(firstImage)

    # Split images into 4 parts
    topLeft1, topRight1, bottomLeft1, bottomRight1 = splitImageFourWays(firstImage)
    topLeft2, topRight2, bottomLeft2, bottomRight2 = splitImageFourWays(secondImage)

    # Compare their hsv histograms
    dTopLeft = compareHSVHistograms(topLeft1, topLeft2)
    dTopRight = compareHSVHistograms(topRight1, topRight2)
    dBottomLeft = compareHSVHistograms(bottomLeft1, bottomLeft2)
    dBottomRight = compareHSVHistograms(bottomRight1, bottomRight2)

    average = dTopLeft[0] + dTopRight[0] + dBottomLeft[0] + dBottomRight[0]
    average /= 4

    if(showResults):
        cv2.imshow('First Image', firstImage)
        cv2.imshow('Second Image', secondImage)
        print(average)
        cv2.waitKey(0)

    return average


def urlToImage(url):
    response = urlopen(url)
    image = np.asarray(bytearray(response.read()), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)

    return image


def getMostSimilarPoster(extractedPoster):
    bestMatch = None
    bestAverage = -100
    for comparisonPoster in OriginalPoster.allPosters:
        comparisonPosterImage = urlToImage(comparisonPoster.poster)
        currentAverage = compareHistograms(extractedPoster, comparisonPosterImage)
        if currentAverage > bestAverage:
            bestAverage = currentAverage
            bestMatch = comparisonPoster

    return bestMatch.title