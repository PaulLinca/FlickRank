import cv2
import numpy as np
from operator import itemgetter
import math


def detectPosterFromImage(image, showResults = False):
    # Make copy of the image
    originalImage = image

    # Resize image for easier viewing
    scaleFactor = 0.3
    originalImage = cv2.resize(originalImage, None, fx=scaleFactor, fy=scaleFactor)

    # Convert image to grayscale
    grayscaleImage = cv2.cvtColor(originalImage, cv2.COLOR_BGR2GRAY)

    # Get image dimensions
    rows, cols = grayscaleImage.shape

    # Blur image for better edges
    blurredImage = cv2.GaussianBlur(grayscaleImage, (7, 7), 0)

    # Apply threshold
    ret, thresholdedImage = cv2.threshold(blurredImage, 127, 255, cv2.THRESH_BINARY)

    # At this point closing and openings can be performed
    # This will help define better contours
    # Different images would need different operations in different orders
    # TODO: Revisit this

    # Apply closing
    closingKernel = np.ones((5, 5), np.uint8)
    closingImage = cv2.morphologyEx(thresholdedImage, cv2.MORPH_CLOSE, closingKernel)

    # Perform Canny Edge Detection
    # Values could vary
    cannyMinVal = 100
    cannyMaxVal = 200
    cannyImage = cv2.Canny(closingImage, cannyMinVal, cannyMaxVal)

    # Find the contours
    contourList, contourHierarchy = cv2.findContours(cannyImage, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)

    # Find contours that resemble squares and that are not too small
    minPerimeter = 500
    squaredContoursImage = np.zeros((rows, cols, 3), np.uint8)

    squaredContours = []
    squaredContoursAreas = []
    squaredContoursPoints = []
    for contourIndex, contour in enumerate(contourList):
        contourPerimeter = cv2.arcLength(contour, True)
        if contourPerimeter > minPerimeter:
            epsilon = contourPerimeter * 0.02
            approxPoints = cv2.approxPolyDP(contour, epsilon, True)
            if len(approxPoints) == 4:
                squaredContours.append(contour)
                squaredContoursAreas.append(cv2.contourArea(contour))
                squaredContoursPoints.append(approxPoints)

    # Decide which contour is the poster
    # Current solution: Find the contour with the largest area
    # TODO: Revisit this
    if len(squaredContours) == 0:
        return
    largestArea = max(squaredContoursAreas)
    largestAreaIndex = squaredContoursAreas.index(largestArea)
    largestAreaContour = squaredContours[largestAreaIndex]

    # Get contour corners
    largestAreaContourPointsApprox = squaredContoursPoints[largestAreaIndex]
    largestAreaContourCorners = np.concatenate(largestAreaContourPointsApprox).tolist()

    # Order the points
    xSorted = sorted(largestAreaContourCorners, key=lambda tup: tup[0])

    leftMost = xSorted[:2]
    rightMost = xSorted[2:]

    leftMostYSorted = sorted(leftMost, key=itemgetter(1))
    rightMostYSorted = sorted(rightMost, key=itemgetter(1))

    clockwisePoints = [leftMostYSorted[0], rightMostYSorted[0], rightMostYSorted[1], leftMostYSorted[1]]

    # Get corresponding points on the original image
    map(lambda x: x*scaleFactor, clockwisePoints)

    # Get max image size for perspective transform
    leftEdge = math.sqrt((clockwisePoints[0][0] - clockwisePoints[3][0])**2 + (clockwisePoints[0][1] - clockwisePoints[3][1])**2)
    rightEdge = math.sqrt((clockwisePoints[1][0] - clockwisePoints[2][0])**2 + (clockwisePoints[1][1] - clockwisePoints[2][1])**2)

    # Don't really need the width since we should keep the standard 2:3 ratio movie posters have
    # topEdge = math.sqrt((clockwisePoints[0][0] - clockwisePoints[1][0])**2 + (clockwisePoints[0][1] - clockwisePoints[1][1])**2)
    # bottomEdge = math.sqrt((clockwisePoints[2][0] - clockwisePoints[3][0])**2 + (clockwisePoints[2][1] - clockwisePoints[3][1])**2)
    # print(topEdge, bottomEdge)
    resultHeight = int(round(max(leftEdge, rightEdge)))
    # resultWidth = int(round(max(topEdge, bottomEdge)))
    resultWidth = int(round(resultHeight / 3 * 2))

    # Apply 4-point perspective transform
    src_pts = np.array(clockwisePoints, dtype=np.float32)
    dst_pts = np.array([[0, 0], [resultWidth, 0], [resultWidth, resultHeight], [0, resultHeight]], dtype=np.float32)
    perspectiveTransformation = cv2.getPerspectiveTransform(src_pts, dst_pts)
    finalPoster = cv2.warpPerspective(originalImage, perspectiveTransformation, (resultWidth, resultHeight))

    if showResults:
        cv2.imshow("Original image", originalImage)
        cv2.imshow("Grayscale image", grayscaleImage)
        cv2.imshow("Blurred image", blurredImage)
        cv2.imshow("Thresholded image", thresholdedImage)
        cv2.imshow("Closed image", closingImage)
        cv2.imshow("Canny image", cannyImage)

        for contourIndex, contour in enumerate(squaredContours):
            cv2.drawContours(squaredContoursImage, squaredContours, contourIndex, (255, 0, 0), 1)
            cv2.imshow("Contours", squaredContoursImage)

        cv2.drawContours(squaredContoursImage, [largestAreaContour], 0, (0, 255, 255), 1)
        cv2.imshow("Largest area contour", squaredContoursImage)

        print(largestAreaContourCorners)
        print(xSorted)
        print(leftMost)
        print(rightMost)
        print(leftMostYSorted)
        print(rightMostYSorted)
        print(clockwisePoints)
        print(leftEdge, rightEdge)

        cv2.imshow("Final poster", finalPoster)

        cv2.imwrite("C:\\Users\\Paul\\Desktop\\1.jpg", originalImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\2.jpg", grayscaleImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\3.jpg", blurredImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\4.jpg", thresholdedImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\5.jpg", closingImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\6.jpg", cannyImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\7.jpg", squaredContoursImage)
        cv2.imwrite("C:\\Users\\Paul\\Desktop\\8.jpg", finalPoster)

        cv2.waitKey(0)

    return finalPoster

cv2.imread("Pics/8.jpg")
detectPosterFromImage(cv2.imread("Pics/8.jpg"), True)