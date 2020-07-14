from posterDetection import posterDetector
from posterDetection import posterRecognizer
import cv2
from imageio import imread
import io
import base64
from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True


def getMovieFromPoster(base64Image):
    # Convert encoded image to a OpenCV compatible image
    imageFromString = imread(io.BytesIO(base64.b64decode(base64Image + "==")))
    cv2CompatibleImage = cv2.cvtColor(imageFromString, cv2.COLOR_RGB2BGR)

    # Extract poster from image
    extractedPoster = posterDetector.detectPosterFromImage(cv2CompatibleImage)

    if extractedPoster is None:
        return "NULL"

    # Get movie from poster
    bestMatch = posterRecognizer.getMostSimilarPoster(extractedPoster)

    return bestMatch